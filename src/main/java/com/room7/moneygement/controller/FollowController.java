package com.room7.moneygement.controller;


import com.room7.moneygement.model.User;
import com.room7.moneygement.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;


@Controller
@AllArgsConstructor
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    @PostMapping("/{userId}")
    public String follow(@PathVariable Long userId, Principal principal) {
        try {
            followService.follow(userId, principal.getName());
            return "redirect:/profile/" + userId;
        } catch (Exception e) {
            return "redirect:/profile/" + userId;
        }
    }

    @DeleteMapping("/unfollow/{userId}")
    public String unfollow(@PathVariable Long userId, Principal principal) {
        try {
            followService.unfollow(userId, principal.getName());
            return "redirect:/following/" + userId;
        } catch (Exception e) {
            return "redirect:/following/" + userId;
        }
    }

    @GetMapping("/followers/{userId}")
    public String getFollowers(@PathVariable Long userId, Model model) {
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

