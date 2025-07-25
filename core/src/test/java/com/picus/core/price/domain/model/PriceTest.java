package com.picus.core.price.domain.model;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;


class PriceTest {

    @Test
    @DisplayName("Price의 PriceThemeType을 변경한다.")
    public void changePriceTheme_success() throws Exception {
        // given
        Price price = Price.builder()
                .priceThemeType(PriceThemeType.FASHION)
                .build();

        // when
        price.changePriceTheme("BEAUTY");

        // then
        assertThat(price.getPriceThemeType())
                .isEqualTo(PriceThemeType.BEAUTY);
    }

    @Test
    @DisplayName("잘못된 PriceThemeType Enum을 넘겨주면 오류가 발생한다.")
    public void changePriceTheme_wrong_enum() throws Exception {
        // given
        Price price = Price.builder()
                .priceThemeType(PriceThemeType.FASHION)
                .build();

        // then
        assertThatThrownBy(() -> price.changePriceTheme("WRONG"));
    }

    @Test
    @DisplayName("Price의 PriceReferenceImage를 추가한다.")
    public void addReferenceImage() throws Exception {
        // given
        Price price = Price.builder()
                .priceReferenceImages(List.of(createPriceReferenceImage("ref1", "file_1", 1))
                )
                .build();

        PriceReferenceImage newPriceReferenceImage = createPriceReferenceImage("ref2", "file_2", 2);
        // when
        price.addReferenceImage(newPriceReferenceImage);

        // then
        assertThat(price.getPriceReferenceImages()).hasSize(2)
                .extracting(
                        PriceReferenceImage::getPriceRefImageNo,
                        PriceReferenceImage::getFileKey,
                        PriceReferenceImage::getImageOrder
                )
                .containsExactlyInAnyOrder(
                        tuple("ref1", "file_1", 1),
                        tuple("ref2", "file_2", 2)
                );
    }

    @Test
    @DisplayName("Price의 기존 PriceReferenceImage를 수정한다.")
    public void updateReferenceImage() throws Exception {
        // given
        Price price = Price.builder()
                .priceReferenceImages(List.of(createPriceReferenceImage("ref1", "file_1", 1))
                )
                .build();

        PriceReferenceImage changedImage = createPriceReferenceImage("ref1", "new_file", 2);
        // when
        price.updateReferenceImage(changedImage);

        // then
        assertThat(price.getPriceReferenceImages()).hasSize(1)
                .extracting(
                        PriceReferenceImage::getPriceRefImageNo,
                        PriceReferenceImage::getFileKey,
                        PriceReferenceImage::getImageOrder
                )
                .containsExactlyInAnyOrder(
                        tuple("ref1", "new_file", 2)
                );
    }

    @Test
    @DisplayName("Price의 기존 PriceReferenceImage를 삭제한다.")
    public void deleteReferenceImage() throws Exception {
        // given
        Price price = Price.builder()
                .priceReferenceImages(List.of(createPriceReferenceImage("ref1", "file_1", 1))
                )
                .build();

        String deleteRefImageNo = "ref1";

        // when
        price.deleteReferenceImage(deleteRefImageNo);

        // then
        assertThat(price.getPriceReferenceImages()).isEmpty();
    }

    @Test
    @DisplayName("Price의 Package를 추가한다.")
    public void addPackage() throws Exception {

        // given
        Price price = Price.builder()
                .packages(List.of(
                        createPackage("pkg01", "pkg_name1", 10000, List.of("cont1"), "notice1")
                ))
                .build();

        Package newPackage = createPackage("pkg02", "pkg_name2", 20000, List.of("cont2"), "notice2");

        // when
        price.addPackage(newPackage);

        // then
        assertThat(price.getPackages()).hasSize(2)
                .extracting(
                        Package::getPackageNo,
                        Package::getName,
                        Package::getPrice,
                        Package::getContents,
                        Package::getNotice
                ).containsExactlyInAnyOrder(
                        tuple("pkg01", "pkg_name1", 10000, List.of("cont1"), "notice1"),
                        tuple("pkg02", "pkg_name2", 20000, List.of("cont2"), "notice2")
                );

    }

