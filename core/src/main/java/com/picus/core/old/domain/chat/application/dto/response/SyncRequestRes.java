package com.picus.core.old.domain.chat.application.dto.response;

import com.picus.core.old.domain.chat.domain.entity.message.MessageType;

public class SyncRequestRes extends MessageRes {

    static private final MessageType messageType = MessageType.SYNC_REQUEST;

    private SyncRequestRes() {
        super(null, messageType);
    }

    public static MessageRes createRes() {
        return new SyncRequestRes();
    }
}
