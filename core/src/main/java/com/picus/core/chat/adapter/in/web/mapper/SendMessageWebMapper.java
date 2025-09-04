package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.request.*;
import com.picus.core.chat.adapter.in.web.data.response.*;
import com.picus.core.chat.application.port.in.command.*;
import com.picus.core.chat.application.port.in.result.*;
import com.picus.core.chat.domain.model.vo.*;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import org.springframework.stereotype.Component;

@Component
public class SendMessageWebMapper {

    public CreateMessageCommand toCommand(SendMessageRequest request, String chatRoomNo, String userNo) {
        switch (request) {
            case SendTextMessageRequest textMessage -> {
                TextContent textContent = TextContent.builder()
                        .text(textMessage.text())
                        .build();

                return CreateTextMessageCommand.builder()
                        .chatRoomNo(chatRoomNo)
                        .senderNo(userNo)
                        .textContent(textContent)
                        .build();
            }
            case SendImageMessageRequest imageMessage -> {
                ImageContent imageContent = ImageContent.builder()
                        .imageKey(imageMessage.imageKey())
                        .build();

                return CreateImageMessageCommand.builder()
                        .chatRoomNo(chatRoomNo)
                        .senderNo(userNo)
                        .imageContent(imageContent)
                        .build();
            }
            case SendFileMessageRequest fileMessage -> {
                FileContent fileContent = FileContent.builder()
                        .fileKey(fileMessage.fileKey())
                        .build();

                return CreateFileMessageCommand.builder()
                        .chatRoomNo(chatRoomNo)
                        .senderNo(userNo)
                        .fileContent(fileContent)
                        .build();
            }
            case SendReservationMessageRequest reservationMessage -> {
                ReservationContent reservationContent = ReservationContent.builder()
                        .build();

                return CreateReservationMessageCommand.builder()
                        .chatRoomNo(chatRoomNo)
                        .senderNo(userNo)
                        .reservationContent(reservationContent)
                        .build();
            }
            default -> throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }
    }

    public CreateMessageResponse toResponse(CreateMessageResult result) {
        switch (result) {
            case CreateTextMessageResult textMessage -> {

                return CreateTextMessageResponse.builder()
                        .senderNo(textMessage.senderNo())
                        .text(textMessage.text())
                        .build();
            }
            case CreateImageMessageResult imageMessage -> {

                return CreateImageMessageResponse.builder()
                        .senderNo(imageMessage.senderNo())
                        .imageUrl(imageMessage.imageUrl())
                        .build();
            }
            case CreateFileMessageResult fileMessage -> {

                return CreateFileMessageResponse.builder()
                        .senderNo(fileMessage.senderNo())
                        .fileUrl(fileMessage.fileUrl())
                        .build();
            }
            case CreateReservationMessageResult reservationMessage -> {

                return CreateReservationMessageResponse.builder()
                        .senderNo(reservationMessage.senderNo())
                        .build();
            }
            default -> throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }
    }
}
