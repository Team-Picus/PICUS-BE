package com.picus.core.weekly_magazine.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weekly_magazine_posts")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyMagazinePostEntity {

    @Id @Tsid
    private String weeklyMagazinePostNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_magazine_no", nullable = false)
    private WeeklyMagazineEntity weeklyMagazineEntity;

    @Column(nullable = false)
    private String postNo;

}
