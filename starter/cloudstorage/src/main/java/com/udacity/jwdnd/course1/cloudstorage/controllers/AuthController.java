package com.udacity.jwdnd.course1.cloudstorage.controllers;


import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String openLoginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/signup")
    public String openSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@ModelAttribute User user, Model model) {
        if (this.userService.getUserByUsername(user.getUsername()) != null) {
            String signupError = "Username " + user.getUsername() + " is already in use";
            model.addAttribute("signupError", signupError);
            return "signup";
        } else {
            int newUserId = this.userService.addUser(user);
            model.addAttribute("signupSuccess", true);
            model.addAttribute("newUserId", newUserId);
            return "signup";
        }
    }
}
