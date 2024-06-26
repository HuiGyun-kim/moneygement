package com.room7.moneygement.repository;

import java.util.List;
import java.util.Optional;
import com.room7.moneygement.model.Follow;
import com.room7.moneygement.model.User;

import org.springframework.data.jpa.repository.JpaRepository;


public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByFollowerAndFollowing(User follower, User following);
    List<Follow> findByFollowing(User following);
    List<Follow> findByFollower(User follower);
}
