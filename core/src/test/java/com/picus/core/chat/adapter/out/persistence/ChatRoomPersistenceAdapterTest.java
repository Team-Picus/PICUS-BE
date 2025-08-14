package com.picus.core.chat.adapter.out.persistence;


import com.picus.core.chat.adapter.out.persistence.jpa.entity.ChatRoomEntity;
import com.picus.core.chat.adapter.out.persistence.jpa.repository.ChatRoomJpaRepository;
import com.picus.core.chat.adapter.out.persistence.mapper.ChatRoomPersistenceMapper;
import com.picus.core.chat.domain.model.ChatRoom;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Import({
        ChatRoomPersistenceAdapter.class,
        ChatRoomPersistenceMapper.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChatRoomPersistenceAdapterTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private ChatRoomJpaRepository chatRoomJpaRepository;
    @Autowired
    private ChatRoomPersistenceAdapter chatRoomPersistenceAdapter;

    @Test
    @DisplayName("clientNo와 expertNo로 ChatRoom을 조회한다.")
    public void findByClientNoAndExpertNo() throws Exception {
        // given
        String clientNo = "c-123";
        String expertNo = "e-123";
        ChatRoomEntity chatRoomEntity = createChatRoomEntity(clientNo, expertNo, false);
        clearPersistenceContext();

        // when
        Optional<ChatRoom> optionalChatRoom = chatRoomPersistenceAdapter.findByClientNoAndExpertNo(clientNo, expertNo);

        // then
        assertThat(optionalChatRoom).isPresent();
        ChatRoom chatRoom = optionalChatRoom.get();
        assertThat(chatRoom.getChatRoomNo()).isNotNull();
        assertThat(chatRoom.getClientNo()).isEqualTo(chatRoomEntity.getClientNo());
        assertThat(chatRoom.getExpertNo()).isEqualTo(chatRoomEntity.getExpertNo());
        assertThat(chatRoom.getIsPinned()).isEqualTo(chatRoomEntity.getIsPinned());
    }

    private ChatRoomEntity createChatRoomEntity(String clientNo, String expertNo, boolean isPinned) {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .clientNo(clientNo)
                .expertNo(expertNo)
                .isPinned(isPinned)
                .build();
        return chatRoomJpaRepository.save(chatRoomEntity);
    }
    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }

}