package com.example.studentmg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class PageController {

    private void addLoggedUser(Model model, Principal principal) {
        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        }
    }

    @GetMapping("/inbox")
    public String inbox(Model model, Principal principal) {
        addLoggedUser(model, principal);
        return "inbox";          // -> templates/inbox.html
    }

    @GetMapping("/calendar")
    public String calendar(Model model, Principal principal) {
        addLoggedUser(model, principal);
        return "calendar";       // -> templates/calendar.html
    }

    @GetMapping("/teachers")
    public String teachers(Model model, Principal principal) {
        addLoggedUser(model, principal);
        return "teachers";       // -> templates/teachers.html
    }
}
