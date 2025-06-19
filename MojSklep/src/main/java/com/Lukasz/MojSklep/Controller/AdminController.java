package com.Lukasz.MojSklep.Controller;

import com.Lukasz.MojSklep.Model.Order;
import com.Lukasz.MojSklep.Service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    private final OrderService orderService;

    public AdminController(OrderService orderService) {
        this.orderService = orderService;
    }
    //  obsługuje zapytania GET pod URL-em "/admin-orders" , obsługuje wszystkie złożone zamówienia i wysyła do widoku
    @GetMapping("/admin-orders")
    public String showAllOrders(Model model) {
        List<Order> allOrders = orderService.getAllOrdersDetailed();
        allOrders.forEach(o -> System.out.println("Order by user: " + o.getUsername() + ", products: " + o.getProducts().size()));
        model.addAttribute("allOrders", allOrders);
        return "admin-orders";
    }
}
