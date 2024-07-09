package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

    @PostMapping("/logout")
    public String logout() {
        return "login";
    }

    @GetMapping("/signup")
    public String openSignUpPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView signup(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        if (this.userService.getUserByUsername(user.getUsername()) != null) {
            String signupError = "Username " + user.getUsername() + " is already in use";
            redirectAttributes.addFlashAttribute("signupError", signupError);
            return new RedirectView("signup");
        } else {
            this.userService.addUser(user);
            redirectAttributes.addFlashAttribute("signupSuccess", true);
            // Project Rubric: On a successful signup, the user should be taken to the login page with a message indicating their registration was successful.
            return new RedirectView("login");
        }
    }
}
