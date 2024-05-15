package com.room7.moneygement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.room7.moneygement.service.CustomUserDetails;

@Controller
public class QnAViewController {

        @GetMapping("/qnachat")
        public String showQnaPage(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
            if (userDetails != null) {
                model.addAttribute("currentUser", userDetails.getUser());
            }
            return "main/qnachat";
        }
    }
