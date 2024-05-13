package com.room7.moneygement.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import com.room7.moneygement.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	boolean existsByUsername(String userId);
	Optional<User> findByEmail(String email);
	@Query("SELECT COUNT(user) FROM User user")
	Long countAllUsers();
}

