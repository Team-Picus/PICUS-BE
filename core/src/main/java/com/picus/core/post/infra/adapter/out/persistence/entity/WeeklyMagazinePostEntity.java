package com.picus.core.post.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "weekly_magazine_posts")
public class WeeklyMagazinePostEntity {

    @Id @Tsid
    private String weeklyMagazinePostNo;


}
