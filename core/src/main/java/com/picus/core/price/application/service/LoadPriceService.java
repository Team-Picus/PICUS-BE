package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.LoadPriceUseCase;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.vo.PriceThemeType;
import com.picus.core.price.domain.vo.SnapSubTheme;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.shared.exception.code.status.GlobalErrorStatus;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@UseCase
public class LoadPriceService implements LoadPriceUseCase {

    private final PriceReadPort priceReadPort;

    @Override
    public List<Price> loadByExpertNo(String expertNo, List<PriceThemeType> priceThemeTypes, List<SnapSubTheme> snapSubThemes) {

        if(priceThemeTypes != null) {
            validateSnapSubTheme(priceThemeTypes, snapSubThemes);
        }

        // 특정 ExpertNo를 가진 Price를 가져옴
        // TODO:PriceReferenceImage의 fileKey -> url 변환 필요
        return priceReadPort.findByExpertNoAndThemes(expertNo, priceThemeTypes, snapSubThemes);
    }

    private void validateSnapSubTheme(List<PriceThemeType> priceThemeTypes, List<SnapSubTheme> snapSubThemes) {
        boolean containsSnap = priceThemeTypes.contains(PriceThemeType.SNAP);
        boolean hasSubThemes = snapSubThemes != null && !snapSubThemes.isEmpty();

        // SNAP 테마가 설정되어 있으나 세부 테마(snapSubThemes)가 비어있는 경우
        if (containsSnap && !hasSubThemes) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }

        // SNAP 테마가 없는데 세부 테마(snapSubThemes)가 존재하는 경우
        if (!containsSnap && hasSubThemes) {
            throw new RestApiException(GlobalErrorStatus._BAD_REQUEST);
        }
    }
}
