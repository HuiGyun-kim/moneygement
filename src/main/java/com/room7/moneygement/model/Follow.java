package com.room7.moneygement.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "follow")
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    @ManyToOne
    @JoinColumn(name = "follower_id", referencedColumnName = "user_id")
    private User follower; // 팔로워

    @ManyToOne
    @JoinColumn(name = "following_id", referencedColumnName = "user_id")
    private User following; // 팔로잉
}
