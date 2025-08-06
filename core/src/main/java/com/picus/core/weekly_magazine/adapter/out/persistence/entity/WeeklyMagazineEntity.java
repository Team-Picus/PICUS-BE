package com.picus.core.weekly_magazine.adapter.out.persistence.entity;

import com.picus.core.weekly_magazine.adapter.out.persistence.entity.vo.WeekAt;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weekly_magazines")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class WeeklyMagazineEntity {

    @Id @Tsid
    private String weeklyMagazineNo;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String topicDescription;

    @Embedded
    private WeekAt weekAt;

    @Column(nullable = false)
    private String thumbnailKey;
}
