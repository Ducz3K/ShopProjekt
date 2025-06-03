package com.Lukasz.MojSklep.Controller;

import com.Lukasz.MojSklep.Model.Order;
import com.Lukasz.MojSklep.Service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/myOrders")
    public String myOrders(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        model.addAttribute("username", username);
        model.addAttribute("userOrders", orderService.getOrdersForUser(username));

        return "myOrders";
    }
}





