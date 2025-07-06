package com.picus.core.post.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weekly_magazines")
public class WeeklyMagazineEntity {

    @Id
    @Tsid
    private String weeklyMagazineNo;

    private String topic;
    private String topicDescription;
    private String weekAt;
    private String thumbnailKey;
}
