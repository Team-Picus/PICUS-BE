package com.picus.core.old.domain.post.domain.entity;

import com.picus.core.old.domain.post.domain.entity.pricing.BasicOption;
import com.picus.core.old.domain.shared.area.District;
import com.picus.core.old.domain.shared.category.Category;
import com.picus.core.old.domain.shared.approval.ApprovalStatus;
import com.picus.core.old.global.common.base.BaseEntity;
import com.picus.core.old.global.common.converter.CategorySetConverter;
import com.picus.core.old.global.common.converter.DistrictSetConverter;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    @Convert(converter = CategorySetConverter.class)
    private Set<Category> postCategories = new HashSet<>();

    // 가용 지역
    @Convert(converter = DistrictSetConverter.class)
    private Set<District> postDistricts = new HashSet<>();

    // 옵션 정보
    @OneToOne(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    private BasicOption basicOption;

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
        postCategories.add(category);
    }


    public void addAvailableArea(District district) {
        postDistricts.add(district);
    }

    /**
     * 승인 상태 변경
     * @param approvalStatus 변경할 승인 상태
     */
    public void updateApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public void updatePost(String title,
                           String detail) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
        if (detail != null && !detail.isBlank()) {
            this.detail = detail;
        }
    }

    public void updateBasicPrice(Integer basicPrice) {
        this.basicOption.updateBasicPrice(basicPrice);
    }

    public void clearPostCategories() {
        this.postCategories.clear();
    }

    public void clearPostDistricts() {
        this.postDistricts.clear();
    }
}
