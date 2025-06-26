package com.picus.core.old.domain.chat.domain.entity.message;

import com.picus.core.old.domain.shared.image.domain.entity.Image;
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
