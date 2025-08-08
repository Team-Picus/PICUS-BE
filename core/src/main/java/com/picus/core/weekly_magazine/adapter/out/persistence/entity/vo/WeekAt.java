package com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo;

import jakarta.persistence.Embeddable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.*;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class WeekAt {

    private int year;

    private int month;

    private int week;

    @Builder
    private WeekAt(int year, int month, int week) {
        this.year = year;
        this.month = month;
        this.week = week;
        validate();
    }

    @PrePersist
    @PreUpdate
    private void validate() {
        if (year < 2000 || year > 2100) {
            throw new IllegalStateException("year는 2000~2100 사이여야 합니다.");
        }

        if (month < 1 || month > 12) {
            throw new IllegalStateException("month는 1~12 사이여야 합니다.");
        }

        if (week < 1 || week > 6) {
            throw new IllegalStateException("week는 1~6 사이여야 합니다.");
        }
    }
}
