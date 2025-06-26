package com.picus.core.old.domain.notification.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpertNotification extends Notification {

    @Column(nullable = false)
    public Long expertNo;

    public ExpertNotification(String content, Long expertNo) {
        super(content);
        this.expertNo = expertNo;
    }
}
