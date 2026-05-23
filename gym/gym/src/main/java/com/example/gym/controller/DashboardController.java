package com.example.gym.controller;

import com.example.gym.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class DashboardController {

    private final MemberService memberService;

    public DashboardController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model, Principal principal) {

        String username = (principal != null) ? principal.getName() : "User";
        model.addAttribute("loggedUser", username);

        long total = memberService.countAll();
        long active = memberService.countByStatus("ACTIVE");
        long paused = memberService.countByStatus("PAUSED");
        long expired = memberService.countByStatus("EXPIRED");

        model.addAttribute("totalMembers", total);
        model.addAttribute("activeMembers", active);
        model.addAttribute("pausedMembers", paused);
        model.addAttribute("expiredMembers", expired);

        return "dashboard";
    }
}
