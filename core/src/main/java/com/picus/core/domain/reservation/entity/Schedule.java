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
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime time;

    @Column(nullable = false)
    private String location;

    public Schedule(LocalDate date, LocalTime time, String location) {
        this.date = date;
        this.time = time;
        this.location = location;
    }
}
