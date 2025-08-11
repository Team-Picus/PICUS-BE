package com.picus.core.price.adapter.out.persistence.entity;

import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@Getter
@Table(name = "prices")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class PriceEntity {

    @Id
    @Tsid
    private String priceNo;

    @Column(nullable = false)
    private String expertNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PriceThemeType priceThemeType;

    @Enumerated(EnumType.STRING)
    private SnapSubTheme snapSubTheme;

    public void updateEntity(PriceThemeType priceThemeType, SnapSubTheme snapSubTheme) {
        this.priceThemeType = priceThemeType;
        this.snapSubTheme = snapSubTheme;
    }

    @PrePersist
    @PreUpdate
    private void validateSnapSubTheme() {
        boolean isSnap = PriceThemeType.SNAP.equals(this.priceThemeType);

        if (isSnap && (snapSubTheme == null)) {
            throw new IllegalStateException("SNAP 테마일 경우 snapSubTheme를 반드시 입력해야 합니다.");
        }

        if (!isSnap && snapSubTheme != null) {
            throw new IllegalStateException("SNAP이 아닌데 snapSubTheme가 들어있을 수 없습니다.");
        }
    }

}
