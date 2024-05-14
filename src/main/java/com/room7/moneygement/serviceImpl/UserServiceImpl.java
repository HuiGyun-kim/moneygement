package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.service.S3Upload;
import com.room7.moneygement.repository.FollowRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.UserRepository;
import com.room7.moneygement.service.UserService;
import lombok.RequiredArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final S3Upload s3Uploader;

    // BCryptPasswordEncoder를 PasswordEncoder 인터페이스로 사용
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    // private final PasswordEncoder passwordEncoder;
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User findById(Long id) { // findById 메서드 구현
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> searchUsers(String searchType, String searchKey) {
        if (searchType != null && searchKey != null && !searchKey.isEmpty()) {
            List<User> result = new ArrayList<>();

            if ("searchId".equals(searchType)) {
                Optional<User> user = userRepository.findByUsername(searchKey);
                user.ifPresent(result::add);
            }

            return result;
        } else {
            return userRepository.findAll();
        }
    }
	// 비밀번호 확인

	public boolean checkPassword(User user, String password) {
		return encoder.matches(password, user.getPassword());
	}

	// 비밀번호 변경
	@Transactional
	public boolean changePassword(User user, String currentPassword, String newPassword) {
		if (user != null && encoder.matches(currentPassword, user.getPassword())) {
			// 현재 비밀번호가 일치하는 경우 새 비밀번호로 변경
			user.setPassword(encoder.encode(newPassword));
			userRepository.save(user);
			return true;
		}
		return false;
	}

	// 회원 탈퇴
	@Transactional
	public User deleteUser(User user) {
		// 회원 탈퇴 시 연관된 정보들도 삭제해야함
		// 방명록, 댓글, 팔로우 등에서 해당 user의 정보 삭제후
		userRepository.delete(user);
		return user;
	}

}

