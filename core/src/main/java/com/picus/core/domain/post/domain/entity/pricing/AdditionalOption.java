package com.picus.core.domain.post.domain.entity.pricing;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class AdditionalOption {

    @Id @Tsid
    @Column(name = "additional_option_id")
    private Long id;

    private String name;

    private Integer pricePerUnit;

    private Integer max;

    private Integer base;

    private Integer increment; // 증가폭

    @ManyToOne
    @JoinColumn(name = "basic_option_id")
    private BasicOption basicOption;


    // ======================================
    // =            Constructors            =
    // ======================================
    public AdditionalOption(BasicOption basicOption,
                            String name,
                            Integer pricePerUnit,
                            Integer max,
                            Integer base,
                            Integer increment) {
        this.basicOption = basicOption;
        this.name = name;
        this.pricePerUnit = pricePerUnit;
        this.max = max;
        this.base = base;
        this.increment = increment;
    }

    // ======================================
    // =          Business methods          =
    // ======================================
    /**
     * 추가 옵션 정보 수정,
     * @param name
     * @param pricePerUnit
     * @param max
     * @param base
     * @param increment
     */
    public void update(String name, Integer pricePerUnit, Integer max, Integer base, Integer increment) {
        // 필요에 따라 유효성 검증 로직 추가 가능
        // 예를 들어, 가격이나 범위 값의 유효성 체크 등

        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (pricePerUnit != null) {
            this.pricePerUnit = pricePerUnit;
        }
        if (max != null) {
            this.max = max;
        }
        if (base != null) {
            this.base = base;
        }
        if (increment != null) {
            this.increment = increment;
        }
    }

    public int calculatePrice(int count) {
        if(isValid(count))
            throw new RuntimeException();   // 수정: 커스텀 Exception으로 변환 예정

        return pricePerUnit * count;
    }

    private boolean isValid(int count) {
        return count >= base && count <= max;
    }
}