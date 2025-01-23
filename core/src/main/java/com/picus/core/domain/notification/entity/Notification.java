package com.picus.core.domain.notification.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {     // 추후 알림톡으로 사용 (프로토타입에서는 미정)

    @Id @Tsid
    @Column(name = "noti_no")
    private Long id;

    private String content;

    private Boolean isRead;

    private LocalDateTime createdAt;

    public Notification(String content) {
        this.content = content;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }
}
