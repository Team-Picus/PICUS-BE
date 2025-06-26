package com.picus.core.old.domain.user.domain.entity.withdrawal;

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

