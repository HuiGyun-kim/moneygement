package com.room7.moneygement.controller;

import com.room7.moneygement.dto.CommentDTO;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.CommentService;
import com.room7.moneygement.service.FollowService;
import com.room7.moneygement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final FollowService followService;
    private final CommentService commentService;

    @GetMapping("/profile/{userId}")
    public String profile(@PathVariable Long userId, @RequestParam(defaultValue = "0") int page, Model model) {
        User user = userService.findById(userId);

        if (user != null) {
            model.addAttribute("user", user);

            // 팔로워 수와 팔로잉 수
            int followersCount = followService.getFollowers(user.getUserId()).size();
            int followingCount = followService.getFollowing(user.getUserId()).size();
            model.addAttribute("followersCount", followersCount);
            model.addAttribute("followingCount", followingCount);

            // 방명록 목록 조회 및 모델에 추가
            Page<CommentDTO> comments = commentService.getCommentsByUserId(userId, page, 6);
            model.addAttribute("comments", comments.getContent());
            model.addAttribute("currentPage", comments.getNumber());
            model.addAttribute("totalPages", comments.getTotalPages());

            // 새로운 방명록 작성을 위한 DTO 객체 추가
            model.addAttribute("newComment", new CommentDTO());

            return "main/profile";
        } else {
            return "redirect:/";
        }
    }
    @PostMapping("/profile/{userId}/comments")
    public String createComment(@PathVariable Long userId, @ModelAttribute CommentDTO commentDTO) {
        commentService.createComment(commentDTO);
        return "redirect:/profile/" + userId;
    }

    @PostMapping("/profile/{userId}/comments/{commentId}/update")
    public String updateComment(@PathVariable Long userId, @PathVariable Long commentId, @ModelAttribute CommentDTO commentDTO) {
        commentService.updateComment(commentId, commentDTO);
        return "redirect:/profile/" + userId;
    }


    @PostMapping("/profile/{userId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long userId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/profile/" + userId;
    }

    @GetMapping("/profile/{userId}/comments/{commentId}/edit")
    public String editComment(@PathVariable Long userId, @PathVariable Long commentId, Model model) {
        CommentDTO comment = commentService.getCommentById(commentId);
        model.addAttribute("comment", comment);
        model.addAttribute("userId", userId);
        return "main/comment-edit";
    }
}
