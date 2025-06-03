package com.Lukasz.MojSklep.Service;

import com.Lukasz.MojSklep.Model.Order;
import com.Lukasz.MojSklep.Model.Product;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    private final String ORDERS_FILE = "orders.csv";

    public List<Order> getAllOrdersDetailed() {
        List<Order> allOrders = new ArrayList<>();

        File file = new File(ORDERS_FILE);
        if (!file.exists()) {
            System.out.println("Plik " + ORDERS_FILE + " nie istnieje w katalogu roboczym: " + file.getAbsolutePath());
            return allOrders;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Order currentOrder = null;
            boolean readingProducts = false;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.equals("=== Nowe zamówienie ===")) {
                    if (currentOrder != null) {
                        allOrders.add(currentOrder);
                    }
                    currentOrder = new Order();
                    readingProducts = false;
                } else if (line.startsWith("Użytkownik: ")) {
                    if (currentOrder != null) {
                        currentOrder.setUsername(line.substring("Użytkownik: ".length()).trim());
                    }
                } else if (line.startsWith("Adres: ")) {
                    if (currentOrder != null) {
                        currentOrder.setAddress(line.substring("Adres: ".length()).trim());
                    }
                } else if (line.startsWith("Płatność: ")) {
                    if (currentOrder != null) {
                        currentOrder.setPaymentMethod(line.substring("Płatność: ".length()).trim());
                    }
                } else if (line.equals("Produkty:")) {
                    readingProducts = true;
                } else if (readingProducts && line.startsWith("- ")) {
                    if (currentOrder != null) {
                        String[] parts = line.substring(2).split(", ");
                        if (parts.length == 2) {
                            try {
                                String productName = parts[0].trim();
                                double price = Double.parseDouble(parts[1].replace(" zł", "").trim());
                                Product p = new Product();
                                p.setName(productName);
                                p.setPrice(price);
                                currentOrder.getProducts().add(p);
                            } catch (NumberFormatException e) {
                                // ignorujemy błędy parsowania ceny
                            }
                        }
                    }
                } else if (line.startsWith("Data: ")) {
                    if (currentOrder != null) {
                        currentOrder.setDate(line.substring("Data: ".length()).trim());
                    }
                    readingProducts = false;
                }
            }
            if (currentOrder != null) {
                allOrders.add(currentOrder);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return allOrders;
    }


    public List<Order> getOrdersForUser(String username) {
        if (username == null) return new ArrayList<>();
        List<Order> allOrders = getAllOrdersDetailed();
        List<Order> userOrders = new ArrayList<>();

        String userKey = username.toLowerCase().trim();

        for (Order order : allOrders) {
            if (order.getUsername() != null &&
                    order.getUsername().toLowerCase().trim().equals(userKey)) {
                userOrders.add(order);
            }
        }

        return userOrders;
    }
}




