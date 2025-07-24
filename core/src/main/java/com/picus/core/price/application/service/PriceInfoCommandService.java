package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.PriceInfoCommand;
import com.picus.core.price.application.port.in.request.*;
import com.picus.core.price.application.port.in.mapper.OptionCommandAppMapper;
import com.picus.core.price.application.port.in.mapper.PackageCommandAppMapper;
import com.picus.core.price.application.port.in.mapper.PriceCommandAppMapper;
import com.picus.core.price.application.port.in.mapper.PriceRefImageCommandAppMapper;
import com.picus.core.price.application.port.out.PriceCommandPort;
import com.picus.core.price.application.port.out.PriceQueryPort;
import com.picus.core.price.domain.model.Price;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._BAD_REQUEST;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@RequiredArgsConstructor
@Transactional
public class PriceInfoCommandService implements PriceInfoCommand {

    private final UserQueryPort userQueryPort;
    private final PriceQueryPort priceQueryPort;
    private final PriceCommandPort priceCommandPort;

    private final PriceCommandAppMapper priceCommandAppMapper;
    private final PriceRefImageCommandAppMapper priceRefImageCommandAppMapper;
    private final PackageCommandAppMapper packageCommandAppMapper;
    private final OptionCommandAppMapper optionCommandAppMapper;

    @Override
    public void updatePrice(UpdatePriceListAppReq command, String currentUserNo) {
        // 현재 사용자의 ExpertNo을 받아옴
        User user = userQueryPort.findById(currentUserNo);
        String expertNo = Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        List<UpdatePriceAppReq> updatePriceAppReqs = command.prices();

        // 이미지 순서 체크
        checkPriceRefImageOrder(updatePriceAppReqs);

        // 추가/수정/삭제 진행
        for (UpdatePriceAppReq updatePriceAppReq : updatePriceAppReqs) {
            switch (updatePriceAppReq.status()) {
                case ChangeStatus.NEW:
                    createPrice(updatePriceAppReq, expertNo);
                    break;
                case ChangeStatus.UPDATE:
                    updatePrice(updatePriceAppReq);
                    break;
                case ChangeStatus.DELETE:
                    deletePrice(updatePriceAppReq.priceNo());
                    break;
            }
        }
    }


    /**
     * private 메서드
     */

    private void checkPriceRefImageOrder(List<UpdatePriceAppReq> updatePriceAppReqs) {
        for (UpdatePriceAppReq updatePriceAppReq : updatePriceAppReqs) {
            List<UpdatePriceReferenceImageAppReq> priceRefImageCommands = updatePriceAppReq.priceReferenceImages();
            Set<Integer> imageOrderSet = new HashSet<>();
            for (UpdatePriceReferenceImageAppReq command : priceRefImageCommands) {
                if (!imageOrderSet.add(command.imageOrder())) {
                    throw new RestApiException(_BAD_REQUEST);
                }
            }
        }
    }

    // Price 저장
    private void createPrice(UpdatePriceAppReq updatePriceAppReq, String expertNo) {
        Price price = priceCommandAppMapper.toPriceDomain(updatePriceAppReq);
        priceCommandPort.save(price, expertNo);
    }

    // Price 수정
    private void updatePrice(UpdatePriceAppReq updatePriceAppReq) {
        Price price = priceQueryPort.findById(updatePriceAppReq.priceNo());
        List<String> deletedPriceRefImageNos = new ArrayList<>();
        List<String> deletedPackageNos = new ArrayList<>();
        List<String> deletedOptionNos = new ArrayList<>();

        // Price 정보 업데이트
        price.changePriceTheme(updatePriceAppReq.priceThemeType());

        // PriceReferenceImage 정보 업데이트
        updatePriceReferenceImage(updatePriceAppReq, price, deletedPriceRefImageNos);

        // Package 업데이트
        updatePackage(updatePriceAppReq, price, deletedPackageNos);

        // Option 업데이트
        updateOption(updatePriceAppReq, price, deletedOptionNos);

        priceCommandPort.update(price, deletedPriceRefImageNos, deletedPackageNos, deletedOptionNos);
    }

    // PriceReferenceImage 수정
    private void updatePriceReferenceImage(UpdatePriceAppReq updatePriceAppReq, Price price, List<String> deletedPriceRefImageNos) {
        List<UpdatePriceReferenceImageAppReq> refImagesCommands = updatePriceAppReq.priceReferenceImages();
        for (UpdatePriceReferenceImageAppReq refImagesCommand : refImagesCommands) {
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
    private void updatePackage(UpdatePriceAppReq updatePriceAppReq, Price price, List<String> deletedPackageNos) {
        List<UpdatePackageAppReq> updatePackageAppReqs = updatePriceAppReq.packages();
        for (UpdatePackageAppReq pkgCmd : updatePackageAppReqs) {
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
    private void updateOption(UpdatePriceAppReq updatePriceAppReq, Price price, List<String> deletedOptionNos) {
        List<UpdateOptionAppReq> updateOptionAppReqs = updatePriceAppReq.options();
        for (UpdateOptionAppReq optCmd : updateOptionAppReqs) {
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
