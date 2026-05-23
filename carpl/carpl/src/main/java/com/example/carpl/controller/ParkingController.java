package com.example.carpl.controller;

import com.example.carpl.model.Parking;
import com.example.carpl.model.User;
import com.example.carpl.repository.ParkingRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/parking")
public class ParkingController {

    @Autowired
    private ParkingRepository parkingRepository;

    // helper: redirect to login if not logged in
    private boolean checkLogin(HttpSession session) {
        return session.getAttribute("loggedUser") != null;
    }

    @GetMapping("/list")
    public String listParking(Model model,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (!checkLogin(session)) {
            ra.addFlashAttribute("error", "Please login first.");
            return "redirect:/login";
        }

        model.addAttribute("records", parkingRepository.findAll());
        User u = (User) session.getAttribute("loggedUser");
        model.addAttribute("userName", u.getFullName());
        return "parking-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (!checkLogin(session)) {
            ra.addFlashAttribute("error", "Please login first.");
            return "redirect:/login";
        }

        model.addAttribute("parking", new Parking());
        model.addAttribute("formTitle", "Add Parking Record");
        return "parking-form";
    }

    @PostMapping("/save")
    public String saveParking(@ModelAttribute("parking") Parking parking,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (!checkLogin(session)) {
            ra.addFlashAttribute("error", "Please login first.");
            return "redirect:/login";
        }

        // if status is LEFT and outTime is null, set now
        if ("LEFT".equalsIgnoreCase(parking.getStatus()) && parking.getOutTime() == null) {
            parking.setOutTime(LocalDateTime.now());
        }

        parkingRepository.save(parking);
        return "redirect:/parking/list";
    }

    @GetMapping("/edit/{id}")
    public String editParking(@PathVariable Long id,
                              Model model,
                              HttpSession session,
                              RedirectAttributes ra) {
        if (!checkLogin(session)) {
            ra.addFlashAttribute("error", "Please login first.");
            return "redirect:/login";
        }

        Parking parking = parkingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid id:" + id));

        model.addAttribute("parking", parking);
        model.addAttribute("formTitle", "Edit Parking Record");
        return "parking-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteParking(@PathVariable Long id,
                                HttpSession session,
                                RedirectAttributes ra) {
        if (!checkLogin(session)) {
            ra.addFlashAttribute("error", "Please login first.");
            return "redirect:/login";
        }

        parkingRepository.deleteById(id);
        return "redirect:/parking/list";
    }
}
