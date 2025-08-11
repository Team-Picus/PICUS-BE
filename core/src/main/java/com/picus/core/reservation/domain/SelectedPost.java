package com.picus.core.reservation.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SelectedPost {

    private String title;
    private String thumbnailImageKey;
    private String expertName;
    private String theme;

}
