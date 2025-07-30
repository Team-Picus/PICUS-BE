package com.picus.core.price.adapter.out.persistence.repository;

import com.picus.core.expert.domain.vo.PriceThemeType;
import com.picus.core.price.adapter.out.persistence.entity.PriceEntity;
import com.picus.core.price.adapter.out.persistence.entity.PriceReferenceImageEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PriceReferenceImageJpaRepositoryTest {

    @Autowired
    private PriceJpaRepository priceJpaRepository;
    @Autowired
    private PriceReferenceImageJpaRepository repository;

    @Autowired
    private TestEntityManager em; // 테스트용 EntityManager (검증용으로 사용)

    @Test
    void shiftAllImageOrdersToNegative_success() {
        // given

        PriceEntity priceEntity = PriceEntity.builder()
                .priceThemeType(PriceThemeType.FASHION)
                .expertNo("expert_no")
                .build();
        priceJpaRepository.save(priceEntity);
        String priceNo = priceEntity.getPriceNo();

        repository.save(PriceReferenceImageEntity.builder()
                .priceEntity(priceEntity)
                .fileKey("file-1")
                .imageOrder(1)
                .build());
        repository.save(PriceReferenceImageEntity.builder()
                .priceEntity(priceEntity)
                .fileKey("file-2")
                .imageOrder(2)
                .build());

        em.flush();
        em.clear();

        // when
        repository.shiftAllImageOrdersToNegative(priceNo);
        em.flush(); // 쿼리 반영
        em.clear(); // 영속성 컨텍스트 초기화 후 다시 조회

        // then
        List<PriceReferenceImageEntity> result = repository.findAllByPriceEntity_PriceNoOrderByImageOrder(priceNo);

        List<Integer> imageOrders = result.stream()
                .map(PriceReferenceImageEntity::getImageOrder)
                .toList();

        assertThat(imageOrders).containsExactlyInAnyOrder(-1, -2);
    }
}