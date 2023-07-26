package hac.controllers;

import hac.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin-dashboard")
    public String showAllUsers(Model model) {
        model.addAttribute("users", adminService.getAllUsers());
        return "admin/admin-dashboard";
    }

    @PostMapping("/enable/{username}")
    public String enableUserAccount(@PathVariable("username") String username) {
        adminService.enableUserAccount(username);
        return "redirect:/admin/admin-dashboard";
    }

    @PostMapping("/disable/{username}")
    public String disableUserAccount(@PathVariable("username") String username) {
        adminService.disableUserAccount(username);
        return "redirect:/admin/admin-dashboard";
    }

    @PostMapping("/delete/{username}")
    public String deleteUserAccount(@PathVariable("username") String username) {
        adminService.deleteUserAccount(username);
        return "redirect:/admin/admin-dashboard";
    }
}
