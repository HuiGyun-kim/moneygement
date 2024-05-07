package com.room7.moneygement.repository;

import java.util.List;
import java.util.Optional;
import com.room7.moneygement.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByUserIdAndFollowMemberId(Long userId, Long followMemberId);
    List<Follow> findByFollowMemberId(Long followMemberId);
    List<Follow> findByUserId(Long userId);
    void deleteByUserIdAndFollowMemberId(Long userId, Long followMemberId);
}
