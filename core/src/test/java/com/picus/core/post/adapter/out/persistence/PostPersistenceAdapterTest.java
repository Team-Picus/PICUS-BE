package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.dto.SearchPostCond;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.adapter.out.persistence.mapper.PostImagePersistenceMapper;
import com.picus.core.post.adapter.out.persistence.mapper.PostPersistenceMapper;
import com.picus.core.post.adapter.out.persistence.repository.PostImageJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.config.QueryDslConfig;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.picus.core.post.domain.vo.PostMoodType.*;
import static com.picus.core.post.domain.vo.PostThemeType.BEAUTY;
import static com.picus.core.post.domain.vo.PostThemeType.SNAP;
import static com.picus.core.post.domain.vo.SnapSubTheme.ADMISSION;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.*;


@Import({
        PostPersistenceAdapter.class,
        PostPersistenceMapper.class,
        PostImagePersistenceMapper.class,
//        JpaAuditingConfiguration.class,
        PostPersistenceAdapterTest.NoAuditing.class, // 현재 테스트 클래스에서 Auditing이 적용되지 않도록
        QueryDslConfig.class
})
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PostPersistenceAdapterTest {

    @Autowired
    PostJpaRepository postJpaRepository;
    @Autowired
    PostImageJpaRepository postImageJpaRepository;

    @Autowired
    private PostPersistenceAdapter postPersistenceAdapter;

    @Autowired
    EntityManager em;

    @TestConfiguration
    static class NoAuditing {
        @Bean
        public AuditorAware<String> auditorAware() {
            return Optional::empty;
        }
    }

    @Test
    @DisplayName("Post를 데이터베이스에 저장한다.")
    public void save_success() throws Exception {
        // given
        Post post = createPost(null, "package-123", "expert-456",
                "테스트 제목", "한 줄 설명", "자세한 설명입니다.",
                List.of(BEAUTY, SNAP), List.of(SnapSubTheme.FAMILY),
                List.of(COZY), SpaceType.INDOOR, "서울특별시 강남구", false,
                List.of(
                        PostImage.builder()
                                .fileKey("img_1.jpg")
                                .imageOrder(1)
                                .build(),
                        PostImage.builder()
                                .fileKey("img_2.jpg")
                                .imageOrder(2)
                                .build()
                ));

        // when
        Post saved = postPersistenceAdapter.save(post);

        // then
        // PostEntity
        Optional<PostEntity> optionalPostEntity = postJpaRepository.findById(saved.getPostNo());
        assertThat(optionalPostEntity).isPresent();
        PostEntity postEntity = optionalPostEntity.get();
        assertThat(postEntity.getPostNo()).isEqualTo(saved.getPostNo());
        assertThat(postEntity.getPackageNo()).isEqualTo("package-123");
        assertThat(postEntity.getExpertNo()).isEqualTo("expert-456");
        assertThat(postEntity.getTitle()).isEqualTo("테스트 제목");
        assertThat(postEntity.getOneLineDescription()).isEqualTo("한 줄 설명");
        assertThat(postEntity.getDetailedDescription()).isEqualTo("자세한 설명입니다.");
        assertThat(postEntity.getPostThemeTypes()).isEqualTo(List.of(BEAUTY, SNAP));
        assertThat(postEntity.getSnapSubThemes()).isEqualTo(List.of(SnapSubTheme.FAMILY));
        assertThat(postEntity.getPostMoodTypes()).isEqualTo(List.of(COZY));
        assertThat(postEntity.getSpaceType()).isEqualTo(SpaceType.INDOOR);
        assertThat(postEntity.getSpaceAddress()).isEqualTo("서울특별시 강남구");
        assertThat(postEntity.getIsPinned()).isEqualTo(false);

        // PostImageEntity
        List<PostImageEntity> imageEntities = postImageJpaRepository.findByPostEntity_PostNo(saved.getPostNo());
        assertThat(imageEntities).hasSize(2)
                .extracting(PostImageEntity::getFileKey, PostImageEntity::getImageOrder)
                .containsExactlyInAnyOrder(
                        tuple("img_1.jpg", 1),
                        tuple("img_2.jpg", 2)
                );

    }

    @Test
    @DisplayName("postNo로 Post 도메인 모델을 조회한다.")
    public void findById_success() throws Exception {
        // given
        // 데이터베이스에 데이터 셋팅
        PostEntity postEntity = createPostEntity(
                "package-123", "expert-456", "제목", "설명", "상세 설명",
                List.of(SNAP), List.of(SnapSubTheme.FAMILY), List.of(COZY),
                SpaceType.INDOOR, "서울시 강남구", false
        );
        createPostImageEntity("file.jpg", 1, postEntity);

        clearPersistenceContext();

        // when
        Optional<Post> result = postPersistenceAdapter.findById(postEntity.getPostNo());

        // then
        assertThat(result).isPresent();

        Post post = result.get();
        assertThat(post.getPostNo()).isEqualTo(postEntity.getPostNo());
        assertThat(post.getPackageNo()).isEqualTo(postEntity.getPackageNo());
        assertThat(post.getAuthorNo()).isEqualTo(postEntity.getExpertNo());
        assertThat(post.getTitle()).isEqualTo(postEntity.getTitle());
        assertThat(post.getOneLineDescription()).isEqualTo(postEntity.getOneLineDescription());
        assertThat(post.getDetailedDescription()).isEqualTo(postEntity.getDetailedDescription());
        assertThat(post.getPostThemeTypes()).isEqualTo(postEntity.getPostThemeTypes());
        assertThat(post.getSnapSubThemes()).isEqualTo(postEntity.getSnapSubThemes());
        assertThat(post.getPostMoodTypes()).isEqualTo(postEntity.getPostMoodTypes());
        assertThat(post.getSpaceType()).isEqualTo(postEntity.getSpaceType());
        assertThat(post.getSpaceAddress()).isEqualTo(postEntity.getSpaceAddress());
        assertThat(post.getIsPinned()).isEqualTo(postEntity.getIsPinned());

        assertThat(post.getPostImages()).hasSize(1)
                .extracting(PostImage::getFileKey, PostImage::getImageOrder)
                .containsExactly(tuple("file.jpg", 1));
    }

    @Test
    @DisplayName("Post를 수정한다. 이때 PostImage 수정되지 않는다.")
    public void update_onlyPost() throws Exception {
        // given

        // 데이터베이스에 데이터셋팅
        PostEntity postEntity = createPostEntity(
                "package-123", "expert-456", "old_title", "old_one", "old_detail",
                List.of(BEAUTY), List.of(), List.of(COZY),
                SpaceType.INDOOR, "old_address", false
        );
        clearPersistenceContext();

        // 수정할 Post 객체
        Post updatedPost = createPost(postEntity.getPostNo(), "package-456", "expert-456",
                "new_title", "new_one", "new_detail",
                List.of(BEAUTY, SNAP), List.of(SnapSubTheme.FAMILY), List.of(VINTAGE),
                SpaceType.OUTDOOR, "new_address", true, List.of()
        );

        // when
        postPersistenceAdapter.update(updatedPost);
        clearPersistenceContext();

        // then
        // PostEntity 검증
        PostEntity postResult = postJpaRepository.findById(postEntity.getPostNo())
                .orElseThrow();
        assertThat(postResult.getPostNo()).isEqualTo(postEntity.getPostNo());
        assertThat(postResult.getPackageNo()).isEqualTo("package-456");
        assertThat(postResult.getExpertNo()).isEqualTo("expert-456");
        assertThat(postResult.getTitle()).isEqualTo("new_title");
        assertThat(postResult.getOneLineDescription()).isEqualTo("new_one");
        assertThat(postResult.getDetailedDescription()).isEqualTo("new_detail");
        assertThat(postResult.getPostThemeTypes()).isEqualTo(List.of(BEAUTY, SNAP));
        assertThat(postResult.getSnapSubThemes()).isEqualTo(List.of(SnapSubTheme.FAMILY));
        assertThat(postResult.getPostMoodTypes()).isEqualTo(List.of(VINTAGE));
        assertThat(postResult.getSpaceType()).isEqualTo(SpaceType.OUTDOOR);
        assertThat(postResult.getSpaceAddress()).isEqualTo("new_address");
        assertThat(postResult.getIsPinned()).isEqualTo(true);
    }


    @Test
    @DisplayName("Post를 수정한다. 이때 PostImage는 경우에 따라 추가/수정/삭제된다.")
    public void update_WithPostImage_success() throws Exception {
        // given

        // 데이터베이스에 데이터셋팅
        PostEntity postEntity = createPostEntity(
                "package-123", "expert-456", "old_title", "old_one", "old_detail",
                List.of(BEAUTY), List.of(), List.of(COZY),
                SpaceType.INDOOR, "old_address", false
        );
        PostImageEntity postImageEntity1 = createPostImageEntity("file_key", 1, postEntity);
        PostImageEntity postImageEntity2 = createPostImageEntity("file_key", 2, postEntity);

        clearPersistenceContext();

        // 수정할 Post 객체
        Post updatedPost = createPost(postEntity.getPostNo(), "package-456", "expert-456",
                "new_title", "new_one", "new_detail",
                List.of(BEAUTY, SNAP), List.of(SnapSubTheme.FAMILY), List.of(VINTAGE),
                SpaceType.OUTDOOR, "new_address", true,
                List.of(
                        createPostImage(null, "new_file_key", 1), // 추가된 이미지
                        createPostImage(postImageEntity1.getPostImageNo(), "file_key", 2) // 수정된 이미지
                )
        );
        List<String> deletedImageNos = List.of(postImageEntity2.getPostImageNo());

        // when
        postPersistenceAdapter.updateWithPostImage(updatedPost, deletedImageNos);
        clearPersistenceContext();

        // then
        // PostEntity 검증
        PostEntity postResult = postJpaRepository.findById(postEntity.getPostNo())
                .orElseThrow();
        assertThat(postResult.getPostNo()).isEqualTo(postEntity.getPostNo());
        assertThat(postResult.getPackageNo()).isEqualTo("package-456");
        assertThat(postResult.getExpertNo()).isEqualTo("expert-456");
        assertThat(postResult.getTitle()).isEqualTo("new_title");
        assertThat(postResult.getOneLineDescription()).isEqualTo("new_one");
        assertThat(postResult.getDetailedDescription()).isEqualTo("new_detail");
        assertThat(postResult.getPostThemeTypes()).isEqualTo(List.of(BEAUTY, SNAP));
        assertThat(postResult.getSnapSubThemes()).isEqualTo(List.of(SnapSubTheme.FAMILY));
        assertThat(postResult.getPostMoodTypes()).isEqualTo(List.of(VINTAGE));
        assertThat(postResult.getSpaceType()).isEqualTo(SpaceType.OUTDOOR);
        assertThat(postResult.getSpaceAddress()).isEqualTo("new_address");
        assertThat(postResult.getIsPinned()).isEqualTo(true);

        // PostImageEntity 검증
        List<PostImageEntity> imageResults = postImageJpaRepository.findByPostEntity_PostNo(postEntity.getPostNo());
        assertThat(imageResults).hasSize(2)
                .extracting(
                        PostImageEntity::getFileKey,
                        PostImageEntity::getImageOrder
                ).containsExactlyInAnyOrder(
                        tuple("new_file_key", 1),
                        tuple("file_key", 2)
                );
    }

    @Test
    @DisplayName("특정 postNo를 가진 PostEntity, PostImageEntity를 삭제한다.")
    public void delete_success() throws Exception {
        // given
        // 데이터베이스에 데이터셋팅
        PostEntity postEntity = createPostEntity(
                "package-123", "expert-456", "old_title", "old_one", "old_detail",
                List.of(BEAUTY), List.of(), List.of(COZY),
                SpaceType.INDOOR, "old_address", false
        );
        createPostImageEntity("file_key", 1, postEntity);
        createPostImageEntity("file_key", 2, postEntity);
        clearPersistenceContext();

        // when
        postPersistenceAdapter.delete(postEntity.getPostNo());

        // then
        assertThat(postJpaRepository.existsById(postEntity.getPostNo())).isFalse();
        assertThat(postImageJpaRepository.findByPostEntity_PostNo(postEntity.getPostNo()))
                .isEmpty();
    }

    @Test
    @DisplayName("특정 expertNo를 가진 PostEntity들 중 제일 최근에 작성된 PostEntity의 수정날짜를 조회한다.")
    public void findTopUpdatedAtByExpertNo() throws Exception {
        // given
        String expertNo = "expert-456";
        LocalDateTime baseTime = LocalDateTime.of(2020, 10, 10, 10, 0).withNano(0);
        PostEntity postEntity1 = createPostEntity(expertNo, "t1",  baseTime.minusDays(1), baseTime.minusDays(1));
        PostEntity postEntity2 = createPostEntity(expertNo, "t2", baseTime.minusDays(2), baseTime.minusDays(2));
        clearPersistenceContext();

        // when
        Optional<LocalDateTime> result = postPersistenceAdapter.findTopUpdatedAtByExpertNo(expertNo);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(postEntity1.getUpdatedAt());
    }

    @Test
    @DisplayName("특정 expertNo를 가지고 isPinned가 True인 Post를 조회한다.")
    public void findByExpertNoAndIsPinnedTrue() throws Exception {
        // given
        // 데이터베이스에 데이터 셋팅
        PostEntity postEntity = createPostEntity(
                "package-123", "expert-456", "제목", "설명", "상세 설명",
                List.of(SNAP), List.of(ADMISSION), List.of(COZY),
                SpaceType.INDOOR, "서울시 강남구", true
        );
        createPostImageEntity("file.jpg", 1, postEntity);
        clearPersistenceContext();

        // when
        Optional<Post> optionalResult = postPersistenceAdapter.findByExpertNoAndIsPinnedTrue("expert-456");

        // then
        assertThat(optionalResult).isPresent();

        Post result = optionalResult.get();
        assertThat(result.getPostNo()).isEqualTo(postEntity.getPostNo());
        assertThat(result.getPackageNo()).isEqualTo(postEntity.getPackageNo());
        assertThat(result.getAuthorNo()).isEqualTo(postEntity.getExpertNo());
        assertThat(result.getTitle()).isEqualTo(postEntity.getTitle());
        assertThat(result.getOneLineDescription()).isEqualTo(postEntity.getOneLineDescription());
        assertThat(result.getDetailedDescription()).isEqualTo(postEntity.getDetailedDescription());
        assertThat(result.getPostThemeTypes()).isEqualTo(postEntity.getPostThemeTypes());
        assertThat(result.getSnapSubThemes()).isEqualTo(postEntity.getSnapSubThemes());
        assertThat(result.getPostMoodTypes()).isEqualTo(postEntity.getPostMoodTypes());
        assertThat(result.getSpaceType()).isEqualTo(postEntity.getSpaceType());
        assertThat(result.getSpaceAddress()).isEqualTo(postEntity.getSpaceAddress());
        assertThat(result.getIsPinned()).isEqualTo(postEntity.getIsPinned());
        assertThat(result.getPostImages()).hasSize(1)
                .extracting(PostImage::getFileKey, PostImage::getImageOrder)
                .containsExactly(tuple("file.jpg", 1));
    }

    @Test
    @DisplayName("제목에 특정 키워드가 포함된 Post를 제목 기준으로 정렬해서 N개 조회")
    public void findTopNByTitleContainingOrderByTitle_success() throws Exception {
        // given
        // 데이터베이스에 데이터 셋팅
        createPostEntity("안녕");
        createPostEntity("데일리 모먼트");
        createPostEntity("데일리");
        createPostEntity("일상이 영화가 되는 데일리 시네마");
        createPostEntity("그날의 데자뷰, 익숙하지만 새로운 순간들");
        createPostEntity("데일리씬");
        createPostEntity("하세요");
        createPostEntity("지갑");
        clearPersistenceContext();

        String keyword = "데";
        int size = 5;

        // when
        List<Post> posts = postPersistenceAdapter.findTopNByTitleContainingOrderByTitle(keyword, size);

        // then
        assertThat(posts).hasSize(size)
                .extracting(Post::getTitle)
                .containsExactly(
                        "데일리",
                        "데일리 모먼트",
                        "데일리씬",
                        "그날의 데자뷰, 익숙하지만 새로운 순간들",
                        "일상이 영화가 되는 데일리 시네마"
                );
    }

    @Test
    @DisplayName("랜덤으로 N개의 고정처리된 Post를 조회한다.")
    public void findRandomTopNByIsPinnedTrue() throws Exception {
        // given
        int size = 3;
        PostEntity postEntity1 = createPostEntity("t1", true);
        PostImageEntity postImgEntity1 = createPostImageEntity("f1", 1, postEntity1);

        PostEntity postEntity2 = createPostEntity("t2", true);
        PostImageEntity postImgEntity2 = createPostImageEntity("f2", 1, postEntity2);

        PostEntity postEntity3 = createPostEntity("t3", true);
        PostImageEntity postImgEntity3 = createPostImageEntity("f3", 1, postEntity3);

        PostEntity postEntity4 = createPostEntity("t4", true);
        PostImageEntity postImgEntity4 = createPostImageEntity("f4", 1, postEntity4);
        PostEntity postEntity5 = createPostEntity("t5", true);
        PostImageEntity postImgEntity5 = createPostImageEntity("f5", 1, postEntity5);

        clearPersistenceContext();

        // when
        List<Post> postResults = postPersistenceAdapter.findRandomTopNByIsPinnedTrue(size);

        // then
        assertThat(postResults).hasSize(3);
        for (Post post : postResults) {
            assertThat(post.getPostImages()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("랜덤으로 N개의 고정처리된 Post를 조회한다. isPinned가 false면 리턴되지 않는다.")
    public void findRandomTopNByIsPinnedTrue_Only_IsPinnedTrue() throws Exception {
        // given
        int size = 3;
        PostEntity postEntity1 = createPostEntity("t1", true);
        PostImageEntity postImgEntity1 = createPostImageEntity("f1", 1, postEntity1);

        PostEntity postEntity2 = createPostEntity("t2", true);
        PostImageEntity postImgEntity2 = createPostImageEntity("f2", 1, postEntity2);

        PostEntity postEntity3 = createPostEntity("t3", true);
        PostImageEntity postImgEntity3 = createPostImageEntity("f3", 1, postEntity3);

        PostEntity postEntity4 = createPostEntity("t4", false);
        PostImageEntity postImgEntity4 = createPostImageEntity("f4", 1, postEntity4);
        PostEntity postEntity5 = createPostEntity("t5", false);
        PostImageEntity postImgEntity5 = createPostImageEntity("f5", 1, postEntity5);

        clearPersistenceContext();

        // when
        List<Post> postResults = postPersistenceAdapter.findRandomTopNByIsPinnedTrue(size);

        // then
        assertThat(postResults).hasSize(3)
                .extracting(Post::getTitle)
                .containsExactlyInAnyOrder("t1", "t2", "t3");
    }

    @Test
    @DisplayName("랜덤으로 N개의 Post를 조회할 때 현재 데이터 수가 N보다 작으면 현재 데이터 수만큼만 반환된다.")
    public void findRandomTopNByIsPinnedTrue_ifN_isBiggerThan_postNums() throws Exception {
        // given
        int size = 3;
        PostEntity postEntity1 = createPostEntity("t1", true);
        PostImageEntity postImgEntity1 = createPostImageEntity("f1", 1, postEntity1);

        PostEntity postEntity2 = createPostEntity("t2", true);
        PostImageEntity postImgEntity2 = createPostImageEntity("f2", 1, postEntity2);

        clearPersistenceContext();

        // when
        List<Post> postResults = postPersistenceAdapter.findRandomTopNByIsPinnedTrue(size);

        // then
        assertThat(postResults).hasSize(2);
        for (Post post : postResults) {
            assertThat(post.getPostImages()).isNotEmpty();
        }
    }

    @Test
    @DisplayName("조건 없이 기본값으로 게시물 조회 - 최신순, size만큼 조회")
    void findBySearchCond_defaultOnly_success() throws Exception {
        // given
        LocalDateTime baseTime = LocalDateTime.of(2020, 10, 10, 10, 0);
        PostEntity p1 = createPostEntity("t1", baseTime.minusDays(1), baseTime.minusDays(1));
        PostEntity p2 = createPostEntity("t2", baseTime.minusDays(2), baseTime.minusDays(2));
        PostEntity p3 = createPostEntity("t3", baseTime.minusDays(3), baseTime.minusDays(3));

        clearPersistenceContext();

        SearchPostCond cond = new SearchPostCond(
                List.of(), List.of(), null, null, List.of()
        );
        String cursor = null;
        String sortBy = "createdAt";
        String sortDirection = "DESC";
        int size = 2;

        // when
        List<Post> results = postPersistenceAdapter.findBySearchCond(cond, cursor, sortBy, sortDirection, size);

        // then
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Post::getTitle)
                .containsExactly("t1", "t2"); // 최신순
    }

//    @Test
//    @DisplayName("themeTypes+snapSubThemes 조건으로 게시물 필터링")
//    void findBySearchCond_themeTypesOnly_success() throws Exception {
//        // given
//        PostEntity snapPost = createPostEntity("snap-post", List.of(PostThemeType.SNAP), List.of(SnapSubTheme.FAMILY));
//        PostEntity dailyPost = createPostEntity("Beauty-post", List.of(BEAUTY), List.of());
//        PostEntity mixedPost = createPostEntity("mixed-post", List.of(PostThemeType.SNAP, BEAUTY), List.of(SnapSubTheme.FAMILY));
//
//        clearPersistenceContext();
//
//        SearchPostCond cond = new SearchPostCond(
//                List.of(PostThemeType.SNAP),
//                List.of(),
//                null,
//                null,
//                List.of()
//        );
//        String cursor = null;
//        String sortBy = "createdAt";
//        String sortDirection = "DESC";
//        int size = 10;
//
//        // when
//        List<Post> results = postPersistenceAdapter.findBySearchCond(cond, cursor, sortBy, sortDirection, size);
//
//        // then
//        assertThat(results).extracting(Post::getTitle)
//                .contains("snap-post", "mixed-post")
//                .doesNotContain("daily-post");
//    }

    /**
     * private 메서드
     */
    private Post createPost(String postNo, String packageNo, String authorNo, String title, String oneLineDescription,
                            String detailedDescription, List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                            List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                            boolean isPinned, List<PostImage> postImages) {
        return Post.builder()
                .postNo(postNo)
                .packageNo(packageNo)
                .authorNo(authorNo)
                .title(title)
                .oneLineDescription(oneLineDescription)
                .detailedDescription(detailedDescription)
                .postThemeTypes(postThemeTypes)
                .snapSubThemes(snapSubThemes)
                .postMoodTypes(postMoodTypes)
                .spaceType(spaceType)
                .spaceAddress(spaceAddress)
                .isPinned(isPinned)
                .postImages(postImages)
                .build();
    }

    private PostImage createPostImage(String postImageNo, String fileKey, int imageOrder) {
        return PostImage.builder()
                .postImageNo(postImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .build();
    }

    private PostEntity createPostEntity(String packageNo, String expertNo, String title, String oneLineDescription, String detailedDescription,
                                        List<PostThemeType> postThemeTypes, List<SnapSubTheme> snapSubThemes,
                                        List<PostMoodType> postMoodTypes, SpaceType spaceType, String spaceAddress,
                                        boolean isPinned) {
        PostEntity postEntity = PostEntity.builder()
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
        return postJpaRepository.save(postEntity);
    }

    private PostEntity createPostEntity(String title) {
        PostEntity postEntity = PostEntity.builder()
                .packageNo("packageNo")
                .expertNo("expertNo")
                .title(title)
                .oneLineDescription("oneLineDescription")
                .detailedDescription("detailedDescription")
                .postThemeTypes(List.of(BEAUTY))
                .snapSubThemes(List.of())
                .postMoodTypes(List.of(VINTAGE))
                .spaceType(SpaceType.OUTDOOR)
                .spaceAddress("spaceAddress")
                .isPinned(false)
                .build();
        return postJpaRepository.save(postEntity);
    }

    private PostEntity createPostEntity(String title, boolean isPinned) {
        PostEntity postEntity = PostEntity.builder()
                .packageNo("packageNo")
                .expertNo("expertNo")
                .title(title)
                .oneLineDescription("oneLineDescription")
                .detailedDescription("detailedDescription")
                .postThemeTypes(List.of(BEAUTY))
                .postMoodTypes(List.of(VINTAGE))
                .spaceType(SpaceType.OUTDOOR)
                .spaceAddress("spaceAddress")
                .isPinned(isPinned)
                .build();
        return postJpaRepository.save(postEntity);
    }

    private PostEntity createPostEntity(String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        PostEntity entity = PostEntity.builder()
                .packageNo("package")
                .expertNo("expert")
                .title(title)
                .oneLineDescription("desc")
                .detailedDescription("detail")
                .postThemeTypes(List.of())
                .snapSubThemes(List.of())
                .postMoodTypes(List.of())
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울시")
                .isPinned(false)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        return postJpaRepository.save(entity);
    }

    private PostEntity createPostEntity(String expertNo, String title, LocalDateTime createdAt, LocalDateTime updatedAt) {
        PostEntity entity = PostEntity.builder()
                .packageNo("package")
                .expertNo(expertNo)
                .title(title)
                .oneLineDescription("desc")
                .detailedDescription("detail")
                .postThemeTypes(List.of())
                .snapSubThemes(List.of())
                .postMoodTypes(List.of())
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울시")
                .isPinned(false)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        return postJpaRepository.save(entity);
    }

    private PostEntity createPostEntity(String title, List<PostThemeType> themeTypes, List<SnapSubTheme> snapSubThemes) {
        PostEntity entity = PostEntity.builder()
                .packageNo("package")
                .expertNo("expert")
                .title(title)
                .oneLineDescription("desc")
                .detailedDescription("detail")
                .postThemeTypes(themeTypes)
                .snapSubThemes(snapSubThemes)
                .postMoodTypes(List.of())
                .spaceType(SpaceType.INDOOR)
                .spaceAddress("서울시")
                .isPinned(false)
                .createdAt(LocalDateTime.now())
                .build();
        return postJpaRepository.save(entity);
    }

    private PostImageEntity createPostImageEntity(String fileKey, int imageOrder, PostEntity postEntity) {
        PostImageEntity postImageEntity = PostImageEntity.builder()
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .postEntity(postEntity)
                .build();
        return postImageJpaRepository.save(postImageEntity);
    }

    private void clearPersistenceContext() {
        em.flush();
        em.clear();
    }
}