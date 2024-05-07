package com.room7.moneygement.serviceImpl;

import com.room7.moneygement.model.Follow;
import com.room7.moneygement.model.User;
import com.room7.moneygement.repository.FollowRepository;
import com.room7.moneygement.service.FollowService;
import com.room7.moneygement.service.UserService;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserService userService;

    public FollowServiceImpl(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @Override
    public void follow(Long followMemberId, String username) {
        User follower = userService.findByUsername(username);
        User followMember = userService.findById(followMemberId);

        Follow follow = new Follow();
        follow.setUserId(follower.getUserId());
        follow.setFollowMemberId(followMember.getUserId());
        followRepository.save(follow);

        follower.getFollowings().add(follow);
        followMember.getFollowers().add(follow);
    }

    @Override
    public void unfollow(Long followMemberId, String username) {
        User follower = userService.findByUsername(username);
        User followMember = userService.findById(followMemberId);

        Follow follow = followRepository.findByUserIdAndFollowMemberId(follower.getUserId(), followMember.getUserId())
                .orElseThrow(() -> new RuntimeException("Follow not found"));

        followRepository.delete(follow);

        follower.getFollowings().remove(follow);
        followMember.getFollowers().remove(follow);
    }

    @Override
    public List<User> getFollowers(Long userId) {
        List<Follow> follows = followRepository.findByFollowMemberId(userId);
        return follows.stream()
                .map(f -> userService.findById(f.getUserId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getFollowing(Long userId) {
        List<Follow> follows = followRepository.findByUserId(userId);
        return follows.stream()
                .map(f -> userService.findById(f.getFollowMemberId()))
                .collect(Collectors.toList());
    }
}