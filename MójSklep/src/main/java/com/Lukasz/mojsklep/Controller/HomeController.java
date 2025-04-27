package com.lukasz.mojsklep.controller;

import com.lukasz.mojsklep.model.Product;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {

    private final Map<String, Product> productCatalog = Map.of(
            "zeszyt", new Product("Zeszyt", "https://cdn.pixabay.com/photo/2017/04/04/07/06/address-book-2200584_1280.png", "Pomieści całą wiedzę na maturę", 5.99),
            "olowek", new Product("Ołówek", "https://cdn.pixabay.com/photo/2014/04/02/10/47/pencil-304559_1280.png", "Narysujesz nim co tylko zechcesz", 1.49),
            "dlugopis", new Product("Długopis", "https://cdn.pixabay.com/photo/2012/04/13/18/44/pen-33237_1280.png", "Idealny kandydat na starter pack do szkoły", 2.99)
    );

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("Products", productCatalog);

        int cartSize = 0;
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart != null) cartSize = cart.size();
        model.addAttribute("cartSize", cartSize);

        // Dodanie nazwy użytkownika do modelu, jeśli jest zalogowany
        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        return "home";
    }

    @PostMapping("/dodaj")
    public String addToCart(@RequestParam String id, HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        Product p = productCatalog.get(id);
        if (p != null) cart.add(p);
        session.setAttribute("cart", cart);
        return "redirect:/";
    }

    @GetMapping("/koszyk")
    public String cart(HttpSession session, Model model) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart == null) cart = new ArrayList<>();
        model.addAttribute("cart", cart);
        model.addAttribute("cartSize", cart.size());

        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);

        return "koszyk";
    }

    @PostMapping("/usun")
    public String removeFromCart(@RequestParam int index, HttpSession session) {
        List<Product> cart = (List<Product>) session.getAttribute("cart");
        if (cart != null && index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        session.setAttribute("cart", cart);
        return "redirect:/koszyk";
    }
}

