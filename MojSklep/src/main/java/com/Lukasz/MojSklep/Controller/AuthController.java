package com.Lukasz.MojSklep.Controller;

import com.Lukasz.MojSklep.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        var user = userService.getUserByUsername(username.toLowerCase().trim());
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("username", user.getUsername().toLowerCase().trim());
            return "redirect:/";  // przekierowanie na stronę główną po zalogowaniu
        } else {
            model.addAttribute("error", "Niepoprawna nazwa użytkownika lub hasło.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";  // nazwa widoku do rejestracji
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           Model model) {
        boolean added = userService.registerUser(username.toLowerCase().trim(), password);
        if (added) {
            model.addAttribute("success", "Użytkownik zarejestrowany!");
        } else {
            model.addAttribute("error", "Użytkownik już istnieje.");
        }
        return "register";
    }
}

