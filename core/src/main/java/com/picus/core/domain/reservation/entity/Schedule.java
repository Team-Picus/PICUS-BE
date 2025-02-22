package com.picus.core.domain.reservation.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Embeddable
@NoArgsConstructor
public class Schedule {

    @Column(nullable = false)
    private LocalDateTime localDateTime;

    @Column(nullable = false)
    private String location;

    public Schedule(LocalDateTime localDateTime, String location) {
        this.localDateTime = localDateTime;
        this.location = location;
    }
}
