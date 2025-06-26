package com.picus.core.old.domain.reservation.domain.entity.order;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예약 내역을 저장하는 Value Entity
 *
 * 실제 Post에서 제시하는 BasePrice, Option 들을 가져오고 사용자가 얼만큼의 옵션을 선택했는지를 "COPY" 해서 저장해야한다.
 * 왜냐하면 Option 가격이 추후에 변동될 수도 있기 떄문
 */
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"selected_option_id", "additional_option_no"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SelectedAdditionalOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id")
    private SelectedOption selectedOption;

    // Post에서 제시하는 Option 번호
    @Column(name = "option_no")
    private Long additionalOptionNo;

    // 선택한 옵션의 개수
    private Integer count;

    public SelectedAdditionalOption(SelectedOption selectedOption, Long additionalOptionNo, Integer count) {
        isPositive(count);
        this.selectedOption = selectedOption;
        this.additionalOptionNo = additionalOptionNo;
        this.count = count;
    }

    public void changeCount(int count) {
        isPositive(count);
        this.count = count;
    }

    private void isPositive(int count) {
        if (count < 0) {
            throw new IllegalArgumentException("count must be positive");
        }
    }

}
