package com.picus.core.domain.chat.domain.repository;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

//    @Query("SELECT new com.picus.core.domain.chat.application.dto.response.ChatRoomRes(" +
//            "c.id, c.clientNo, c.expertNo, " +
//            "CONCAT('', " +
//            "  CASE WHEN function('DATE', c.lastMessageAt) = CURRENT_DATE " +
//            "       THEN function('DATE_FORMAT', c.lastMessageAt, '%p %h:%i') " +
//            "       ELSE function('DATE_FORMAT', c.lastMessageAt, '%m:%d') " +
//            "  END" +
//            "), " +
//            "c.thumbnailMessage) " +
//            "FROM ChatRoom c " +
//            "WHERE c.clientNo = :userNo OR c.expertNo = :userNo " +
//            "ORDER BY c.lastMessageAt DESC")
//    Page<ChatRoomRes> findChatRooms(@Param("userNo") Long userNo, Pageable pageable);
}
