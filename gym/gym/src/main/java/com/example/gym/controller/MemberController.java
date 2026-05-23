package com.example.gym.controller;

import com.example.gym.entity.Member;
import com.example.gym.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // LIST PAGE
    @GetMapping
    public String listMembers(Model model, Principal principal) {

        String username = (principal != null) ? principal.getName() : "User";
        model.addAttribute("loggedUser", username);

        model.addAttribute("members", memberService.getAllMembers());

        return "members";
    }

    // SHOW ADD FORM
    @GetMapping("/new")
    public String showAddForm(Model model, Principal principal) {

        String username = (principal != null) ? principal.getName() : "User";
        model.addAttribute("loggedUser", username);

        model.addAttribute("member", new Member());
        model.addAttribute("formTitle", "Add Member");

        return "member-form";
    }

    // SAVE NEW OR UPDATED MEMBER
    @PostMapping
    public String saveMember(@ModelAttribute("member") Member member) {
        memberService.saveMember(member);
        return "redirect:/members";
    }

    // SHOW EDIT FORM
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, Principal principal) {

        String username = (principal != null) ? principal.getName() : "User";
        model.addAttribute("loggedUser", username);

        Member member = memberService.getMemberById(id);
        model.addAttribute("member", member);
        model.addAttribute("formTitle", "Edit Member");

        return "member-form";
    }

    // DELETE MEMBER
    @GetMapping("/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return "redirect:/members";
    }
}
