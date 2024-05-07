package com.room7.moneygement.service;

import com.room7.moneygement.model.User;
import java.util.List;



public interface FollowService {
    void follow(Long followMemberId, String username);
    void unfollow(Long followMemberId, String username);
    List<User> getFollowers(Long userId);
    List<User> getFollowing(Long userId);
}
