package com.picus.core.reservation.domain;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SelectedPost {

    private String title;
    private String thumbnailImageKey;
    private String expertName;
    private String theme;
    private List<String> moods;

}
