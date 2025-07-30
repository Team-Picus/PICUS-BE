package com.picus.core.price.domain;

import com.picus.core.expert.domain.vo.PriceThemeType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@EqualsAndHashCode
public class Price {

    private String priceNo;

    private String expertNo;

    private PriceThemeType priceThemeType;
    private List<PriceReferenceImage> priceReferenceImages;
    private List<Package> packages;
    private List<Option> options;

    public void changePriceTheme(String priceThemeType) {
        if (priceThemeType != null)
            this.priceThemeType = PriceThemeType.valueOf(priceThemeType);
    }

    public void addReferenceImage(PriceReferenceImage newImage) {
        // 이뮤터블 리스트 방어
        if (!(this.priceReferenceImages instanceof ArrayList)) {
            this.priceReferenceImages = new ArrayList<>(this.priceReferenceImages);
        }

        this.priceReferenceImages.add(newImage);
    }
    public void updateReferenceImage(PriceReferenceImage changedImage) {
        // 이뮤터블 리스트 방어
        if (!(this.priceReferenceImages instanceof ArrayList)) {
            this.priceReferenceImages = new ArrayList<>(this.priceReferenceImages);
        }

        if (changedImage.getPriceRefImageNo() != null) {
            for (PriceReferenceImage referenceImage : this.priceReferenceImages) {
                if (changedImage.getPriceRefImageNo().equals(referenceImage.getPriceRefImageNo())) {
                    referenceImage.updatePriceReferenceImage(changedImage.getFileKey(), changedImage.getImageOrder());
                    break;
                }
            }
        }
    }
    public void deleteReferenceImage(String refImageNo) {
        if (!(this.priceReferenceImages instanceof ArrayList)) {
            this.priceReferenceImages = new ArrayList<>(this.priceReferenceImages);
        }

        if (refImageNo != null) {
            this.priceReferenceImages.removeIf(image ->
                    refImageNo.equals(image.getPriceRefImageNo()));
        }
    }

    // Package 관련 로직
    public void addPackage(Package newPackage) {
        // 이뮤터블 리스트 방어
        if (!(this.packages instanceof ArrayList)) {
            this.packages = new ArrayList<>(this.packages);
        }
        this.packages.add(newPackage);
    }

    public void updatePackage(Package changedPackage) {
        // 이뮤터블 리스트 방어
        if (!(this.packages instanceof ArrayList)) {
            this.packages = new ArrayList<>(this.packages);
        }
        if (changedPackage.getPackageNo() != null) {
            for (Package pkg : this.packages) {
                if (changedPackage.getPackageNo().equals(pkg.getPackageNo())) {
                    pkg.updatePackage(
                            changedPackage.getName(), changedPackage.getPrice(),
                            changedPackage.getContents(), changedPackage.getNotice());
                    break;
                }
            }
        }
    }

    public void deletePackage(String packageNo) {
        // 이뮤터블 리스트 방어
        if (!(this.packages instanceof ArrayList)) {
            this.packages = new ArrayList<>(this.packages);
        }
        if (packageNo != null) {
            this.packages.removeIf(pkg ->
                    packageNo.equals(pkg.getPackageNo())
            );
        }
    }


    // Option 관련 로직
    public void addOption(Option newOption) {
        // 이뮤터블 리스트 방어
        if (!(this.options instanceof ArrayList)) {
            this.options = new ArrayList<>(this.options);
        }
        this.options.add(newOption);
    }

    public void updateOption(Option changedOption) {
        // 이뮤터블 리스트 방어
        if (!(this.options instanceof ArrayList)) {
            this.options = new ArrayList<>(this.options);
        }
        if (changedOption.getOptionNo() != null) {
            for (Option opt : this.options) {
                if (changedOption.getOptionNo().equals(opt.getOptionNo())) {
                    // Option 엔티티 쪽에 정의된 updateOption(...) 호출
                    opt.updateOption(changedOption.getName(), changedOption.getCount(),
                            changedOption.getPrice(), changedOption.getContents());
                    break;
                }
            }
        }
    }

    public void deleteOption(String optionNo) {
        // 이뮤터블 리스트 방어
        if (!(this.options instanceof ArrayList)) {
            this.options = new ArrayList<>(this.options);
        }
        if (optionNo != null) {
            this.options.removeIf(opt ->
                    optionNo.equals(opt.getOptionNo())
            );
        }
    }
}
