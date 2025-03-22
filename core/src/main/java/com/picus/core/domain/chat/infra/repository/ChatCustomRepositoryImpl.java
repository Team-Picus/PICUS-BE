package com.picus.core.domain.chat.infra.repository;

import com.picus.core.domain.chat.application.dto.response.ChatRoomRes;
import com.picus.core.domain.chat.domain.entity.QChatRoom;
import com.picus.core.domain.chat.domain.entity.participant.QChatUser;
import com.picus.core.domain.user.domain.entity.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatCustomRepositoryImpl implements ChatCustomRepository {

    private final JPAQueryFactory query;

    public List<ChatRoomRes> findMyChatRoomMetadata(Long userNo, Long lastMessageNo) {
        QChatUser currentChatUser = QChatUser.chatUser; // 현재 사용자의 채팅 참여 정보
        QChatUser partnerChatUser = new QChatUser("partnerChatUser"); // 같은 방에 참여한 상대
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QUser partnerUser = QUser.user; // 상대의 User 엔티티

        return query
                .select(Projections.constructor(
                        ChatRoomRes.class,
                        chatRoom.id,                                    // roomNo
                        chatRoom.lastMessageAt,                         // lastMessageAt (필요시 날짜 포맷팅 적용)
                        chatRoom.thumbnailMessage,                      // thumbnailMessage
                        currentChatUser.unreadCnt,                      // unreadMessageCnt (현재 사용자의 안 읽은 메시지 수)
                        partnerUser.id,                                 // partnerId (상대의 사용자 번호)
                        partnerUser.profile.profileImgId,               // profileImageId (상대의 프로필 이미지 아이디)
                        Expressions.constant(""),                 // profileImageUrl
                        partnerUser.profile.nickname                    // nickname (상대의 닉네임)
                ))
                .from(currentChatUser)
                // 현재 사용자의 참여 정보와 ChatRoom 연결
                .join(chatRoom).on(currentChatUser.roomNo.eq(chatRoom.id))
                // 같은 채팅방에서, 현재 사용자가 아닌 다른 참여자(상대)를 partnerChatUser로 구분
                .join(partnerChatUser).on(partnerChatUser.roomNo.eq(chatRoom.id)
                        .and(partnerChatUser.userNo.ne(userNo)))
                // partnerChatUser의 userNo로 상대의 User 정보 조인
                .join(partnerUser).on(partnerChatUser.userNo.eq(partnerUser.id))
                .where(currentChatUser.userNo.eq(userNo))
                .orderBy(chatRoom.lastMessageAt.desc())
                .fetch();
    }
}
