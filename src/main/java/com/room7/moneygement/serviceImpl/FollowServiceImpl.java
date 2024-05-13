package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.model.Follow;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.FollowRepository;
import com.room7.moneygement.service.FollowService;
import com.room7.moneygement.service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    @Override
    public void follow(Long followingId, String username) {
        User follower = userService.findByUsername(username);
        User following = userService.findById(followingId);

        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowing(following);
        followRepository.save(follow);
    }


    @Override
    public void unfollow(Long followingId, String username) {
        User follower = userService.findByUsername(username);
        User following = userService.findById(followingId);

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
            .orElse(null);

        if (follow != null) {
            followRepository.delete(follow);
        }
    }


    @Override
    public List<User> getFollowers(Long userId) {
        User user = userService.findById(userId);
        List<Follow> follows = followRepository.findByFollowing(user);
        return follows.stream()
            .map(Follow::getFollower)
            .collect(Collectors.toList());
    }


    @Override
    public List<User> getFollowing(Long userId) {
        User user = userService.findById(userId);
        List<Follow> follows = followRepository.findByFollower(user);
        return follows.stream()
            .map(Follow::getFollowing)
            .collect(Collectors.toList());
    }
    @Override
    public void unfollowMe(Long followerId, String username) {
        User follower = userService.findById(followerId);
        User following = userService.findByUsername(username);

        Follow follow = followRepository.findByFollowerAndFollowing(follower, following)
            .orElse(null);

        if (follow != null) {
            followRepository.delete(follow);
        }
    }
}