package com.picus.core.price.adapter.out.persistence.repository;

import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PriceJpaRepositoryTest {


    @Autowired
    private PriceJpaRepository priceJpaRepository;

    @Test
    @DisplayName("Price를 저장할 때, SNAP 테마일 경우 snapSubTheme가 없으면 에러가 발생한다.")
    public void save_error1() throws Exception {
        // given
        PriceEntity priceEntity = PriceEntity.builder()
                .expertNo("expert-123")
                .priceThemeType(PriceThemeType.SNAP)
                .snapSubTheme(null)
                .build();

        // when // then
        assertThatThrownBy(() -> priceJpaRepository.save(priceEntity))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP 테마일 경우 snapSubTheme를 반드시 입력해야 합니다.");
    }

    @Test
    @DisplayName("Price를 저장할 때, SNAP 테마가 아닌데 snapSubTheme가 있으면 에러가 발생한다.")
    public void save_error2() throws Exception {
        // given
        PriceEntity priceEntity = PriceEntity.builder()
                .expertNo("expert-123")
                .priceThemeType(PriceThemeType.FASHION)
                .snapSubTheme(SnapSubTheme.FRIENDSHIP)
                .build();

        // when // then
        assertThatThrownBy(() -> priceJpaRepository.save(priceEntity))
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP이 아닌데 snapSubTheme가 들어있을 수 없습니다.");
    }

    @Test
    @DisplayName("Price를 업데이트 할 때, SNAP 테마일 경우 snapSubTheme가 없으면 에러가 발생한다.")
    public void update_error1() throws Exception {
        // given
        PriceEntity priceEntity = PriceEntity.builder()
                .expertNo("expert-123")
                .priceThemeType(PriceThemeType.FASHION)
                .snapSubTheme(null)
                .build();
        priceJpaRepository.save(priceEntity);

        priceEntity.updateEntity(PriceThemeType.SNAP, null);
        // when // then
        assertThatThrownBy(() -> priceJpaRepository.flush())
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP 테마일 경우 snapSubTheme를 반드시 입력해야 합니다.");
    }

    @Test
    @DisplayName("Price를 업데이트할 때, SNAP 테마가 아닌데 snapSubTheme가 있으면 에러가 발생한다.")
    public void update_error2() throws Exception {
        // given
        PriceEntity priceEntity = PriceEntity.builder()
                .expertNo("expert-123")
                .priceThemeType(PriceThemeType.FASHION)
                .snapSubTheme(null)
                .build();
        priceJpaRepository.save(priceEntity);
        priceEntity.updateEntity(PriceThemeType.BEAUTY, SnapSubTheme.ADMISSION);

        // when // then
        assertThatThrownBy(() -> priceJpaRepository.flush())
                .isInstanceOf(InvalidDataAccessApiUsageException.class)
                .hasMessage("SNAP이 아닌데 snapSubTheme가 들어있을 수 없습니다.");
    }

}