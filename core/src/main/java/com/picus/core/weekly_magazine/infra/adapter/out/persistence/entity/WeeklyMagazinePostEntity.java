package com.picus.core.weekly_magazine.infra.adapter.out.persistence.entity;

import com.picus.core.post.infra.adapter.out.persistence.entity.PostEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "weekly_magazine_posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WeeklyMagazinePostEntity {

    @Id @Tsid
    private String weeklyMagazinePostNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_magazine_no", nullable = false)
    private WeeklyMagazineEntity weeklyMagazineEntity;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weekly_magazine_post_no", nullable = false)
    private PostEntity postEntity;

}
