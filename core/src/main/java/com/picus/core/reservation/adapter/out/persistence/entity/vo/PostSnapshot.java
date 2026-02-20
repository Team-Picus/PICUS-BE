package com.picus.core.reservation.adapter.out.persistence.entity.vo;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.util.List;

@Getter
@Builder
@Embeddable
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostSnapshot {

    private String title;
    private String thumbnailImageKey;
    private String expertName;
    private List<String> themes;
    private List<String> moods;
}
