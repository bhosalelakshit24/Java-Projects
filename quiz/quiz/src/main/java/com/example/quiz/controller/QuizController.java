package com.example.quiz.controller;

import com.example.quiz.model.Question;
import com.example.quiz.model.Quiz;
import com.example.quiz.model.User;
import com.example.quiz.repository.QuestionRepository;
import com.example.quiz.repository.QuizRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/quizzes")
public class QuizController {

    @Autowired
    private QuizRepository quizRepo;

    @Autowired
    private QuestionRepository questionRepo;

    // -------- helper: get logged-in user from session ----------
    private User getLoggedUser(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    // ===================== LIST ALL QUIZZES =====================
    @GetMapping
    public String listQuizzes(HttpSession session, Model model) {
        User user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/auth";
        }

        model.addAttribute("quizzes", quizRepo.findAll());
        model.addAttribute("name", user.getFullName());
        return "quiz-list";     // templates/quiz-list.html
    }

    // ===================== CREATE QUIZ (BASIC INFO) =============
    @GetMapping("/builder/new")
    public String showCreateQuizForm(HttpSession session, Model model) {
        User user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/auth";
        }

        model.addAttribute("quiz", new Quiz());
        model.addAttribute("name", user.getFullName());
        return "quiz-builder";  // templates/quiz-builder.html
    }

    @PostMapping("/builder/save")
    public String saveNewQuiz(@ModelAttribute Quiz quiz,
                              HttpSession session) {
        User user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/auth";
        }

        quiz.setOwner(user);
        quizRepo.save(quiz);

        // after saving basic info, go to add-questions page
        return "redirect:/quizzes/" + quiz.getId() + "/questions/new";
    }

    // ===================== ADD QUESTIONS TO QUIZ ================
    @GetMapping("/{quizId}/questions/new")
    public String showAddQuestionForm(@PathVariable Long quizId,
                                      HttpSession session,
                                      Model model) {
        User user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/auth";
        }

        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz id: " + quizId));

        model.addAttribute("quiz", quiz);
        model.addAttribute("question", new Question());
        model.addAttribute("questions", questionRepo.findByQuizId(quizId));
        model.addAttribute("name", user.getFullName());
        return "question-form";   // templates/question-form.html
    }

    @PostMapping("/{quizId}/questions/save")
    public String saveQuestion(@PathVariable Long quizId,
                               @ModelAttribute Question question,
                               HttpSession session) {

        User user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/auth";
        }

        Quiz quiz = quizRepo.findById(quizId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid quiz id: " + quizId));

        question.setQuiz(quiz);
        questionRepo.save(question);

        // stay on same page to keep adding questions
        return "redirect:/quizzes/" + quizId + "/questions/new";
    }

    @GetMapping("/{quizId}/questions/delete/{id}")
    public String deleteQuestion(@PathVariable Long quizId,
                                 @PathVariable Long id,
                                 HttpSession session) {
        User user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/auth";
        }

        questionRepo.deleteById(id);
        return "redirect:/quizzes/" + quizId + "/questions/new";
    }

    // ===================== DELETE QUIZ (optional) ===============
    @GetMapping("/delete/{id}")
    public String deleteQuiz(@PathVariable Long id,
                             HttpSession session) {
        User user = getLoggedUser(session);
        if (user == null) {
            return "redirect:/auth";
        }

        quizRepo.deleteById(id);
        return "redirect:/quizzes";
    }
}
