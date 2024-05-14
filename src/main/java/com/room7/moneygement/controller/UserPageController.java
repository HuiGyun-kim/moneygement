package com.room7.moneygement.controller;

import com.room7.moneygement.dto.PasswordChangeDTO;
import com.room7.moneygement.model.User;
import com.room7.moneygement.service.CustomUserDetails;
import com.room7.moneygement.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserPageController {
    private final UserService userService;

    @PostMapping("/change-pw")
    public ResponseEntity<?> changePassword(HttpServletRequest request, HttpServletResponse response,
                                            @RequestBody PasswordChangeDTO passwordChangeDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findUserById(userDetails.getUserId()); // User 객체 검색

            boolean success = userService.changePassword(user, passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());

            if (success) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                return ResponseEntity.ok(Map.of("success", true, "message", "비밀번호가 성공적으로 변경되었습니다."));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "비밀번호 변경에 실패했습니다. 현재 비밀번호를 확인해주세요."));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "인증 정보를 확인할 수 없습니다."));
    }

    // 회원 탈퇴 api
    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody Map<String, String> credentials,
                                           HttpServletRequest request, HttpServletResponse response) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userService.findUserById(userDetails.getUserId()); // User 객체 검색
            String password = credentials.get("password");

            boolean passwordMatch = userService.checkPassword(user, password);
            if(passwordMatch) {
                new SecurityContextLogoutHandler().logout(request, response, authentication);
                userService.deleteUser(user);
                return ResponseEntity.ok(Map.of("success", true, "message", "계정이 성공적으로 삭제되었습니다."));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("success", false, "message", "비밀번호가 잘못되었습니다."));
            }
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", "인증 정보를 확인할 수 없습니다."));
    }
}
