package com.room7.moneygement.dto;

import com.room7.moneygement.model.Follow;

import lombok.Data;

@Data
public class FollowDTO {
    private Long followId;
    private Long followerId; // 팔로워 ID
    private Long followingId; // 팔로잉 ID

    public FollowDTO(Follow follow) {
        this.followId = follow.getFollowId();
        this.followerId = follow.getFollower().getUserId(); // 팔로워의 유저 ID 참조
        this.followingId = follow.getFollowing().getUserId(); // 팔로잉의 유저 ID 참조
    }
}