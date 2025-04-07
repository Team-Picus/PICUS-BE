package com.picus.core.domain.post.application.converter;

import com.picus.core.domain.post.application.dto.response.*;
import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.domain.shared.image.application.dto.response.ImageUrl;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PostConverter {
    public static PostDetailResponse convertDetail(@NotNull Post post, @NotNull List<ImageUrl> imageUrls) {

        // 가용 지역: District의 displayName을 사용한다고 가정
        List<DistrictResponse> areas = post.getPostDistricts().stream()
                .map(district -> new DistrictResponse(
                        district.name(),
                        district.getDisplayName()
                ))
                .toList();

        // BasicOption 변환
        BasicOptionResponse basicOptionResponse = null;
        if (post.getBasicOption() != null) {
            basicOptionResponse = new BasicOptionResponse(
                    post.getBasicOption().getBasicPrice(),
                    post.getBasicOption().getAdditionalOptions().stream()
                            .map(opt -> new AdditionalOptionResponse(
                                    opt.getId(),
                                    opt.getName(),
                                    opt.getPricePerUnit(),
                                    opt.getMax(),
                                    opt.getBase(),
                                    opt.getIncrement()))
                            .collect(Collectors.toList())
            );
        }


        // 카테고리 변환: PostCategory의 category(enum)에서 이름과 타입을 추출
        List<CategoryRespnose> categoryRespnoses = post.getPostCategories().stream()
                .map(pc -> new CategoryRespnose(
                        pc.name(),          // 또는 pc.getCategory().getDisplayName() 등
                        pc.getType().name()))
                .collect(Collectors.toList());

        return new PostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getDetail(),
                post.getStudioNo(),
                areas,
                basicOptionResponse,
//                imageDtos,
                categoryRespnoses,
                post.getPostStatus(),
                imageUrls,
                post.getApprovalStatus()
        );
    }

    public static PostSummaryResponse convertSummary(Post post) {

        Integer basicPrice = null;

        if (post.getBasicOption() != null) {
            basicPrice = post.getBasicOption().getBasicPrice();
        }

        return new PostSummaryResponse(
                post.getId(),
                post.getStudioNo(),
                post.getTitle(),
                post.getDetail(),
                basicPrice
//                postImageDtos
        );
    }
}
