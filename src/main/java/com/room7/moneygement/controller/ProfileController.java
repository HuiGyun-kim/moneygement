package com.room7.moneygement.controller;

import com.room7.moneygement.model.User;
import com.room7.moneygement.service.FollowService;
import com.room7.moneygement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final FollowService followService;

    @GetMapping("/profile/{userId}")
    public String profile(@PathVariable Long userId, Model model) {
        User user = userService.findById(userId);

        if (user != null) {
            model.addAttribute("user", user);
            // 팔로워 수와 팔로잉 수 가져오기
            int followersCount = followService.getFollowers(user.getUserId()).size();
            int followingCount = followService.getFollowing(user.getUserId()).size();

            // 모델에 추가
            model.addAttribute("followersCount", followersCount);
            model.addAttribute("followingCount", followingCount);

            return "main/profile";
        } else {
            return "redirect:/";
        }
    }
}
