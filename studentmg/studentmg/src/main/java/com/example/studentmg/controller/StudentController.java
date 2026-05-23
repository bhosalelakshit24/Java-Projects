package com.example.studentmg.controller;

import com.example.studentmg.entity.Student;
import com.example.studentmg.repository.StudentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class StudentController {

    private final StudentRepository studentRepo;

    public StudentController(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    // ====================== DASHBOARD ======================
    @GetMapping("/home")
    public String home(Model model, Principal principal) {

        // show logged-in username in header
        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        }

        // ---------- LIST ----------
        model.addAttribute("students", studentRepo.findAll());

        // ---------- STATS ----------
        long total      = studentRepo.count();
        long active     = studentRepo.countByStatus("Active");
        long onLeave    = studentRepo.countByStatus("On Leave");
        long graduated  = studentRepo.countByStatus("Graduated");
        long dropped    = studentRepo.countByStatus("Dropout");

        long firstYear  = studentRepo.countByYear("First Year");
        long secondYear = studentRepo.countByYear("Second Year");
        long thirdYear  = studentRepo.countByYear("Third Year");

        // percentages for progress bars
        model.addAttribute("activePct", pct(active, total));
        model.addAttribute("onLeavePct", pct(onLeave, total));
        model.addAttribute("graduatedPct", pct(graduated, total));

        // numbers for cards
        model.addAttribute("totalStudents", total);
        model.addAttribute("activeStudents", active);
        model.addAttribute("onLeaveStudents", onLeave);
        model.addAttribute("graduatedStudents", graduated);
        model.addAttribute("droppedStudents", dropped);

        // year-wise count
        model.addAttribute("firstYearStudents", firstYear);
        model.addAttribute("secondYearStudents", secondYear);
        model.addAttribute("thirdYearStudents", thirdYear);

        return "home";
    }


    // ====================== ADD FORM ======================
    @GetMapping("/students/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("formTitle", "Add New Student");
        return "student-form";
    }


    // ====================== SAVE NEW STUDENT ======================
    @PostMapping("/students")
    public String addStudent(@ModelAttribute Student student) {
        studentRepo.save(student);
        return "redirect:/home";
    }


    // ====================== EDIT FORM ======================
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Student s = studentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ID: " + id));

        model.addAttribute("student", s);
        model.addAttribute("formTitle", "Edit Student");
        return "student-form";
    }


    // ====================== UPDATE STUDENT ======================
    @PostMapping("/students/{id}")
    public String updateStudent(@PathVariable Long id, @ModelAttribute Student student) {
        student.setId(id);
        studentRepo.save(student);
        return "redirect:/home";
    }


    // ====================== DELETE STUDENT ======================
    @PostMapping("/students/{id}/delete")
    public String deleteStudent(@PathVariable Long id) {
        studentRepo.deleteById(id);
        return "redirect:/home";
    }
    // ====================== ATTENDANCE PAGE ======================
    @GetMapping("/attendance")
    public String attendance(Model model, Principal principal) {

        if (principal != null) {
            model.addAttribute("loggedUser", principal.getName());
        }

        // reuse same students list
        model.addAttribute("students", studentRepo.findAll());

        return "attendance";     // -> templates/attendance.html
    }


    // ====================== HELP METHOD ======================
    private int pct(long part, long all) {
        if (all <= 0) return 0;
        return (int) ((part * 100) / all);
    }
}
