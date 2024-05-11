package com.room7.moneygement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.room7.moneygement.model.UserChallenge;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {

    List<UserChallenge> findByUserId(Long userId);

    boolean existsByUserIdAndJoinDateBetween(Long userId, LocalDateTime startDateTime, LocalDateTime endDateTime);

}

