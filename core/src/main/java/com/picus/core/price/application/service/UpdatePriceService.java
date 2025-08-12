package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.UpdatePriceUseCase;
import com.picus.core.price.application.port.in.command.*;
import com.picus.core.price.application.port.in.mapper.UpdateOptionCommandMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePackageCommandMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePriceCommandMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePriceRefImageCommandMapper;
import com.picus.core.price.application.port.out.PriceCreatePort;
import com.picus.core.price.application.port.out.PriceDeletePort;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.application.port.out.PriceUpdatePort;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.picus.core.price.application.port.in.command.ChangeStatus.DELETE;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UpdatePriceService implements UpdatePriceUseCase {
    private final PriceReadPort priceReadPort;
    private final PriceCreatePort priceCreatePort;
    private final PriceUpdatePort priceUpdatePort;
    private final PriceDeletePort priceDeletePort;

    private final UpdatePriceCommandMapper updatePriceCommandMapper;
    private final UpdatePriceRefImageCommandMapper updatePriceRefImageCommandMapper;
    private final UpdatePackageCommandMapper updatePackageCommandMapper;
    private final UpdateOptionCommandMapper updateOptionCommandMapper;

    @Override
    public void update(UpdatePriceListCommand command, String currentUserNo) {

        List<UpdatePriceCommand> updatePriceCommands = command.prices();

        // 이미지 순서 체크
        checkPriceRefImageOrder(updatePriceCommands);

        // 추가/수정/삭제 진행
        for (UpdatePriceCommand updatePriceCommand : updatePriceCommands) {
            switch (updatePriceCommand.status()) {
                case ChangeStatus.NEW:
                    createPrice(updatePriceCommand, currentUserNo);
                    break;
                case ChangeStatus.UPDATE:
                    updatePrice(updatePriceCommand, currentUserNo);
                    break;
                case DELETE:
                    deletePrice(updatePriceCommand.priceNo(), currentUserNo);
                    break;
            }
        }
    }


    /**
     * private 메서드
     */

    private void checkPriceRefImageOrder(List<UpdatePriceCommand> updatePriceCommands) {
        for (UpdatePriceCommand updatePriceCommand : updatePriceCommands) {
            List<UpdatePriceReferenceImageCommand> priceRefImageCommands = updatePriceCommand.priceReferenceImages();
            Set<Integer> imageOrderSet = new HashSet<>();
            for (UpdatePriceReferenceImageCommand command : priceRefImageCommands) {
                if (!command.status().equals(DELETE) && !imageOrderSet.add(command.imageOrder())) {
                    throw new RestApiException(_BAD_REQUEST);
                }
            }
        }
    }

    // Price 저장
    private void createPrice(UpdatePriceCommand updatePriceCommand, String expertNo) {
        Price price = updatePriceCommandMapper.toPriceDomain(updatePriceCommand);
        priceCreatePort.create(price, expertNo);
    }

    // Price 수정
    private void updatePrice(UpdatePriceCommand updatePriceCommand, String expertNo) {
        Price price = priceReadPort.findById(updatePriceCommand.priceNo());
        // 현재 사용자와 수정하는 Price의 사용자가 다른 경우 예외
        throwIfNotOwner(expertNo, price.getExpertNo());

        List<String> deletedPriceRefImageNos = new ArrayList<>();
        List<String> deletedPackageNos = new ArrayList<>();
        List<String> deletedOptionNos = new ArrayList<>();

        // Price 정보 업데이트
        price.changePriceTheme(updatePriceCommand.priceThemeType(), updatePriceCommand.snapSubTheme());

        // PriceReferenceImage 정보 업데이트
        updatePriceReferenceImage(updatePriceCommand, price, deletedPriceRefImageNos);

        // Package 업데이트
        updatePackage(updatePriceCommand, price, deletedPackageNos);

        // Option 업데이트
        updateOption(updatePriceCommand, price, deletedOptionNos);

        priceUpdatePort.update(price, deletedPriceRefImageNos, deletedPackageNos, deletedOptionNos);
    }

    // PriceReferenceImage 수정
    private void updatePriceReferenceImage(UpdatePriceCommand updatePriceCommand, Price price, List<String> deletedPriceRefImageNos) {
        List<UpdatePriceReferenceImageCommand> refImagesCommands = updatePriceCommand.priceReferenceImages();
        for (UpdatePriceReferenceImageCommand refImagesCommand : refImagesCommands) {
            switch (refImagesCommand.status()) {
                case ChangeStatus.NEW:
                    price.addReferenceImage(updatePriceRefImageCommandMapper.toDomain(refImagesCommand));
                    break;
                case ChangeStatus.UPDATE:
                    price.updateReferenceImage(updatePriceRefImageCommandMapper.toDomain(refImagesCommand));
                    break;
                case DELETE:
                    price.deleteReferenceImage(refImagesCommand.priceRefImageNo());
                    deletedPriceRefImageNos.add(refImagesCommand.priceRefImageNo());
                    break;
            }
        }
    }

    // Package 수정
    private void updatePackage(UpdatePriceCommand updatePriceCommand, Price price, List<String> deletedPackageNos) {
        List<UpdatePackageCommand> updatePackageCommands = updatePriceCommand.packages();
        for (UpdatePackageCommand pkgCmd : updatePackageCommands) {
            switch (pkgCmd.status()) {
                case ChangeStatus.NEW:
                    price.addPackage(updatePackageCommandMapper.toDomain(pkgCmd));
                    break;
                case ChangeStatus.UPDATE:
                    price.updatePackage(updatePackageCommandMapper.toDomain(pkgCmd));
                    break;
                case DELETE:
                    price.deletePackage(pkgCmd.packageNo());
                    deletedPackageNos.add(pkgCmd.packageNo());
                    break;
            }
        }
    }

    // Option 수정
    private void updateOption(UpdatePriceCommand updatePriceCommand, Price price, List<String> deletedOptionNos) {
        List<UpdateOptionCommand> updateOptionCommands = updatePriceCommand.options();
        for (UpdateOptionCommand optCmd : updateOptionCommands) {
            switch (optCmd.status()) {
                case ChangeStatus.NEW:
                    price.addOption(updateOptionCommandMapper.toDomain(optCmd));
                    break;
                case ChangeStatus.UPDATE:
                    price.updateOption(updateOptionCommandMapper.toDomain(optCmd));
                    break;
                case DELETE:
                    price.deleteOption(optCmd.optionNo());
                    deletedOptionNos.add(optCmd.optionNo());
                    break;
            }
        }
    }

    // Price 삭제
    private void deletePrice(String deletePriceNo, String expertNo) {
        Price price = priceReadPort.findById(deletePriceNo);
        // 현재 사용자와 수정하는 Price의 사용자가 다른 경우 예외
        throwIfNotOwner(expertNo, price.getExpertNo());

        priceDeletePort.delete(deletePriceNo);
    }

    private void throwIfNotOwner(String expertNo, String priceExpertNo) {
        if(!expertNo.equals(priceExpertNo))
            throw new RestApiException(_FORBIDDEN);
    }
}
