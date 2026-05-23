package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.model.User;
import com.example.employee.repository.EmployeeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepo;

    private User getLoggedUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // LIST
    @GetMapping
    public String listEmployees(HttpSession session, Model model) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        model.addAttribute("employees", employeeRepo.findAll());
        model.addAttribute("name", user.getFullName());
        return "employee-list";   // templates/employee-list.html
    }

    // ADD
    @GetMapping("/new")
    public String newEmployee(HttpSession session, Model model) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        model.addAttribute("employee", new Employee());
        model.addAttribute("name", user.getFullName());
        return "employee-form";
    }

    // EDIT
    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable Long id,
                               HttpSession session,
                               Model model) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        Employee emp = employeeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid employee id: " + id));

        model.addAttribute("employee", emp);
        model.addAttribute("name", user.getFullName());
        return "employee-form";
    }

    // SAVE (add + update)
    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute Employee employee,
                               HttpSession session) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        employeeRepo.save(employee);
        return "redirect:/employees";
    }

    // DELETE
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id,
                                 HttpSession session) {
        User user = getLoggedUser(session);
        if (user == null) return "redirect:/auth";

        employeeRepo.deleteById(id);
        return "redirect:/employees";
    }
}
