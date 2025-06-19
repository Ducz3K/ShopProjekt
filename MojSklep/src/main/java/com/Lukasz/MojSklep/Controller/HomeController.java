package com.Lukasz.MojSklep.Controller;

import com.Lukasz.MojSklep.Model.Product;
import com.Lukasz.MojSklep.Service.CartService;
import com.Lukasz.MojSklep.Service.ProductService;
import com.Lukasz.MojSklep.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final CartService cartService;
    private final UserService userService;

    public HomeController(ProductService productService, CartService cartService, UserService userService) {
        this.productService = productService;
        this.cartService = cartService;
        this.userService = userService;
    }
// strona główna
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("cartSize", cartService.getCartSize(session));
        model.addAttribute("username", session.getAttribute("username"));
        return "home";
    }
// dodawanie do koszyka
    @PostMapping("/dodaj")
    public String addToCart(@RequestParam String id, HttpSession session) {
        cartService.addToCart(id, session);
        return "redirect:/";
    }

    @GetMapping("/koszyk")
    public String cart(HttpSession session, Model model) {
        model.addAttribute("cart", cartService.getCart(session));
        model.addAttribute("cartSize", cartService.getCartSize(session));
        model.addAttribute("username", session.getAttribute("username"));
        return "koszyk";
    }
// usuwanie produktów w koszyku
    @PostMapping("/usun")
    public String removeFromCart(@RequestParam int index, HttpSession session) {
        cartService.removeFromCart(index, session);
        return "redirect:/koszyk";
    }

// składanie zamówień
    @PostMapping("/zamow")
    public String placeOrder(HttpSession session,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam String street,
                             @RequestParam String postalCode,
                             @RequestParam String city,
                             @RequestParam String paymentMethod) {
        String username = (String) session.getAttribute("username");
        List<Product> cart = cartService.getCart(session);

        if (username != null && !cart.isEmpty()) {
            cartService.saveUserOrder(username, cart);
            cartService.saveOrderToFile(
                    username, cart,
                    firstName, lastName, street, postalCode, city, paymentMethod
            );
            session.setAttribute("cart", new ArrayList<>());
        }

        return "redirect:/koszyk?success=true";

    }

    @GetMapping("/moje-zamowienia")
    public String myOrders(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login"; // jeśli niezalogowany, przekieruj na login
        }

        // Pobierz listę zamówień użytkownika z serwisu koszyka
        List<List<Product>> orders = cartService.getUserOrders(username);
        model.addAttribute("orders", orders);
        model.addAttribute("username", username);

        return "myOrders";

    }
}
