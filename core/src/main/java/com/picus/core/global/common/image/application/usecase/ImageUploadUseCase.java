package com.picus.core.global.common.image.application.usecase;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.picus.core.global.common.image.application.dto.request.UploadImage;
import com.picus.core.global.common.image.application.dto.response.ImageUrl;
import com.picus.core.global.common.image.application.dto.response.UploadUrl;
import com.picus.core.global.common.image.domain.entity.Image;
import com.picus.core.global.common.image.domain.entity.ImageType;
import com.picus.core.global.common.image.domain.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageUploadUseCase {

    private final AmazonS3 amazonS3Client;
    private final ImageService imageService;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public UploadUrl getPostS3Url(Long userNo, UploadImage request) {
        String key = "profile/" + userNo + "/" + UUID.randomUUID() + "/" + request.filename();
        Date expiration = getExpiration();

        Image image = imageService.save(key, request);

        // URL 생성
        GeneratePresignedUrlRequest preSignedUrl = generatePostPreSignedUrl(key, expiration);
        URL url = amazonS3Client.generatePresignedUrl(preSignedUrl);

        return UploadUrl.builder()
                .preSignedUrl(url.toExternalForm())
                .imageId(image.getId())
                .build();
    }

    @Transactional(readOnly = true)
    public String getImage(Long imageId, ImageType imageType) {
        Date expiration = getExpiration();

        Image image = imageService.findImage(imageId, imageType);
        GeneratePresignedUrlRequest preSignedUrlRequest = generateGetPreSignedUrl(image.getPreSignedKey(), expiration);
        URL url = amazonS3Client.generatePresignedUrl(preSignedUrlRequest);

        return url.toExternalForm();
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
}
