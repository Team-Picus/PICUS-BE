package com.picus.core.global.common.image.domain.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageImageResource extends Image {

    public MessageImageResource(String preSignedKey) {
        super(preSignedKey);
    }
}
