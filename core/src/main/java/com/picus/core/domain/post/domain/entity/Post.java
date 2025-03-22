package com.picus.core.domain.post.domain.entity;

import com.picus.core.domain.post.domain.entity.area.PostDistrict;
import com.picus.core.domain.post.domain.entity.cateogory.PostCategory;
import com.picus.core.domain.post.domain.entity.pricing.BasicOption;
import com.picus.core.domain.shared.area.entity.District;
import com.picus.core.domain.shared.category.entity.Category;
import com.picus.core.domain.shared.enums.ApprovalStatus;
import com.picus.core.global.common.base.BaseEntity;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

    @Id
    @Tsid
    @Column(name = "post_no")
    private Long id;

    // 기본 정보
    private String title;
    private String detail;
    private Long studioNo;
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostCategory> postCategories = new ArrayList<>();

    // 가용 지역
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostDistrict> postDistricts = new ArrayList<>();

    // 옵션 정보
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private BasicOption basicOption;

//    // 이미지
//    @OneToMany(cascade = CascadeType.ALL,
//            orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "post")
//    private List<PostImageResource> images = new ArrayList<>();

    // 상태
    private PostStatus postStatus = PostStatus.DRAFT;

    // 승인 상태
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    // ======================================
    // =            Constructors            =
    // ======================================
    /**
     * 포스트 생성자
     * @param studioNo
     */
    public Post(Long studioNo) {
        this.studioNo = studioNo;
    }

    // ======================================
    // =          Business methods          =
    // ======================================
    /**
     * 포스트 초기화,
     * @param title
     * @param detail
     * @param basicPrice
     * @return 초기화 성공 여부
     */
    public void register(String title,
                         String detail,
                         Integer basicPrice) {

        // 이미 생성된 경우
        if (getPostStatus() != PostStatus.DRAFT) {
            // TODO 예외 정의 후 예외 던져야한다.
            throw new RuntimeException("이미 게시된 포스트입니다.");
        }
        this.title = title;
        this.detail = detail;
        this.postStatus = PostStatus.PUBLISHED;
        this.basicOption = new BasicOption(this, basicPrice);
    }

    /**
     * 포스트 카테고리 추가
     * @param category 카테고리
     */
    public void addPostCategory(Category category) {
        PostCategory postCategory = new PostCategory(this, category);
        postCategories.add(postCategory);
    }

//    /**
//     * 포스트 이미지 추가
//     * @param preSignedKey 이미지 키
//     */
//    public void addPostImageResource(String preSignedKey) {
//        // TODO 이미지 갯수 제약 넣어야함
//        images.add(new PostImageResource(preSignedKey, id));
//    }

    public void addAvailableArea(District district) {
        postDistricts.add(new PostDistrict(this, district));
    }

    /**
     * 승인 상태 변경
     * @param approvalStatus 변경할 승인 상태
     */
    public void updateApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }


}
