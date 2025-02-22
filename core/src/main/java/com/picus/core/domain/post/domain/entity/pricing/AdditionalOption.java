package com.picus.core.domain.post.domain.entity.pricing;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class AdditionalOption {

    @Id @Tsid
    @Column(name = "additional_option_id")
    private Long id;

    private Integer pricePerUnit;

    private Integer max;

    private Integer base;

    private Integer increment;

    private OptionType optionType;

    @ManyToOne
    @JoinColumn(name = "basic_option_id")
    private BasicOption basicOption;

    public int calculatePrice(int count) {
        if(isValid(count))
            throw new RuntimeException();   // 수정: 커스텀 Exception으로 변환 예정

        return pricePerUnit * count;
    }

    private boolean isValid(int count) {
        return count >= base && count <= max;
    }
}