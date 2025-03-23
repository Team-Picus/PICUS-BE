package com.picus.core.domain.reservation.domain.entity;

import com.picus.core.domain.shared.category.entity.Category;
import com.picus.core.global.common.category.entity.CategoryType;
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

    private Category locationCategory;

    @Column(nullable = false)
    private String location;

    public Schedule(LocalDateTime localDateTime, Category category, String location) {
        if (category == null || category.getType() != CategoryType.LOCATION) {
            throw new IllegalArgumentException("category must not be null"); // TODO 예외 처리 명확하게
        }
        this.locationCategory = category;
        this.localDateTime = localDateTime;
        this.location = location;
    }
}
