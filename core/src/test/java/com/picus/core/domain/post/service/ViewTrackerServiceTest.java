package com.picus.core.domain.post.service;

import com.picus.core.domain.post.entity.view.ViewCount;
import com.picus.core.domain.post.entity.view.ViewHistory;
import com.picus.core.domain.post.exception.PostNotFoundException;
import com.picus.core.domain.post.repository.view.primary.ViewCountRepository;
import com.picus.core.domain.post.repository.view.primary.ViewHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ViewTrackerServiceTest {

    @Mock
    private ViewCountRepository viewCountRepository;

    @Mock
    private ViewHistoryRepository viewHistoryRepository;

    @InjectMocks
    private ViewTrackerService viewTrackerService;

    private final Long userId = 1L;
    private final Long postId = 100L;
    private final String viewHistoryKey = ViewHistory.format(userId, postId);
    private final String viewCountKey = ViewCount.generateKey(postId);

    @Test
    public void updateViewCount_조회_이력_X() {
        when(viewHistoryRepository.existsById(viewHistoryKey)).thenReturn(false);

        viewTrackerService.updateViewCount(userId, postId);

        verify(viewCountRepository, times(1)).increment(viewCountKey);
        verify(viewHistoryRepository, times(1)).save(any(ViewHistory.class));
    }

    @Test
    public void updateViewCount_조회_이력_O() {
        when(viewHistoryRepository.existsById(viewHistoryKey)).thenReturn(true);

        viewTrackerService.updateViewCount(userId, postId);

        verify(viewCountRepository, never()).increment(viewCountKey);
        verify(viewHistoryRepository, never()).save(any(ViewHistory.class));
    }

    @Test
    public void findViewCount_O() {
        Integer viewCount = 5;
        when(viewCountRepository.findViewCount(viewCountKey)).thenReturn(Optional.of(viewCount));

        Integer result = viewTrackerService.findViewCount(postId);

        assertEquals(viewCount, result);
    }

    @Test
    public void findViewCount_X() {
        when(viewCountRepository.findViewCount(viewCountKey)).thenReturn(Optional.empty());

        assertThrows(PostNotFoundException.class, () -> viewTrackerService.findViewCount(postId));
    }
}
