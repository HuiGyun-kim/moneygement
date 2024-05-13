package com.room7.moneygement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long id;

    @Getter
    @Setter
    @Column(name = "user_id")
    private Long userId;

    @Getter
    @Setter
    @Column(name = "date")
    private LocalDate date;

    @Getter
    @Setter
    @Column(name = "is_attended")
    private boolean attended; //출첵 여부

    public Long getId() {
        return id;
    }
}
