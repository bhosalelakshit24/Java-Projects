package com.example.recipe.controller;

import com.example.recipe.entity.Recipe;
import com.example.recipe.repository.RecipeRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RecipeController {

    private final RecipeRepository recipeRepository;

    public RecipeController(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    // HOME -> list all recipes
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("recipes", recipeRepository.findAll());
        return "home";   // templates/home.html
    }

    // ✅ THIS is the URL you’re trying to open
    @GetMapping("/recipes/new")
    public String showCreateForm(Model model) {
        model.addAttribute("recipe", new Recipe());
        model.addAttribute("formTitle", "Add New Recipe");
        return "recipe-form";   // templates/recipe-form.html
    }

    // Save new recipe
    @PostMapping("/recipes")
    public String createRecipe(@ModelAttribute Recipe recipe) {
        recipeRepository.save(recipe);
        return "redirect:/home";
    }

    // Edit form
    @GetMapping("/recipes/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid recipe id: " + id));
        model.addAttribute("recipe", recipe);
        model.addAttribute("formTitle", "Edit Recipe");
        return "recipe-form";
    }

    // Update
    @PostMapping("/recipes/{id}")
    public String updateRecipe(@PathVariable Long id, @ModelAttribute Recipe recipe) {
        recipe.setId(id);
        recipeRepository.save(recipe);
        return "redirect:/home";
    }

    // Delete
    @PostMapping("/recipes/{id}/delete")
    public String deleteRecipe(@PathVariable Long id) {
        recipeRepository.deleteById(id);
        return "redirect:/home";
    }
}