package com.picus.core.domain.chat.domain.entity.message;

import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;

@Getter
@TypeAlias("textMessage")
public class TextMessage extends Message {

    private String content;

    public TextMessage(Long roomNo, Long senderNo, String content) {
        super(roomNo, senderNo, MessageType.TEXT);
        this.content = content;
    }
}
