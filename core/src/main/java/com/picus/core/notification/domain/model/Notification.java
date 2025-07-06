package com.picus.core.notification.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Notification {

    private String notificationNo;
    private String title;
    private String content;
    private NotificationType notificationType;
    private Boolean isRead;
    private String receiverNo;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;

}
