package com.picus.core.post.adapter.out.persistence;

import com.picus.core.post.adapter.out.persistence.dto.SearchPostCond;
import com.picus.core.post.adapter.out.persistence.entity.PostEntity;
import com.picus.core.post.adapter.out.persistence.entity.PostImageEntity;
import com.picus.core.post.adapter.out.persistence.mapper.PostImagePersistenceMapper;
import com.picus.core.post.adapter.out.persistence.mapper.PostPersistenceMapper;
import com.picus.core.post.adapter.out.persistence.repository.PostImageJpaRepository;
import com.picus.core.post.adapter.out.persistence.repository.PostJpaRepository;
import com.picus.core.post.application.port.out.*;
import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.PostImage;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.post.domain.vo.SnapSubTheme;
import com.picus.core.post.domain.vo.SpaceType;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.picus.core.post.adapter.out.persistence.entity.QPostEntity.postEntity;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;
import static org.springframework.util.StringUtils.hasText;


@PersistenceAdapter
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostCreatePort, PostReadPort, PostUpdatePort, PostDeletePort {

    private final PostJpaRepository postJpaRepository;
    private final PostImageJpaRepository postImageJpaRepository;

    private final JPAQueryFactory queryFactory; // querydsl

    private final PostPersistenceMapper postPersistenceMapper;
    private final PostImagePersistenceMapper postImagePersistenceMapper;

    // 저장
    @Override
    public Post save(Post post) {
        PostEntity postEntity = savePostEntity(post); // PostEntity 저장

        List<PostImage> postImages = savePostImageEntities(post.getPostImages(), postEntity); // 해당 Post의 PostImageEntity 저장

        return postPersistenceMapper.toDomain(postEntity, postImages);
    }

    // id로 단건 조회
    @Override
    public Optional<Post> findById(String postNo) {
        Optional<PostEntity> optionalPostEntity = postJpaRepository.findById(postNo);

        if (optionalPostEntity.isEmpty()) {
            return Optional.empty();
        }

        PostEntity postEntity = optionalPostEntity.get();
        List<PostImageEntity> imageEntities = postImageJpaRepository.findByPostEntity_PostNo(postNo);
        List<PostImage> postImages = imageEntities.stream()
                .map(postImagePersistenceMapper::toDomain)
                .toList();

        Post post = postPersistenceMapper.toDomain(postEntity, postImages);
        return Optional.of(post);
    }

    // expertNo로 제일 최신 updatedAt 조회
    @Override
    public Optional<LocalDateTime> findTopUpdatedAtByExpertNo(String expertNo) {
        return postJpaRepository.findTopUpdatedAtByExpertNo(expertNo);
    }

    // 고정처리된 특정 expertNo Post 조회
    @Override
    public Optional<Post> findByExpertNoAndIsPinnedTrue(String expertNo) {
        // 특정 전문가의 고정처리한 게시물 조회
        Optional<PostEntity> optionalPostEntity = postJpaRepository.findByExpertNoAndIsPinnedTrue(expertNo);

        if (optionalPostEntity.isEmpty()) {
            return Optional.empty();
        }

        // 해당 게시물의 이미지 조회
        PostEntity postEntity = optionalPostEntity.get();
        List<PostImageEntity> imageEntities = postImageJpaRepository.findByPostEntity_PostNo(postEntity.getPostNo());
        List<PostImage> postImages = imageEntities.stream()
                .map(postImagePersistenceMapper::toDomain)
                .toList();

        Post post = postPersistenceMapper.toDomain(postEntity, postImages);
        return Optional.of(post);
    }

    // title에 특정 keyword가 포함된 Post title로 정렬해서 N개 조회
    @Override
    public List<Post> findTopNByTitleContainingOrderByTitle(String keyword, int size) {
        List<PostEntity> postEntities = postJpaRepository.findTopNByTitleContainingOrderByTitle(keyword, size);

        return postEntities.stream()
                .map(postEntity -> postPersistenceMapper.toDomain(postEntity, List.of()))
                .toList();
    }

    // 고정처리된 게시물 N개 랜덤하게 조회
    @Override
    public List<Post> findRandomTopNByIsPinnedTrue(int size) {
        // 고정 처리된 PostEntity 랜덤하게 N개 조회
        List<PostEntity> postEntities = postJpaRepository.findRandomTopNByIsPinnedTrue(size);

        return postEntities.stream()
                .map(postEntity -> {
                    // 조회된 PostEntity의 PostImageEntity 조회
                    List<PostImageEntity> imageEntities =
                            postImageJpaRepository.findByPostEntity_PostNo(postEntity.getPostNo());

                    List<PostImage> postImages = imageEntities.stream()
                            .map(postImagePersistenceMapper::toDomain)
                            .toList();

                    return postPersistenceMapper.toDomain(postEntity, postImages);
                })
                .toList();
    }

    // Post 특정 검색 조건으로 조회
    @Override
    public List<Post> findBySearchCond(SearchPostCond cond, Object cursor, String sortBy, String sortDirection, int size) {

        return queryFactory
                .selectFrom(postEntity)
                .where(
                        themeTypesIn(cond.themeTypes()),
                        snapSubThemesIn(cond.snapSubThemes()),
                        moodTypesIn(cond.moodTypes()),
                        spaceTypeEq(cond.spaceType()),
                        addressEq(cond.address()),
                        titleKeywordLike(cond.keyword()),
                        cursorCond(cursor, sortBy, sortDirection)
                )
                .orderBy(sortOrder(sortBy, sortDirection))
                .limit(size)
                .fetch()
                .stream()
                .map(post -> {
                    List<PostImageEntity> imageEntities = postImageJpaRepository.findByPostEntity_PostNo(post.getPostNo());
                    List<PostImage> postImages = imageEntities.stream()
                            .map(postImagePersistenceMapper::toDomain)
                            .toList();
                    return postPersistenceMapper.toDomain(post, postImages);
                })
                .toList();
    }

    @Override
    public List<Post> findByIdList(List<String> postNoList) {

        // PostEntity 조회
        List<PostEntity> postEntities = postJpaRepository.findByPostNoIn(postNoList);

        return postEntities.stream()
                .map(postEntity -> {
                    // 해당 PostEntity의 PostImageEntity 조회
                    List<PostImageEntity> imageEntities = postImageJpaRepository.findByPostEntity_PostNo(postEntity.getPostNo());

                    // PostImage 도메인으로 매핑
                    List<PostImage> postImages = imageEntities.stream()
                            .map(postImagePersistenceMapper::toDomain)
                            .toList();

                    // Post 도메인으로 매핑
                    return postPersistenceMapper.toDomain(postEntity, postImages);
                })
                .toList();
    }

    // PostEntity만 수정
    @Override
    public void update(Post post) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // PostEntity 수정
        postEntity.updatePostEntity(
                post.getPackageNos(), post.getTitle(), post.getOneLineDescription(),
                post.getDetailedDescription(), post.getPostThemeTypes(), post.getSnapSubThemes(), post.getPostMoodTypes(),
                post.getSpaceType(), post.getSpaceAddress(), post.getIsPinned()
        );
    }

    // PostImage도 함께 수정
    @Override
    public void updateWithPostImage(Post post, List<String> deletedPostImageNos) {
        PostEntity postEntity = postJpaRepository.findById(post.getPostNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // PostEntity 수정
        postEntity.updatePostEntity(
                post.getPackageNos(), post.getTitle(), post.getOneLineDescription(),
                post.getDetailedDescription(), post.getPostThemeTypes(), post.getSnapSubThemes(), post.getPostMoodTypes(),
                post.getSpaceType(), post.getSpaceAddress(), post.getIsPinned()
        );

        // PostImageEntity 수정
        // 삭제
        if (!deletedPostImageNos.isEmpty()) {
            postImageJpaRepository.deleteByPostImageNoIn(deletedPostImageNos);

            // 삭제 후 이미지 순서 재정렬
            List<PostImageEntity> imagesAfterDelete = postImageJpaRepository.findByPostEntity_PostNoOrderByImageOrder(post.getPostNo());
            for (int i = 0; i < imagesAfterDelete.size(); i++) {
                imagesAfterDelete.get(i).assignImageOrder(i + 1);
            }
        }

        // 추가 수정 전에 모든 imageOrder 값을 임시값으로 변경
        postImageJpaRepository.shiftAllImageOrdersToNegative(post.getPostNo());

        // 추가/수정
        List<PostImage> postImages = post.getPostImages();
        for (PostImage domain : postImages) {
            String postImageNo = domain.getPostImageNo();
            if (postImageNo != null) {
                // pk가 있다 = 수정
                PostImageEntity entity = postImageJpaRepository.findById(postImageNo)
                        .orElseThrow(() -> new RestApiException(_NOT_FOUND));
                entity.updatePostImageEntity(domain.getFileKey(), domain.getImageOrder());
            } else {
                // pk가 없다 = 추가
                PostImageEntity entity = postImagePersistenceMapper.toEntity(domain);
                entity.bindPostEntity(postEntity);
                postImageJpaRepository.save(entity);
            }
        }

        // 최종적으로 재정렬
        List<PostImageEntity> finalImages = postImageJpaRepository.findByPostEntity_PostNoOrderByImageOrder(post.getPostNo());

        for (int i = 0; i < finalImages.size(); i++) {
            finalImages.get(i).assignImageOrder(i + 1);
        }
    }

    // 삭제
    @Override
    public void delete(String postNo) {
        // PostImageEntity 삭제
        postImageJpaRepository.deleteByPostEntity_PostNo(postNo);

        // PostEntity 삭제
        postJpaRepository.deleteById(postNo);
    }

    /**
     * private 메서드
     */
    private PostEntity savePostEntity(Post post) {
        PostEntity postEntity = postPersistenceMapper.toEntity(post);
        postJpaRepository.save(postEntity);
        return postEntity;
    }

    private List<PostImage> savePostImageEntities(List<PostImage> postImages, PostEntity postEntity) {
        List<PostImageEntity> postImageEntities = postImages.stream()
                .map(postImage -> {
                    PostImageEntity entity = postImagePersistenceMapper.toEntity(postImage);
                    entity.bindPostEntity(postEntity);
                    return entity;
                })
                .toList();

        return postImageJpaRepository.saveAll(postImageEntities).stream()
                .map(postImagePersistenceMapper::toDomain)
                .toList();
    }

    private BooleanBuilder themeTypesIn(List<PostThemeType> themeTypes) {
        BooleanBuilder builder = new BooleanBuilder();
        if (themeTypes != null && !themeTypes.isEmpty()) {
            for (PostThemeType t : themeTypes) {
                // 두 번째 인자 없이, SQL 리터럴 '%SNAP%' 등을 템플릿에 직접 넣음
                builder.or(Expressions.booleanTemplate(
                        "{0} like '%" + t.name() + "%'",
                        postEntity.postThemeTypes
                ));
            }
        }
        return builder;
    }

    private BooleanBuilder snapSubThemesIn(List<SnapSubTheme> snapSubThemes) {
        BooleanBuilder builder = new BooleanBuilder();
        if (snapSubThemes != null && !snapSubThemes.isEmpty()) {
            for (SnapSubTheme s : snapSubThemes) {
                builder.or(Expressions.booleanTemplate(
                        "{0} like '%" + s.name() + "%'",
                        postEntity.snapSubThemes
                ));
            }
        }
        return builder;
    }

    private BooleanBuilder moodTypesIn(List<PostMoodType> moodTypes) {
        BooleanBuilder builder = new BooleanBuilder();
        if (moodTypes != null && !moodTypes.isEmpty()) {
            for (PostMoodType m : moodTypes) {
                builder.or(Expressions.booleanTemplate(
                        "{0} like '%" + m.name() + "%'",
                        postEntity.postMoodTypes
                ));
            }
        }
        return builder;
    }

    private BooleanBuilder spaceTypeEq(SpaceType type) {
        return (type != null)
                ? new BooleanBuilder(postEntity.spaceType.eq(type))
                : new BooleanBuilder();
    }

    private BooleanBuilder addressEq(String address) {
        return (hasText(address))
                ? new BooleanBuilder(postEntity.spaceAddress.eq(address))
                : new BooleanBuilder();
    }

    private BooleanBuilder titleKeywordLike(String titleKeyword) {
        return (hasText(titleKeyword))
                ? new BooleanBuilder(postEntity.title.like("%" + titleKeyword + "%"))
                : new BooleanBuilder();
    }

    private BooleanBuilder cursorCond(Object cursor, String sortBy, String sortDirection) {
        if (cursor == null) return new BooleanBuilder();

        boolean isDESC = "DESC".equalsIgnoreCase(sortDirection);

        try {
            // 현재는 sortBy가 "createdAt"뿐이므로 바로 createdAt 리턴
            return new BooleanBuilder(
                    isDESC ? postEntity.createdAt.lt((LocalDateTime) cursor) : postEntity.createdAt.gt((LocalDateTime) cursor));
        } catch (Exception e) {
            return new BooleanBuilder();
        }
    }

    private OrderSpecifier<?> sortOrder(String sortBy, String sortDirection) {
        boolean isDESC = "DESC".equalsIgnoreCase(sortDirection);
        // 현재는 sortBy가 "createdAt"뿐이므로 바로 createdAt 리턴
        return isDESC ? postEntity.createdAt.desc() : postEntity.createdAt.asc();
    }
}