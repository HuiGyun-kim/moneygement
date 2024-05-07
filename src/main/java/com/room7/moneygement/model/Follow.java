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

    @Column(name = "follow_member_id")
    private Long followMemberId;

    @Column(name = "user_id")
    private Long userId;
}
