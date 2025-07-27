package com.picus.core.post.domain;
import java.time.LocalDateTime;

public class Comment {

    private String commentNo;

    private String postNo;
    private String authorNo;

    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
