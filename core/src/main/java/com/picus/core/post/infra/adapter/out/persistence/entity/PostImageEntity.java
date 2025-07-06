package com.picus.core.post.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "post_images")
public class PostImageEntity {

    @Id @Tsid
    private String postImageNo;

    private String key;
}
