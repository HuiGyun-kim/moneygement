package com.room7.moneygement.controller;

import com.room7.moneygement.model.User;
import com.room7.moneygement.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/profile/{username}")
    public String profile(@PathVariable String username, Model model) {
        User user = userService.findByUsername(username);

        if (user != null) {
            model.addAttribute("user", user);
            return "main/profile";
        } else {
            return "redirect:/";
        }
    }
}
