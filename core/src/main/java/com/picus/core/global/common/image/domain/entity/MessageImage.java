package com.picus.core.global.common.image.domain.entity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageImage extends Image {

    public MessageImage(String preSignedKey) {
        super(preSignedKey);
    }
}
