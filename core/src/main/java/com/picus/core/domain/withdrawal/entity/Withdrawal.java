package com.picus.core.domain.withdrawal.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Embeddable
@Access(AccessType.FIELD)
public class Withdrawal {

    private LocalDateTime withdrawalAt;

    public Withdrawal() {
        this.withdrawalAt = LocalDateTime.now();
    }
}