    @Test
    @DisplayName("Price의 특정 Package를 수정한다.")
    public void updatePackage() throws Exception {

        // given
        Price price = Price.builder()
                .packages(List.of(
                        createPackage("pkg01", "pkg_name1", 10000, List.of("cont1"), "notice1")
                ))
                .build();

        Package changedPackage = createPackage("pkg01", "chg_name", 20000, List.of("chg_content"), "chg_not");

        // when
        price.updatePackage(changedPackage);

        // then
        assertThat(price.getPackages()).hasSize(1)
                .extracting(
                        Package::getPackageNo,
                        Package::getName,
                        Package::getPrice,
                        Package::getContents,
                        Package::getNotice
                ).containsExactlyInAnyOrder(
                        tuple("pkg01", "chg_name", 20000, List.of("chg_content"), "chg_not")
                );

    }

    @Test
    @DisplayName("Price의 특정 Package를 삭제한다.")
    public void deletePackage() throws Exception {

        // given
        Price price = Price.builder()
                .packages(List.of(
                        createPackage("pkg01", "pkg_name1", 10000, List.of("cont1"), "notice1")
                ))
                .build();

        String deletePackageNo = "pkg01";

        // when
        price.deletePackage(deletePackageNo);

        // then
        assertThat(price.getPackages()).isEmpty();
    }

    @Test
    @DisplayName("Price의 Option을 추가한다.")
    public void addOption() throws Exception {
        // given
        Price price = Price.builder()
                .options(List.of(
                        createOption("opt_no1", "opt_name1", 3, 10000, List.of("content1"))
                )).build();

        Option newOption = createOption("opt_no2", "opt_name2", 5, 20000, List.of("content2"));

        // when
        price.addOption(newOption);

        // then
        assertThat(price.getOptions()).hasSize(2)
                .extracting(
                        Option::getOptionNo,
                        Option::getName,
                        Option::getCount,
                        Option::getPrice,
                        Option::getContents
                )
                .containsExactlyInAnyOrder(
                        tuple("opt_no1", "opt_name1", 3, 10000, List.of("content1")),
                        tuple("opt_no2", "opt_name2", 5, 20000, List.of("content2"))
                );
    }

    @Test
    @DisplayName("Price의 특정 Option을 수정한다.")
    public void updateOption() throws Exception {
        // given
        Price price = Price.builder()
                .options(List.of(
                        createOption("opt_no1", "opt_name1", 3, 10000, List.of("content1"))
                )).build();

        Option changedOption = createOption("opt_no1", "chg_name", 5, 20000, List.of("content1", "content2"));

        // when
        price.updateOption(changedOption);

        // then
        assertThat(price.getOptions()).hasSize(1)
                .extracting(
                        Option::getOptionNo,
                        Option::getName,
                        Option::getCount,
                        Option::getPrice,
                        Option::getContents
                )
                .containsExactlyInAnyOrder(
                        tuple("opt_no1", "chg_name", 5, 20000, List.of("content1", "content2"))
                );
    }

    @Test
    @DisplayName("Price의 특정 Option을 삭제한다.")
    public void deleteOption() throws Exception {
        // given
        Price price = Price.builder()
                .options(List.of(
                        createOption("opt_no1", "opt_name1", 3, 10000, List.of("content1"))
                )).build();

        String deleteOptionNo = "opt_no1";

        // when
        price.deleteOption(deleteOptionNo);

        // then
        assertThat(price.getOptions()).isEmpty();
    }


    private PriceReferenceImage createPriceReferenceImage(String priceRefImageNo, String fileKey, int imageOrder) {
        return PriceReferenceImage.builder()
                .priceRefImageNo(priceRefImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .build();
    }

    private Package createPackage(String packageNo, String name, int price, List<String> contents, String notice) {
        return Package.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .build();
    }

    private Option createOption(String optionNo, String name, int count, int price, List<String> contents) {
        return Option.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .build();
    }

}