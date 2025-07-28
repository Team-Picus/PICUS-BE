package com.picus.core.post.application.service;

import com.picus.core.post.application.port.in.mapper.LoadGalleryAppMapper;
import com.picus.core.post.application.port.in.response.LoadGalleryAppResp;
import com.picus.core.post.application.port.out.ReadPostPort;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class LoadGalleryServiceTest {

    @Mock private ReadPostPort readPostPort;
    @Mock private LoadGalleryAppMapper appMapper;

    @InjectMocks LoadGalleryService loadGalleryService;

    @Test
    @DisplayName("특정 전문가의 갤러리를 조회한다.")
    public void load_success() throws Exception {
        // given
        String expertNo = "expert-123";

        Post post = Post.builder()
                .postImages(List.of(
                        createPostImage("img-123", "file1.jpg", 1),
                        createPostImage("img-234", "file2.jpg", 2)
                ))
                .build();
        given(readPostPort.findByExpertNoAndIsPinnedTrue(expertNo)).willReturn(Optional.of(post));
        LoadGalleryAppResp loadGalleryAppResp = mock(LoadGalleryAppResp.class);
        given(appMapper.toAppResp(post, "")).willReturn(loadGalleryAppResp); // TODO: fileKey -> url 변환로직 추가 후 수정


        // when
        Optional<LoadGalleryAppResp> result = loadGalleryService.load(expertNo);

        // then
        assertThat(result).isPresent();

        then(readPostPort).should().findByExpertNoAndIsPinnedTrue(expertNo);
        then(appMapper).should().toAppResp(post, "");
    }

    @Test
    @DisplayName("특정 전문가의 갤러리를 조회한다. 고정처리한 게시물이 없는 경우, 널이 리턴된다.")
    public void load_ifNotExist() throws Exception {
        // given
        String expertNo = "expert-123";

        given(readPostPort.findByExpertNoAndIsPinnedTrue(expertNo)).willReturn(Optional.empty());


        // when
        Optional<LoadGalleryAppResp> result = loadGalleryService.load(expertNo);

        // then
        assertThat(result).isEmpty();

        then(readPostPort).should().findByExpertNoAndIsPinnedTrue(expertNo);
    }

    private PostImage createPostImage(String postImageNo, String fileKey, int imageOrder) {
        return PostImage.builder()
                .postImageNo(postImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .build();
    }

}