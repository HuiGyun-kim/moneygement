package com.room7.moneygement.controller;

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

}
