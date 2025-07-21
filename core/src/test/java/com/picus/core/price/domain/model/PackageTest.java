package com.picus.core.price.domain.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PackageTest {

    @Test
    @DisplayName("Package를 업데이트한다.")
    public void updatePackage() throws Exception {
        // given
        Package aPackage = Package.builder()
                .name("pkg_name")
                .price(10000)
                .contents(List.of("contents1"))
                .notice("ntc1")
                .build();

        // when
        aPackage.updatePackage("new_name", 20000, List.of("contents1", "contents2"), "new_ntc");

        // then
        Assertions.assertThat(aPackage)
                .extracting(
                        Package::getName,
                        Package::getPrice,
                        Package::getContents,
                        Package::getNotice
                ).containsExactlyInAnyOrder(
                        "new_name", 20000, List.of("contents1", "contents2"), "new_ntc"
                );
    }

}