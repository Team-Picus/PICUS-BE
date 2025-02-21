package com.picus.core.domain.post.entity.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BackupViewCount {

    @Id
    @Column(name = "post_id")
    private Long id;

    private Integer viewCount;
}
