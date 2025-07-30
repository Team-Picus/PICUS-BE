package com.picus.core.price.application.service;

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
import com.picus.core.user.application.port.out.ReadUserPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._BAD_REQUEST;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class ApplyPriceChangesService implements ApplyPriceChangesUseCase {

    private final ReadUserPort readUserPort;
    private final PriceQueryPort priceQueryPort;
    private final PriceCommandPort priceCommandPort;

    private final PriceCommandAppMapper priceCommandAppMapper;
    private final PriceRefImageCommandAppMapper priceRefImageCommandAppMapper;
    private final PackageCommandAppMapper packageCommandAppMapper;
    private final OptionCommandAppMapper optionCommandAppMapper;

    @Override
    public void apply(ApplyPriceChangesCommand command, String currentUserNo) {
        // 현재 사용자의 ExpertNo을 받아옴
        User user = readUserPort.findById(currentUserNo);
        String expertNo = Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        List<PriceCommand> priceCommands = command.prices();

        // 이미지 순서 체크
        checkPriceRefImageOrder(priceCommands);

        // 추가/수정/삭제 진행
        for (PriceCommand priceCommand : priceCommands) {
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

    private void checkPriceRefImageOrder(List<PriceCommand> priceCommands) {
        for (PriceCommand priceCommand : priceCommands) {
            List<PriceReferenceImageCommand> priceRefImageCommands = priceCommand.priceReferenceImages();
            Set<Integer> imageOrderSet = new HashSet<>();
            for (PriceReferenceImageCommand command : priceRefImageCommands) {
                if (!imageOrderSet.add(command.imageOrder())) {
                    throw new RestApiException(_BAD_REQUEST);
                }
            }
        }
    }

    // Price 저장
    private void createPrice(PriceCommand priceCommand, String expertNo) {
        Price price = priceCommandAppMapper.toPriceDomain(priceCommand);
        priceCommandPort.save(price, expertNo);
    }

    // Price 수정
    private void updatePrice(PriceCommand priceCommand) {
        Price price = priceQueryPort.findById(priceCommand.priceNo());
        List<String> deletedPriceRefImageNos = new ArrayList<>();
        List<String> deletedPackageNos = new ArrayList<>();
        List<String> deletedOptionNos = new ArrayList<>();

        // Price 정보 업데이트
        price.changePriceTheme(priceCommand.priceThemeType());

        // PriceReferenceImage 정보 업데이트
        updatePriceReferenceImage(priceCommand, price, deletedPriceRefImageNos);

        // Package 업데이트
        updatePackage(priceCommand, price, deletedPackageNos);

        // Option 업데이트
        updateOption(priceCommand, price, deletedOptionNos);

        priceCommandPort.update(price, deletedPriceRefImageNos, deletedPackageNos, deletedOptionNos);
    }

    // PriceReferenceImage 수정
    private void updatePriceReferenceImage(PriceCommand priceCommand, Price price, List<String> deletedPriceRefImageNos) {
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
                    deletedPriceRefImageNos.add(refImagesCommand.priceRefImageNo());
                    break;
            }
        }
    }

    // Package 수정
    private void updatePackage(PriceCommand priceCommand, Price price, List<String> deletedPackageNos) {
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
                    deletedPackageNos.add(pkgCmd.packageNo());
                    break;
            }
        }
    }

    // Option 수정
    private void updateOption(PriceCommand priceCommand, Price price, List<String> deletedOptionNos) {
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
                    deletedOptionNos.add(optCmd.optionNo());
                    break;
            }
        }
    }

    // Price 삭제
    private void deletePrice(String deletePriceNo) {
        priceCommandPort.delete(deletePriceNo);
    }
}
