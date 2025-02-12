package com.picus.core.domain.payments.entity;

import com.picus.core.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Option extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id")
    private Long id;

    @Column(nullable = false)
    private Long postNo;

    private Double price;

//    @Enumerated(value = EnumType.STRING)
//    private OptionType optionType;
}
