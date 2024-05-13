package com.room7.moneygement.serviceImpl;
import com.room7.moneygement.repository.FollowRepository;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.room7.moneygement.model.Ledger;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.LedgerRepository;
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

	// public boolean checkPassword(String rawPassword, String encodedPassword) {
	// 	return passwordEncoder.matches(rawPassword, encodedPassword);
	// }
	@Override
	public User findById(Long id) { // findById 메서드 구현
		return userRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("User not found"));
	}

	public List<User> searchUsers(String searchType, String searchKey){
		if (searchType != null && searchKey != null && !searchKey.isEmpty()) {
			List<User> result = new ArrayList<>();

			if ("searchId".equals(searchType)) {
				Optional<User> user = userRepository.findByUsername(searchKey);
				user.ifPresent(result::add);
			}

			return result;
		}
		else {
			return userRepository.findAll();
		}
	}
}
