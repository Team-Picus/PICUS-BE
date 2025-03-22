package com.picus.core.domain.post.application.converter;

import com.picus.core.domain.post.application.dto.response.*;
import com.picus.core.domain.post.domain.entity.Post;
import com.picus.core.domain.shared.image.application.dto.response.ImageUrl;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

public abstract class PostConverter {
    public static PostDetailDto convertDetail(@NotNull Post post, @NotNull List<ImageUrl> imageUrls) {

        // 가용 지역: District의 displayName을 사용한다고 가정
        List<DistrictDto> areas = post.getPostDistricts().stream()
                .map(district -> new DistrictDto(
                        district.getDistrict().name(),
                        district.getDistrict().getDisplayName()
                ))
                .toList();

        // BasicOption 변환
        BasicOptionDto basicOptionDto = null;
        if (post.getBasicOption() != null) {
            basicOptionDto = new BasicOptionDto(
                    post.getBasicOption().getBasicPrice(),
                    post.getBasicOption().getAdditionalOptions().stream()
                            .map(opt -> new AdditionalOptionDto(
                                    opt.getName(),
                                    opt.getPricePerUnit(),
                                    opt.getMax(),
                                    opt.getBase(),
                                    opt.getIncrement()))
                            .collect(Collectors.toList())
            );
        }

//        // 이미지 변환: PostImageResource에서 preSignedKey를 가져온다고 가정
//        List<PostImageDto> imageDtos = post.getImages().stream()
//                .map(img -> new PostImageDto(img.getPreSignedKey()))
//                .collect(Collectors.toList());

        // 카테고리 변환: PostCategory의 category(enum)에서 이름과 타입을 추출
        List<CategoryDto> categoryDtos = post.getPostCategories().stream()
                .map(pc -> new CategoryDto(
                        pc.getCategory().name(),          // 또는 pc.getCategory().getDisplayName() 등
                        pc.getCategory().getType().name()))
                .collect(Collectors.toList());

        return new PostDetailDto(
                post.getId(),
                post.getTitle(),
                post.getDetail(),
                post.getStudioNo(),
                areas,
                basicOptionDto,
//                imageDtos,
                categoryDtos,
                post.getPostStatus(),
                imageUrls,
                post.getApprovalStatus()
        );
    }

    public static PostSummaryDto convertSummary(Post post) {

        Integer basicPrice = null;

        if (post.getBasicOption() != null) {
            basicPrice = post.getBasicOption().getBasicPrice();
        }
//
//        List<PostImageDto> postImageDtos = post.getImages().stream()
//                .map(img -> new PostImageDto(img.getPreSignedKey()))
//                .collect(Collectors.toList());

        return new PostSummaryDto(
                post.getId(),
                post.getStudioNo(),
                post.getTitle(),
                post.getDetail(),
                basicPrice
//                postImageDtos
        );
    }
}
