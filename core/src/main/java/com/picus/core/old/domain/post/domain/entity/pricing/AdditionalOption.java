package com.picus.core.old.domain.post.domain.entity.pricing;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
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

    private AdditionalOptionStatus status = AdditionalOptionStatus.ACTIVE;

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

    public void deactivateStatus() {
        this.status = AdditionalOptionStatus.INACTIVE;
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