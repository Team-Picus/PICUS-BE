package com.picus.core.domain.chat.domain.entity.message;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("systemMessage")
public class SystemMessage extends Message {

    private String content;

    public SystemMessage(Long roomNo, String content) {
        super(roomNo, null, MessageType.SYSTEM);
        this.content = content;
    }
}
