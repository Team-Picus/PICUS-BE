package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.domain.model.Package;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PackagePersistenceMapperTest {

    private final PackagePersistenceMapper mapper = new PackagePersistenceMapper();

    @Test
    void toDomain_success() {
        // given
        PackageEntity entity = PackageEntity.builder()
                .packageNo("pkg001")
                .name("기본 패키지")
                .price(15000)
                .contents(List.of("제공 항목 1", "제공 항목 2"))
                .notice("주의 사항 있음")
                .build();

        // when
        Package result = mapper.toDomain(entity);

        // then
        assertThat(result.getPackageNo()).isEqualTo("pkg001");
        assertThat(result.getName()).isEqualTo("기본 패키지");
        assertThat(result.getPrice()).isEqualTo(15000);
        assertThat(result.getContents()).isEqualTo(List.of("제공 항목 1", "제공 항목 2"));
        assertThat(result.getNotice()).isEqualTo("주의 사항 있음");
    }

    @Test
    @DisplayName("Package -> PackageEntity")
    public void toEntity() throws Exception {
        // given
        Package p = Package.builder()
                .name("기본 패키지")
                .price(15000)
                .contents(List.of("제공 항목 1", "제공 항목 2"))
                .notice("주의 사항 있음")
                .build();

        // when
        PackageEntity entity = mapper.toEntity(p);

        // then
        assertThat(entity.getName()).isEqualTo("기본 패키지");
        assertThat(entity.getPrice()).isEqualTo(15000);
        assertThat(entity.getContents()).isEqualTo(List.of("제공 항목 1", "제공 항목 2"));
        assertThat(entity.getNotice()).isEqualTo("주의 사항 있음");
    }
}