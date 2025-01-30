package com.picus.core.domain.post.entity.pricing;

import com.picus.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Option extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "option_id")
    private Long id;

    @Column(nullable = false)
    private Long postNo;

    private String name;

    private Double price;
}
