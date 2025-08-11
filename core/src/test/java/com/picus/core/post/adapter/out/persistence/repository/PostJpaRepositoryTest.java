package com.picus.core.post.adapter.out.persistence.repository;

import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostJpaRepositoryTest {

    @Autowired
    private PostJpaRepository postJpaRepository;

    @Test
    @DisplayName("PostEntity를 데이터베이스에 저장할 때, post theme가 SNAP 테마일 경우 snapSubThemes를 반드시 입력하지 않은 경우 에러가 발생한다.")
    public void save_error1() throws Exception {
        // given
        PostEntity postEntity = createPostEntity("pkg-123", "expert-456",
                "테스트 제목", "한 줄 설명",
                "자세한 설명입니다.", List.of(PostThemeType.SNAP), List.of(),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울특별시 강남구", false);

        // when // then
        assertThatThrownBy(() -> postJpaRepository.save(postEntity))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP 테마일 경우 snapSubThemes를 반드시 입력해야 합니다.");

    }

    @Test
    @DisplayName("PostEntity를 데이터베이스에 저장할 때, post theme가 SNAP이 아닌데 snapSubThemes가 들어있는 경우 에러가 발생한다.")
    public void save_error2() throws Exception {
        // given
        PostEntity postEntity = createPostEntity("pkg-123", "expert-456",
                "테스트 제목", "한 줄 설명",
                "자세한 설명입니다.", List.of(PostThemeType.BEAUTY), List.of(SnapSubTheme.ADMISSION),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울특별시 강남구", false);

        // when // then
        assertThatThrownBy(() -> postJpaRepository.save(postEntity))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP이 아닌데 snapSubThemes가 들어있을 수 없습니다.");

    }

    @Test
    @DisplayName("PostEntity를 업데이트 할 때, post theme가 SNAP 테마일 경우 snapSubThemes를 반드시 입력하지 않은 경우 에러가 발생한다.")
    public void update_error1() throws Exception {
        // given
        PostEntity postEntity = createPostEntity("pkg-123", "expert-456",
                "테스트 제목", "한 줄 설명",
                "자세한 설명입니다.", List.of(PostThemeType.SNAP), List.of(SnapSubTheme.ADMISSION),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울특별시 강남구", false);

        postJpaRepository.save(postEntity);

        // when
        postEntity.updatePostEntity(
                postEntity.getPackageNo(), postEntity.getTitle(), postEntity.getOneLineDescription(),
                postEntity.getDetailedDescription(), postEntity.getPostThemeTypes(), List.of(), postEntity.getPostMoodTypes(),
                postEntity.getSpaceType(), postEntity.getSpaceAddress(), postEntity.getIsPinned()
        );

        // then
        assertThatThrownBy(() -> postJpaRepository.flush())
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP 테마일 경우 snapSubThemes를 반드시 입력해야 합니다.");
    }

    @Test
    @DisplayName("PostEntity를 업데이트 할 때, post theme가 SNAP이 아닌데 snapSubThemes가 들어있는 경우 에러가 발생한다.")
    public void update_error2() throws Exception {
        // given
        PostEntity postEntity = createPostEntity("pkg-123", "expert-456",
                "테스트 제목", "한 줄 설명",
                "자세한 설명입니다.", List.of(PostThemeType.BEAUTY), List.of(),
                List.of(PostMoodType.COZY), SpaceType.INDOOR, "서울특별시 강남구", false);

        postJpaRepository.save(postEntity);

        // when
        postEntity.updatePostEntity(
                postEntity.getPackageNo(), postEntity.getTitle(), postEntity.getOneLineDescription(),
                postEntity.getDetailedDescription(), postEntity.getPostThemeTypes(), List.of(SnapSubTheme.ADMISSION), postEntity.getPostMoodTypes(),
                postEntity.getSpaceType(), postEntity.getSpaceAddress(), postEntity.getIsPinned()
        );

        // then
        assertThatThrownBy(() -> postJpaRepository.flush())
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP이 아닌데 snapSubThemes가 들어있을 수 없습니다.");

    }

    private PostEntity createPostEntity(String packageNo, String expertNo, String title, String oneLineDescription,
                                        String detailedDescription, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                                        List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                                        boolean isPinned) {
        return PostEntity.builder()
                .packageNo(packageNo)
                .expertNo(expertNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .snapSubThemes(snapSubThemes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .isPinned(isPinned)
                .build();
    }

}