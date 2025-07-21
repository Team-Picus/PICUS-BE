package com.picus.core.price.application.service;

import com.picus.core.expert.domain.model.vo.PriceThemeType;
import com.picus.core.price.application.port.in.ApplyPriceChangesUseCase;
import com.picus.core.price.application.port.in.command.*;
import com.picus.core.price.application.port.mapper.OptionCommandAppMapper;
import com.picus.core.price.application.port.mapper.PackageCommandAppMapper;
import com.picus.core.price.application.port.mapper.PriceCommandAppMapper;
import com.picus.core.price.application.port.mapper.PriceRefImageCommandAppMapper;
import com.picus.core.price.application.port.out.PriceCommandPort;
import com.picus.core.price.application.port.out.PriceQueryPort;
import com.picus.core.price.domain.model.Price;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class ApplyPriceChangesService implements ApplyPriceChangesUseCase {

    private final UserQueryPort userQueryPort;
    private final PriceQueryPort priceQueryPort;
    private final PriceCommandPort priceCommandPort;

    private final PriceCommandAppMapper priceCommandAppMapper;
    private final PriceRefImageCommandAppMapper priceRefImageCommandAppMapper;
    private final PackageCommandAppMapper packageCommandAppMapper;
    private final OptionCommandAppMapper optionCommandAppMapper;

    @Override
    public void apply(ApplyPriceChangesCommand command, String currentUserNo) {
        // 현재 사용자의 ExpertNo을 받아옴
        User user = userQueryPort.findById(currentUserNo);
        String expertNo = Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        for (PriceCommand priceCommand : command.prices()) {
            switch (priceCommand.status()) {
                case ChangeStatus.NEW:
                    createPrice(priceCommand, expertNo);
                    break;
                case ChangeStatus.UPDATE:
                    updatePrice(priceCommand);
                    break;
                case ChangeStatus.DELETE:
                    deletePrice(priceCommand.priceNo());
                    break;
            }
        }
    }

    /**
     * private 메서드
     */
    // Price 저장
    private void createPrice(PriceCommand priceCommand, String expertNo) {
        Price price = priceCommandAppMapper.toPriceDomain(priceCommand);
        priceCommandPort.save(price, expertNo);
    }

    // Price 수정
    private void updatePrice(PriceCommand priceCommand) {
        Price price = priceQueryPort.findById(priceCommand.priceNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        // Price 정보 업데이트
        price.changePriceTheme(priceCommand.priceThemeType());

        // PriceReferenceImage 정보 업데이트
        updatePriceReferenceImage(priceCommand, price);

        // Package 업데이트
        updatePackage(priceCommand, price);

        // Option 업데이트
        updateOption(priceCommand, price);

        priceCommandPort.update(price);
    }

    // PriceReferenceImage 수정
    private void updatePriceReferenceImage(PriceCommand priceCommand, Price price) {
        List<PriceReferenceImageCommand> refImagesCommands = priceCommand.priceReferenceImages();
        for (PriceReferenceImageCommand refImagesCommand : refImagesCommands) {
            switch (refImagesCommand.status()) {
                case ChangeStatus.NEW:
                    price.addReferenceImage(priceRefImageCommandAppMapper.toDomain(refImagesCommand));
                    break;
                case ChangeStatus.UPDATE:
                    price.updateReferenceImage(priceRefImageCommandAppMapper.toDomain(refImagesCommand));
                    break;
                case ChangeStatus.DELETE:
                    price.deleteReferenceImage(refImagesCommand.priceRefImageNo());
                    break;
            }
        }
    }

    // Package 수정
    private void updatePackage(PriceCommand priceCommand, Price price) {
        List<PackageCommand> packageCommands = priceCommand.packages();
        for (PackageCommand pkgCmd : packageCommands) {
            switch (pkgCmd.status()) {
                case ChangeStatus.NEW:
                    price.addPackage(packageCommandAppMapper.toDomain(pkgCmd));
                    break;
                case ChangeStatus.UPDATE:
                    price.updatePackage(packageCommandAppMapper.toDomain(pkgCmd));
                    break;
                case ChangeStatus.DELETE:
                    price.deletePackage(pkgCmd.packageNo());
                    break;
            }
        }
    }

    // Option 수정
    private void updateOption(PriceCommand priceCommand, Price price) {
        List<OptionCommand> optionCommands = priceCommand.options();
        for (OptionCommand optCmd : optionCommands) {
            switch (optCmd.status()) {
                case ChangeStatus.NEW:
                    price.addOption(optionCommandAppMapper.toDomain(optCmd));
                    break;
                case ChangeStatus.UPDATE:
                    price.updateOption(optionCommandAppMapper.toDomain(optCmd));
                    break;
                case ChangeStatus.DELETE:
                    price.deleteOption(optCmd.optionNo());
                    break;
            }
        }
    }

    // Price 삭제
    private void deletePrice(String deletePriceNo) {
        priceCommandPort.delete(deletePriceNo);
    }
}
