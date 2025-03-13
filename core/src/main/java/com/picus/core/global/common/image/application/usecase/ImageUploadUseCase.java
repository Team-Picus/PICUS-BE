package com.picus.core.global.common.image.application.usecase;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.picus.core.domain.post.domain.entity.image.PostImage;
import com.picus.core.global.common.exception.RestApiException;
import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.application.dto.response.ImageUrl;
import com.picus.core.global.common.image.application.dto.response.UploadUrl;
import com.picus.core.global.common.image.domain.entity.Image;
import com.picus.core.global.common.image.domain.entity.ImageType;
import com.picus.core.global.common.image.domain.factory.ImageFactory;
import com.picus.core.global.common.image.domain.repository.ImageRepository;
import com.picus.core.global.common.image.domain.repository.MessageImageRepository;
import com.picus.core.global.common.image.domain.repository.PostImageRepository;
import com.picus.core.global.common.image.domain.repository.ReviewImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ImageUploadUseCase {

    private final AmazonS3 amazonS3Client;
    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final MessageImageRepository messageImageRepository;
    private final ImageFactory imageFactory;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public UploadUrl getPostS3Url(Long userNo, UploadImage request) {
        String key = "profile/" + userNo + "/" + UUID.randomUUID() + "/" + request.filename();
        Date expiration = getExpiration();

        imageRepository.save(imageFactory.toEntity(key, request));

        // URL 생성
        GeneratePresignedUrlRequest preSignedUrl = generatePostPreSignedUrl(key, expiration);
        URL url = amazonS3Client.generatePresignedUrl(preSignedUrl);

        return UploadUrl.builder()
                .preSignedUrl(url.toExternalForm())
                .key(key)
                .build();
    }

    @Transactional(readOnly = true)
    public ImageUrl getGetS3Url(Long imageId, ImageType imageType) {
        Date expiration = getExpiration();

        Image image = getImageByType(imageId, imageType);
        GeneratePresignedUrlRequest preSignedUrlRequest = generateGetPreSignedUrl(image.getPreSignedKey(), expiration);
        URL url = amazonS3Client.generatePresignedUrl(preSignedUrlRequest);

        return new ImageUrl(url.toExternalForm());
    }


    private GeneratePresignedUrlRequest generatePostPreSignedUrl(String key, Date expiration) {
        return new GeneratePresignedUrlRequest(bucket, key)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);
    }

    private GeneratePresignedUrlRequest generateGetPreSignedUrl(String key, Date expiration) {
        return new GeneratePresignedUrlRequest(bucket, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration);
    }

    private static Date getExpiration() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        // 만료 시간 1시간 (1000ms * 60초 * 60분)
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private Image getImageByType(Long imageId, ImageType imageType) {
        return switch (imageType) {
            case MESSAGE -> messageImageRepository.findById(imageId)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
            case POST -> postImageRepository.findById(imageId)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
            case REVIEW -> reviewImageRepository.findById(imageId)
                    .orElseThrow(() -> new RestApiException(_NOT_FOUND));
        };
    }
}
