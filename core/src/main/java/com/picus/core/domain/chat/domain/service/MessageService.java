package com.picus.core.domain.chat.domain.service;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.domain.entity.message.ImageMessage;
import com.picus.core.domain.chat.domain.entity.message.Message;
import com.picus.core.domain.chat.domain.entity.message.ReservationMessage;
import com.picus.core.domain.chat.domain.entity.message.TextMessage;
import com.picus.core.domain.chat.domain.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void save(Message message) {
        switch (message) {
            case TextMessage tm -> messageRepository.save(tm);
            case ImageMessage im -> messageRepository.save(im);
            case ReservationMessage rm -> messageRepository.save(rm);
            case null, default -> throw new IllegalArgumentException("지원하지 않는 메시지 타입: " + message.getClass());
        }
    }

}
