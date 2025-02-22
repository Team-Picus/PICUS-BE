package com.picus.core.global.common.category.entity;

import com.picus.core.domain.post.application.usecase.TodayAvailableCategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TodayAvailableCategoryServiceTest {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private org.springframework.data.redis.core.ValueOperations<String, String> valueOperations;

    @InjectMocks
    private TodayAvailableCategoryService service;

    @Test
    void activate_O() {
        // given
        List<Long> postIds = Arrays.asList(1L, 2L, 3L);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // when
        service.activate(postIds);

        // then
        ArgumentCaptor<java.util.Map<String, String>> captor = ArgumentCaptor.forClass(java.util.Map.class);
        verify(valueOperations, times(1)).multiSet(captor.capture());
        java.util.Map<String, String> resultMap = captor.getValue();

        // 검증: map 크기, key 접두사 및 값이 빈 문자열인지 확인
        assertNotNull(resultMap);
        assert(resultMap.size() == 3);
        postIds.forEach(postId -> {
            String key = "TODAY_AVAILABLE:" + postId;
            assert(resultMap.containsKey(key));
            assert(resultMap.get(key).equals(""));
        });
    }

    @Test
    void deactivate_O() {
        // given
        Long postId = 2L;
        String expectedKey = "TODAY_AVAILABLE:" + postId;

        // when
        service.deactivate(postId);

        // then
        verify(redisTemplate, times(1)).delete(expectedKey);
    }

    @Test
    void activate_withDuration_O() {
        // given
        List<Long> postIds = Arrays.asList(1L, 2L, 3L);
        Duration duration = Duration.ofSeconds(60);

        // String serializer used by redisTemplate
        StringRedisSerializer serializer = new StringRedisSerializer();
        when(redisTemplate.getStringSerializer()).thenReturn(serializer);

        // when
        service.activate(postIds, duration);

        // then
        // 캡처: executePipelined에 전달된 RedisCallback
        ArgumentCaptor<RedisCallback<Object>> callbackCaptor = ArgumentCaptor.forClass(RedisCallback.class);
        verify(redisTemplate).executePipelined(callbackCaptor.capture());
        RedisCallback<Object> callback = callbackCaptor.getValue();
        assertNotNull(callback);

        // 모의 RedisConnection 생성
        RedisConnection redisConnection = mock(RedisConnection.class);

        // 콜백 실행 (파이프라인 내의 각 명령이 실행됨)
        callback.doInRedis(redisConnection);

        // 각 postId마다 setEx가 올바른 파라미터로 호출되었는지 검증
        for (Long postId : postIds) {
            String keyStr = "TODAY_AVAILABLE:" + postId;
            byte[] keyBytes = serializer.serialize(keyStr);
            byte[] valueBytes = serializer.serialize("");
            verify(redisConnection).setEx(eq(keyBytes), eq(duration.getSeconds()), aryEq(valueBytes));
        }
        // 총 postIds.size()번 호출되었는지 확인
        verify(redisConnection, times(postIds.size())).setEx(any(), anyLong(), any());
    }
}
