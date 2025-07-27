package com.picus.core.post.domain;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PostImage {

    private String postImageNo;

    private String fileKey;
    private Integer imageOrder;

    public void updatePostImage(String fileKey, Integer imageOrder) {
        if(fileKey != null)
            this.fileKey = fileKey;
        if(imageOrder != null)
            this.imageOrder = imageOrder;
    }
}
