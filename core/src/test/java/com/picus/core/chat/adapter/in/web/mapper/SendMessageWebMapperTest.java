package com.picus.core.chat.adapter.in.web.mapper;

import com.picus.core.chat.adapter.in.web.data.request.*;
import com.picus.core.chat.adapter.in.web.data.response.*;
import com.picus.core.chat.application.port.in.command.CreateMessageCommand;
import com.picus.core.chat.application.port.in.result.*;
import com.picus.core.chat.domain.model.vo.FileContent;
import com.picus.core.chat.domain.model.vo.ImageContent;
import com.picus.core.chat.domain.model.vo.ReservationContent;
import com.picus.core.chat.domain.model.vo.TextContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SendMessageWebMapperTest {

    private final SendMessageWebMapper webMapper = new SendMessageWebMapper();

    @Test
    @DisplayName("textMessageRequest -> textMessageCommand")
    public void toCommand_textMessage() throws Exception {
        // given
        SendTextMessageRequest request = SendTextMessageRequest.builder()
                .text("text")
                .build();
        String roomId = "cr-123";
        String userNo = "u-123";

        // when
        CreateMessageCommand command = webMapper.toCommand(request, roomId, userNo);

        // then
        assertThat(command.chatRoomNo()).isEqualTo(roomId);
        assertThat(command.senderNo()).isEqualTo(userNo);
        assertThat(command.content()).isInstanceOf(TextContent.class);
        assertThat(((TextContent) command.content()).text()).isEqualTo("text");
    }

    @Test
    @DisplayName("imageMessageRequest -> imageMessageCommand")
    public void toCommand_imageMessage() throws Exception {
        // given
        SendImageMessageRequest request = SendImageMessageRequest.builder()
                .imageKey("a.png")
                .build();
        String roomId = "cr-123";
        String userNo = "u-123";

        // when
        CreateMessageCommand command = webMapper.toCommand(request, roomId, userNo);

        // then
        assertThat(command.chatRoomNo()).isEqualTo(roomId);
        assertThat(command.senderNo()).isEqualTo(userNo);
        assertThat(command.content()).isInstanceOf(ImageContent.class);
        assertThat(((ImageContent) command.content()).imageKey()).isEqualTo("a.png");
    }

    @Test
    @DisplayName("fileMessageRequest -> fileMessageCommand")
    public void toCommand_fileMessage() throws Exception {
        // given
        SendFileMessageRequest request = SendFileMessageRequest.builder()
                .fileKey("a.pdf")
                .build();
        String roomId = "cr-123";
        String userNo = "u-123";

        // when
        CreateMessageCommand command = webMapper.toCommand(request, roomId, userNo);

        // then
        assertThat(command.chatRoomNo()).isEqualTo(roomId);
        assertThat(command.senderNo()).isEqualTo(userNo);
        assertThat(command.content()).isInstanceOf(FileContent.class);
        assertThat(((FileContent) command.content()).fileKey()).isEqualTo("a.pdf");
    }

    @Test
    @DisplayName("reservationMessageRequest -> reservationMessageCommand")
    public void toCommand_reservationMessage() throws Exception {
        // given
        SendReservationMessageRequest request = SendReservationMessageRequest.builder()
                .build();
        String roomId = "cr-123";
        String userNo = "u-123";

        // when
        CreateMessageCommand command = webMapper.toCommand(request, roomId, userNo);

        // then
        assertThat(command.chatRoomNo()).isEqualTo(roomId);
        assertThat(command.senderNo()).isEqualTo(userNo);
        assertThat(command.content()).isInstanceOf(ReservationContent.class);
    }
    
    @Test
    @DisplayName("textMessageResult -> textMessageResponse")
    public void toResponse_text() throws Exception {
        // given
        CreateTextMessageResult result = CreateTextMessageResult.builder()
                .senderNo("u-123")
                .text("text")
                .build();

        // when
        CreateMessageResponse response = webMapper.toResponse(result);

        // then
        assertThat(response).isInstanceOf(CreateTextMessageResponse.class);
        CreateTextMessageResponse textResponse = (CreateTextMessageResponse) response;
        assertThat(textResponse.senderNo()).isEqualTo(result.senderNo());
        assertThat(textResponse.text()).isEqualTo(result.text());
    }

    @Test
    @DisplayName("imageMessageResult -> imageMessageResponse")
    public void toResponse_image() throws Exception {
        // given
        CreateImageMessageResult result = CreateImageMessageResult.builder()
                .senderNo("u-123")
                .imageUrl("a.com")
                .build();

        // when
        CreateMessageResponse response = webMapper.toResponse(result);

        // then
        assertThat(response).isInstanceOf(CreateImageMessageResponse.class);
        CreateImageMessageResponse textResponse = (CreateImageMessageResponse) response;
        assertThat(textResponse.senderNo()).isEqualTo(result.senderNo());
        assertThat(textResponse.imageUrl()).isEqualTo(result.imageUrl());
    }

    @Test
    @DisplayName("fileMessageResult -> fileMessageResponse")
    public void toResponse_file() throws Exception {
        // given
        CreateFileMessageResult result = CreateFileMessageResult.builder()
                .senderNo("u-123")
                .fileUrl("a.com")
                .build();

        // when
        CreateMessageResponse response = webMapper.toResponse(result);

        // then
        assertThat(response).isInstanceOf(CreateFileMessageResponse.class);
        CreateFileMessageResponse textResponse = (CreateFileMessageResponse) response;
        assertThat(textResponse.senderNo()).isEqualTo(result.senderNo());
        assertThat(textResponse.fileUrl()).isEqualTo(result.fileUrl());
    }

    @Test
    @DisplayName("reservationMessageResult -> reservationMessageResponse")
    public void toResponse_reservation() throws Exception {
        // given
        CreateReservationMessageResult result = CreateReservationMessageResult.builder()
                .senderNo("u-123")
                .build();

        // when
        CreateMessageResponse response = webMapper.toResponse(result);

        // then
        assertThat(response).isInstanceOf(CreateReservationMessageResponse.class);
        CreateReservationMessageResponse textResponse = (CreateReservationMessageResponse) response;
        assertThat(textResponse.senderNo()).isEqualTo(result.senderNo());
    }
}