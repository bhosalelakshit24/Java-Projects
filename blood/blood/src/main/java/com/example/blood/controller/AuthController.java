package com.example.blood.controller;

import com.example.blood.model.User;
import com.example.blood.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepo;

    // show combined login/register page
    @GetMapping({"/", "/auth", "/login"})
    public String showAuth() {
        return "auth";   // templates/auth.html
    }

    @PostMapping("/register")
    public String register(@RequestParam String fullName,
                           @RequestParam String email,
                           @RequestParam String password,
                           RedirectAttributes ra) {

        if (userRepo.findByEmail(email) != null) {
            ra.addFlashAttribute("error", "Email already registered!");
            return "redirect:/auth";
        }

        User u = new User(fullName, email, password);
        userRepo.save(u);
        ra.addFlashAttribute("success", "Registration successful! Please login.");
        return "redirect:/auth";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes ra) {

        User user = userRepo.findByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            ra.addFlashAttribute("error", "Invalid email or password");
            return "redirect:/auth";
        }

        session.setAttribute("user", user);
        return "redirect:/blood";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth";
    }
}
