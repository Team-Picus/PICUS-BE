package com.picus.core.weekly_magazine.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weekly_magazines")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyMagazineEntity {

    @Id @Tsid
    private String weeklyMagazineNo;

    @Column(nullable = false)
    private String topic;
    @Column(nullable = false)
    private String topicDescription;
    @Column(nullable = false)
    private String weekAt;
    @Column(nullable = false)
    private String thumbnailKey;
}
