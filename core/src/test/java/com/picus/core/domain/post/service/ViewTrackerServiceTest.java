package com.picus.core.domain.post.service;

import com.picus.core.domain.post.entity.view.ViewCount;
import com.picus.core.domain.post.exception.PostNotFoundException;
import com.picus.core.domain.post.repository.view.primary.ViewCountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ViewTrackerServiceTest {

    private ViewCountRepository viewCountRepository;
    private ViewTrackerService viewTrackerService;

    @BeforeEach
    void setUp() {
        viewCountRepository = mock(ViewCountRepository.class);
        viewTrackerService = new ViewTrackerService(viewCountRepository);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findViewCount_shouldReturnViewCount_whenPresent() {
        // given
        Long postId = 1L;
        Integer expectedCount = 10;
        String key = ViewCount.generateKey(postId);

        when(viewCountRepository.findViewCount(key)).thenReturn(Optional.of(expectedCount));

        // when
        Integer actualCount = viewTrackerService.findViewCount(postId);

        // then
        assertEquals(expectedCount, actualCount);
        verify(viewCountRepository, times(1)).findViewCount(key);
    }

    @Test
    void findViewCount_shouldThrowPostNotFoundException_whenNotPresent() {
        // given
        Long postId = 1L;
        String key = ViewCount.generateKey(postId);

        when(viewCountRepository.findViewCount(key)).thenReturn(Optional.empty());

        // when & then
        assertThrows(PostNotFoundException.class, () -> viewTrackerService.findViewCount(postId));
        verify(viewCountRepository, times(1)).findViewCount(key);
    }
}
