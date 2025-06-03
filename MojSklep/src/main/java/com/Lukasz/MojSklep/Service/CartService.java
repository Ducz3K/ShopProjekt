package com.Lukasz.MojSklep.Service;

import com.Lukasz.MojSklep.Model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

@Service
public class CartService {

    private final ProductService productService;

    private final Map<String, List<List<Product>>> userOrders = new HashMap<>();

    public CartService(ProductService productService) {
        this.productService = productService;
    }

    public void saveUserOrder(String username, List<Product> cart) {
        List<List<Product>> orders = userOrders.getOrDefault(username, new ArrayList<List<Product>>());
        orders.add(new ArrayList<>(cart)); // kopiujemy cart do nowej listy
        userOrders.put(username, orders);
    }

    public List<List<Product>> getUserOrders(String username) {
        return userOrders.getOrDefault(username, new ArrayList<>());
    }

    public void addToCart(String id, HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        Product product = productService.getProductById(id);
        if (product != null) {
            cart.add(product);
        }
        session.setAttribute("cart", cart);
    }

    public List<Product> getCart(HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        return cart != null ? cart : new ArrayList<>();
    }

    public void removeFromCart(int index, HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart != null && index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        session.setAttribute("cart", cart);
    }

    public int getCartSize(HttpSession session) {
        List<Product> cart = getCart(session);
        return cart.size();
    }


    public void saveOrderToFile(String username, List<Product> cart) {
        try (FileWriter writer = new FileWriter("orders.csv", true)) {
            writer.write("Użytkownik: " + username + ", Data: " + new Date() + "\n");
            for (Product p : cart) {
                writer.write(" - " + p.getName() + ", " + p.getPrice() + " zł\n");
            }
            writer.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveOrderToFile(String username,
                                List<Product> cart,
                                String firstName,
                                String lastName,
                                String street,
                                String postalCode,
                                String city,
                                String paymentMethod) {
        try (FileWriter writer = new FileWriter("orders.csv", true)) {
            writer.write("=== Nowe zamówienie ===\n");
            writer.write("Użytkownik: " + username + "\n");
            writer.write("Adres: " + firstName + " " + lastName + ", " + street + ", " + postalCode + " " + city + "\n");
            writer.write("Płatność: " + paymentMethod + "\n");
            writer.write("Produkty:\n");

            for (Product p : cart) {
                writer.write(" - " + p.getName() + ", " + p.getPrice() + " zł\n");
            }

            writer.write("Data: " + new Date() + "\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> readAllOrders() {
        List<String> orders = new ArrayList<>();
        File file = new File("orders.csv");

        if (!file.exists()) return orders;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("===")) {
                    if (sb.length() > 0) orders.add(sb.toString());
                    sb = new StringBuilder();
                }
                sb.append(line).append("\n");
            }
            if (sb.length() > 0) orders.add(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return orders;
    }

    public List<List<Product>> getOrdersFromFileForUser(String username) {
        List<List<Product>> userOrdersFromFile = new ArrayList<>();
        File file = new File("orders.csv");
        if (!file.exists()) return userOrdersFromFile;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            List<Product> currentOrder = new ArrayList<>();
            boolean correctUser = false;
            boolean firstOrderHandled = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("=== Nowe zamówienie ===")) {
                    if (correctUser && !currentOrder.isEmpty()) {
                        userOrdersFromFile.add(new ArrayList<>(currentOrder));
                    }
                    currentOrder.clear();
                    correctUser = false;
                    firstOrderHandled = true;
                } else {
                    if (line.startsWith("Użytkownik: ")) {
                        // Obsłuż przypadek 1-liniowego zamówienia (brak nagłówka)
                        if (!firstOrderHandled && currentOrder.isEmpty()) {
                            firstOrderHandled = true;
                        }
                        String userInFile = line.substring("Użytkownik: ".length()).split(",")[0].trim();
                        correctUser = userInFile.trim().equalsIgnoreCase(username.trim());
                    } else if (correctUser && line.startsWith(" - ")) {
                        String[] parts = line.substring(3).split(", ");
                        if (parts.length == 2) {
                            String productName = parts[0].trim();
                            String priceStr = parts[1].replace(" zł", "").trim();
                            try {
                                double price = Double.parseDouble(priceStr);
                                Product p = new Product();
                                p.setName(productName);
                                p.setPrice(price);
                                currentOrder.add(p);
                            } catch (NumberFormatException e) {

                            }
                        }
                    }
                }
            }

            if (correctUser && !currentOrder.isEmpty()) {
                userOrdersFromFile.add(currentOrder);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return userOrdersFromFile;
    }
}

