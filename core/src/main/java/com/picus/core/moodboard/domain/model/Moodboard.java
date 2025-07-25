package com.picus.core.moodboard.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Moodboard {

    private String userNo;
    private String postNo;
    private LocalDateTime createdAt;

}
