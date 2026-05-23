package com.example.blood.controller;

import com.example.blood.model.BloodRecord;
import com.example.blood.model.User;
import com.example.blood.repository.BloodRecordRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blood")
public class BloodController {

    @Autowired
    private BloodRecordRepository bloodRepo;

    private User getLoggedUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // Dashboard: list all blood records
    @GetMapping
    public String listBlood(HttpSession session, Model model) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        model.addAttribute("records", bloodRepo.findAll());
        model.addAttribute("name", user.getFullName());
        return "blood-list";
    }

    // Add new record
    @GetMapping("/new")
    public String newRecord(HttpSession session, Model model) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        model.addAttribute("record", new BloodRecord());
        model.addAttribute("name", user.getFullName());
        return "blood-form";
    }

    // Edit record
    @GetMapping("/edit/{id}")
    public String editRecord(@PathVariable Long id,
                             HttpSession session,
                             Model model) {

        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        BloodRecord record = bloodRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
        model.addAttribute("record", record);
        model.addAttribute("name", user.getFullName());
        return "blood-form";
    }

    // Save (add or update)
    @PostMapping("/save")
    public String saveRecord(@ModelAttribute("record") BloodRecord record,
                             HttpSession session) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        bloodRepo.save(record);
        return "redirect:/blood";
    }

    // Delete
    @GetMapping("/delete/{id}")
    public String deleteRecord(@PathVariable Long id,
                               HttpSession session) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        bloodRepo.deleteById(id);
        return "redirect:/blood";
    }
}
