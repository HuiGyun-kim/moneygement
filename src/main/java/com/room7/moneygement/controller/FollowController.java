package com.room7.moneygement.controller;


import com.room7.moneygement.model.User;
import com.room7.moneygement.service.FollowService;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class); // 로거 인스턴스 추가

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
    public ResponseEntity<String> unfollow(@PathVariable Long userId, Principal principal) {
        try {
            followService.unfollow(userId, principal.getName());
            return new ResponseEntity<>("Unfollowed successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to unfollow user: " + userId + ", error: " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to unfollow", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @DeleteMapping("/unfollow-me/{followerId}")
    public ResponseEntity<String> unfollowMe(@PathVariable Long followerId, Principal principal) {
        try {
            followService.unfollowMe(followerId, principal.getName());
            return new ResponseEntity<>("Unfollowed successfully", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to unfollow user: " + followerId + ", error: " + e.getMessage(), e);
            return new ResponseEntity<>("Failed to unfollow", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

