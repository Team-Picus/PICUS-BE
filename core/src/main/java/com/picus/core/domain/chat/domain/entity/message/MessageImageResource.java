package com.picus.core.domain.chat.domain.entity.message;

import com.picus.core.domain.shared.image.domain.entity.Image;
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
