package com.example.carpl.controller;

import com.example.carpl.model.User;
import com.example.carpl.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    // Root & /login & /auth -> show slide page
    @GetMapping({"/", "/login", "/auth"})
    public String showAuthPage() {
        return "auth";   // templates/auth.html
    }

    // --------- REGISTER (Sign up form) ----------
    @PostMapping("/register")
    public String doRegister(@RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam(required = false) String phoneNumber,
                             RedirectAttributes ra) {

        if (userRepository.findByEmail(email) != null) {
            ra.addFlashAttribute("error", "Email already registered");
            return "redirect:/auth";
        }

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);   // optional
        userRepository.save(user);

        ra.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/auth";
    }

    // --------- LOGIN (Login form) ----------
    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session,
                          RedirectAttributes ra) {

        User user = userRepository.findByEmail(email);

        if (user == null || !user.getPassword().equals(password)) {
            ra.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/auth";
        }

        session.setAttribute("loggedUser", user);
        return "redirect:/parking/list";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}
