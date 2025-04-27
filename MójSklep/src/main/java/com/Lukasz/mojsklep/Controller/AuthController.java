package com.lukasz.mojsklep.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "Login"; // Wskazanie widoku login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        // Sprawdzanie poprawności danych logowania
        if ("Lukasz".equals(username) && "1234".equals(password)) {
            // Zapisanie użytkownika w sesji
            session.setAttribute("username", username);

            // Po zalogowaniu przekierowanie na stronę główną
            return "redirect:/";
        } else {
            // Błędne dane logowania, pokazanie komunikatu o błędzie
            model.addAttribute("error", "Niepoprawna nazwa użytkownika lub hasło.");
            return "Login"; // Pozostanie na stronie logowania
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Usunięcie użytkownika z sesji
        return "redirect:/"; // Powrót na stronę główną
    }
}
