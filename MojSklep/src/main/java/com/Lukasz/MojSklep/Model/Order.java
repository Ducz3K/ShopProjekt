package com.Lukasz.MojSklep.Model;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private String username;
    private String address;
    private String paymentMethod;
    private String date;
    private List<Product> products = new ArrayList<>();

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    public void addProduct(Product product) {
        this.products.add(product);
    }
}
