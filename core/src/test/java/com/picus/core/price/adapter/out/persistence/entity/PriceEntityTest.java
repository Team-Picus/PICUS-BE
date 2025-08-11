package com.picus.core.price.adapter.out.persistence.entity;

import com.picus.core.price.adapter.out.persistence.repository.PriceJpaRepository;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PriceEntityTest {

    @Autowired
    private PriceJpaRepository priceJpaRepository;

    @Test
    void unique_constraint_on_expert_theme_subTheme() {
        // given: 같은 expertNo + priceThemeType + snapSubTheme 조합 2건을 저장 시도
        String expertNo = "expert-001";

        PriceEntity first = PriceEntity.builder()
                .expertNo(expertNo)
                .priceThemeType(PriceThemeType.SNAP)
                .snapSubTheme(SnapSubTheme.FAMILY)
                .build();

        PriceEntity duplicate = PriceEntity.builder()
                .expertNo(expertNo)
                .priceThemeType(PriceThemeType.SNAP)
                .snapSubTheme(SnapSubTheme.FAMILY)
                .build();
        priceJpaRepository.saveAndFlush(first);

        // when // then: 두 번째 저장에서 유니크 제약 위반 발생
        assertThatThrownBy(() -> priceJpaRepository.saveAndFlush(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}