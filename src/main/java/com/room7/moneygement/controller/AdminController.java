package com.room7.moneygement.controller;

import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.UserRepository;
import com.room7.moneygement.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;

    @GetMapping("/search")
    public String searchId(@RequestParam String searchType, @RequestParam String searchKey, Model model) {
        List<User> searchResult = userService.searchUsers(searchType, searchKey);

        if (searchResult.isEmpty()) {
            model.addAttribute("message", "존재하지 않는 회원입니다.");
            List<User> userList = userRepository.findAll();
            model.addAttribute("userList", userList);
            model.addAttribute("total", userRepository.countAllUsers());
            return "main/admin";
        }
        else {
            model.addAttribute("userList", searchResult);
            model.addAttribute("total", userRepository.countAllUsers());
            return "main/admin";
        }
    }
    @GetMapping("/refresh")
    public String refresh(Model model) {
        List<User> userList = userRepository.findAll();
        model.addAttribute("userList", userList);
        model.addAttribute("total", userRepository.countAllUsers());
        return "main/admin";
    }
}
