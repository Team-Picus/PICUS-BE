package com.picus.core.domain.chat.domain.service;

import com.picus.core.domain.chat.application.dto.request.SendMsgReq;
import com.picus.core.domain.chat.domain.entity.message.*;
import com.picus.core.domain.chat.domain.repository.MessageRepository;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._METHOD_ARGUMENT_ERROR;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void save(Message message) {
        switch (message) {
            case TextMessage tm -> messageRepository.save(tm);
            case ImageMessage im -> messageRepository.save(im);
            case ReservationMessage rm -> messageRepository.save(rm);
            case null, default -> throw new RestApiException(_METHOD_ARGUMENT_ERROR);
        }
    }

}
