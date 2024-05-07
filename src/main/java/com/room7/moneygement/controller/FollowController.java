package com.room7.moneygement.controller;


import com.room7.moneygement.model.User;
import com.room7.moneygement.service.FollowService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


@Controller
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follow/{userId}")
    public String follow(@PathVariable Long userId, Principal principal) {
        try {
            followService.follow(userId, principal.getName());
            return "redirect:/profile/" + userId;
        } catch (Exception e) {
            return "redirect:/profile/" + userId;
        }
    }

    @PostMapping("/unfollow/{userId}")
    public String unfollow(@PathVariable Long userId, Principal principal) {
        try {
            followService.unfollow(userId, principal.getName());
            return "redirect:/profile/" + userId;
        } catch (Exception e) {
            return "redirect:/profile/" + userId;
        }
    }

    @GetMapping("/followers/{userId}")
    public String getFollowers(@PathVariable Long userId, @PageableDefault(size = 10, page = 0) Pageable pageable, Model model) {
        List<User> followers = followService.getFollowers(userId);
        model.addAttribute("followers", followers);
        return "/myPage/Follower";
    }

    @GetMapping("/following/{userId}")
    public String getFollowing(@PathVariable Long userId, Model model) {
        List<User> following = followService.getFollowing(userId);
        model.addAttribute("following", following);
        return "/myPage/Following";
    }
}

