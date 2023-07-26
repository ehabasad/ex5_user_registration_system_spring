package hac.controllers;

import hac.model.User;
import hac.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;


@Controller
public class PasswordResetController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/password-reset")
    public String showPasswordResetForm(Model model) {
        model.addAttribute("username", ""); // Initialize with an empty string
        return "password-reset";
    }

    @GetMapping("/new-password")
    public String showNewPasswordForm(Model model) {
        model.addAttribute("error", "");
        return "new-password";
    }

    @PostMapping("/password-reset")
    public String resetPassword(@RequestParam("username") String username,
                                @RequestParam("email") String email,
                                HttpSession session,
                                Model model) {
        // Check if the username and email exist in the database
        User user = userService.findByUsernameAndEmail(username, email);
        if (user != null) {
            // Username and email match, save the username in the session
            session.setAttribute("username", username);
            return "redirect:/new-password";
        } else {
            // Username and email do not match, show an error message
            model.addAttribute("error", "Invalid username or email");
            return "redirect:/password-reset?error";
        }
    }

    @PostMapping("/new-password")
    public String setNewPassword(@RequestParam("password") String password,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 HttpSession session,
                                 Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Password and Confirm Password do not match");
            return "redirect:/new-password?error";
        }

        // Retrieve the username from the session
        String username = (String) session.getAttribute("username");
        if (username != null) {
            // Retrieve the user from the database by the username
            User user = userService.findUserByUsername(username);
            if (user != null) {
                // Update the user's password
                String encodedPassword = passwordEncoder.encode(password);
                user.setPassword(encodedPassword);
                userService.updateUserProfile(user);
                userService.changePassword(username,password);
                model.addAttribute("success", "You've successfully reset your password!");
                return "redirect:/new-password?success";
            } else {
                model.addAttribute("error", "User not found");
            }
        } else {
            model.addAttribute("error", "Invalid session");
        }

        return "redirect:/new-password?error";
    }

}
