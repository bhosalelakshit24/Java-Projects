package com.example.assignment.controller;

import com.example.assignment.entity.Assignment;
import com.example.assignment.repository.AssignmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AssignmentController {

    private final AssignmentRepository repo;

    public AssignmentController(AssignmentRepository repo) {
        this.repo = repo;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("assignments", repo.findAll());
        return "home";
    }

    @GetMapping("/assignments/new")
    public String newAssignmentForm(Model model) {
        Assignment assignment = new Assignment();
        assignment.setStatus("PENDING");
        model.addAttribute("assignment", assignment);
        model.addAttribute("formTitle", "Add New Assignment");
        return "assignment-form";
    }

    @PostMapping("/assignments")
    public String createAssignment(@ModelAttribute Assignment assignment) {
        repo.save(assignment);
        return "redirect:/home";
    }

    @GetMapping("/assignments/edit/{id}")
    public String editAssignmentForm(@PathVariable Long id, Model model) {
        Assignment assignment = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
        model.addAttribute("assignment", assignment);
        model.addAttribute("formTitle", "Edit Assignment");
        return "assignment-form";
    }

    @PostMapping("/assignments/{id}")
    public String updateAssignment(@PathVariable Long id, @ModelAttribute Assignment assignment) {
        assignment.setId(id);
        repo.save(assignment);
        return "redirect:/home";
    }

    @PostMapping("/assignments/{id}/delete")
    public String deleteAssignment(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/home";
    }

    @PostMapping("/assignments/{id}/done")
    public String markDone(@PathVariable Long id) {
        Assignment assignment = repo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id: " + id));
        assignment.setStatus("DONE");
        repo.save(assignment);
        return "redirect:/home";
    }
}
