package com.picus.core.notification.infra.adapter.out.persistence.entity;

import com.picus.core.shared.common.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
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

    private String title;

    private String content;

    private NotificationType notificationType;

    private Boolean isRead;

    private String receiverNo;
}
