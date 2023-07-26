package hac.controllers;

import hac.model.User;
import hac.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserRegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserRegistrationDto registrationDto, BindingResult result) {
        if (result.hasErrors()) {
            return "register";
        }

        User user = userService.registerUser(registrationDto, result);
        if (user == null) {
            result.reject(null, "Failed to register user");
            return "register";
        }

        return "redirect:/users/register?success";
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model, Principal principal) {
        String username = principal.getName(); // Retrieve the username
        User user = userService.findUserByUsername(username); // Assuming you have a repository for User objects
        model.addAttribute("user", user); // Add the user object to the model

        return "users/profile";
    }

    @PostMapping("/profile")
    public String updateUserProfile(@Valid @ModelAttribute("user") User updatedUser, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/profile";
        }
        userService.saveUserProfile(updatedUser);
        model.addAttribute("success", "Profile updated successfully!");
        return "redirect:/users/profile?success";
    }

}
