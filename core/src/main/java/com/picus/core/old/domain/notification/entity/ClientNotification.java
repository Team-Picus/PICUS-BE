package com.picus.core.old.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClientNotification extends Notification {

    @Column(nullable = false)
    private Long clientNo;

    public ClientNotification(String content, Long clientNo) {
        super(content);
        this.clientNo = clientNo;
    }
}
