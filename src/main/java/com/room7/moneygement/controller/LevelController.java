package com.room7.moneygement.controller;

import com.room7.moneygement.serviceImpl.UserChallengeServiceImpl;
import com.room7.moneygement.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.room7.moneygement.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/level")
public class LevelController {

    private final UserServiceImpl userService;
    private final UserChallengeServiceImpl userChallengeService;

    @GetMapping("/exp/{userId}")
    public ResponseEntity<?> getExpAndLevel(@PathVariable Long userId) {
        User user = userService.findById(userId);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("exp", user.getExp());
        response.put("level", user.getCurrentLevel());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/complete/{challengeId}")
    public ResponseEntity<?> completeChallenge(@PathVariable Long challengeId) {
        try {
            userChallengeService.updateExpOnCompleted(challengeId);
            return ResponseEntity.ok().body(Map.of("message", "챌린지는 완료되었으며, 경험치를 50 보내드립니다!"));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "챌린지가 완료되지 않았습니다. 다시 확인해주세요."));
        }
    }
}
