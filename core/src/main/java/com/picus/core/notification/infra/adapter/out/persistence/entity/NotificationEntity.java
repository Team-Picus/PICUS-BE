package com.picus.core.notification.infra.adapter.out.persistence.entity;

import com.picus.core.notification.domain.model.NotificationType;
import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NotificationEntity extends BaseEntity {

    @Id @Tsid
    private String notificationNo;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private Boolean isRead;

    @Column(nullable = false)
    private String receiverNo;
}
