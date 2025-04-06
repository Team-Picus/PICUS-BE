package com.picus.core.domain.reservation.domain.entity.order;

import com.picus.core.domain.reservation.domain.entity.Reservation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class SelectedOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_no")
    private Reservation reservation;

    // 기본 가격 (변경 불가능)
    @Field(name = "base_price")
    private Integer basePrice;

    // 추가 옵션
    @OneToMany(mappedBy = "selectedOption",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER)
    private List<SelectedAdditionalOption> additionalOptions = new ArrayList<>();

    // ======================================
    // =            Constructors            =
    // ======================================
    public SelectedOption(Reservation reservation, Integer basicPrice) {
        isPositive(basicPrice);
        this.reservation = reservation;
        this.basePrice = basicPrice;
    }

    // ======================================
    // =          Business Methods          =
    // ======================================
    public void addAdditionalOption(Long optionNo, int count) {
        // 옵션 No 중복 체크
        for (SelectedAdditionalOption selectedAdditionalOption : additionalOptions) {
            if (selectedAdditionalOption.getAdditionalOptionNo().equals(optionNo)) {
                throw new IllegalArgumentException("optionNo already exists");
            }
        }

        SelectedAdditionalOption selectedAdditionalOption = new SelectedAdditionalOption(this, optionNo, count);
        additionalOptions.add(selectedAdditionalOption);
    }

    public void changeAdditionalOption(Long optionNO, int count) {
        for (SelectedAdditionalOption selectedAdditionalOption : additionalOptions) {
            if (selectedAdditionalOption.getAdditionalOptionNo().equals(optionNO)) {
                selectedAdditionalOption.changeCount(count);
                return;
            }
        }

        throw new IllegalArgumentException("optionNo not found");
    }

    private void isPositive(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be positive");
        }
    }
}
