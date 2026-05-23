package com.example.notes.controller;

import com.example.notes.model.Note;
import com.example.notes.model.User;
import com.example.notes.repository.NoteRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/notes")
public class NotesController {

    @Autowired
    private NoteRepository noteRepo;

    @GetMapping
    public String listNotes(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/auth";

        model.addAttribute("notes", noteRepo.findByUserId(user.getId()));
        model.addAttribute("name", user.getFullName());
        return "notes-list";
    }

    @GetMapping("/new")
    public String newNote(Model model) {
        model.addAttribute("note", new Note());
        return "note-form";
    }

    @PostMapping("/save")
    public String saveNote(@ModelAttribute Note note, HttpSession session) {
        User user = (User) session.getAttribute("user");
        note.setUser(user);
        noteRepo.save(note);
        return "redirect:/notes";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Note note = noteRepo.findById(id).orElse(null);
        model.addAttribute("note", note);
        return "note-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        noteRepo.deleteById(id);
        return "redirect:/notes";
    }
}
