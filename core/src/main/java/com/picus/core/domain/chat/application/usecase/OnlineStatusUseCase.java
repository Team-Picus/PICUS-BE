package com.picus.core.domain.chat.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OnlineStatusUseCase {

    private final RedisTemplate<String, Long> chatRoom2Users;
    private static final String ONLINE_STATUS = "ONLINE_STATUS:";

    public void addChatRoom2Member(Long roomNo, Long userNo) {
        SetOperations<String, Long> ops = chatRoom2Users.opsForSet();
//        chatRoom2Users.opsForValue().set(convertToKey(roomNo), userNo); // todo 수정

        ops.add(convertToKey(roomNo), userNo);
    }

    public Set<Long> getOnlineMembers(Long roomNo) {
        SetOperations<String, Long> ops = chatRoom2Users.opsForSet();
        return ops.members(convertToKey(roomNo));
    }

    public int getOnlineMemberCntInChatRoom(Long roomNo) {
        SetOperations<String, Long> ops = chatRoom2Users.opsForSet();
        String s = convertToKey(roomNo);
        Set<Long> members = ops.members(s);
        return (ops.members(convertToKey(roomNo))).size();
    }

    public void removeChatRoom2Member(Long roomNo, Long userNo) {
        SetOperations<String, Long> ops = chatRoom2Users.opsForSet();
        ops.remove(convertToKey(roomNo), userNo);
    }
    
    private String convertToKey(Long roomNo) {
        return ONLINE_STATUS + roomNo;
    }
}
