package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.UpdatePriceCommand;
import com.picus.core.price.application.port.in.request.*;
import com.picus.core.price.application.port.in.mapper.UpdateOptionAppMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePackageAppMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePriceAppMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePriceRefImageAppMapper;
import com.picus.core.price.application.port.out.CreatePricePort;
import com.picus.core.price.application.port.out.DeletePricePort;
import com.picus.core.price.application.port.out.ReadPricePort;
import com.picus.core.price.application.port.out.UpdatePricePort;
import com.picus.core.price.domain.Price;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@RequiredArgsConstructor
@Transactional
public class UpdatePriceService implements UpdatePriceCommand {

    private final UserQueryPort userQueryPort;

    private final ReadPricePort readPricePort;
    private final CreatePricePort createPricePort;
    private final UpdatePricePort updatePricePort;
    private final DeletePricePort deletePricePort;

    private final UpdatePriceAppMapper updatePriceAppMapper;
    private final UpdatePriceRefImageAppMapper updatePriceRefImageAppMapper;
    private final UpdatePackageAppMapper updatePackageAppMapper;
    private final UpdateOptionAppMapper updateOptionAppMapper;

    @Override
    public void update(UpdatePriceListAppReq command, String currentUserNo) {
        // 현재 사용자의 ExpertNo을 받아옴
        User user = userQueryPort.findById(currentUserNo);
        String expertNo = Optional.ofNullable(user.getExpertNo())
                .orElseThrow(() -> new RestApiException(_FORBIDDEN));

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
                    updatePrice(updatePriceAppReq, expertNo);
                    break;
                case ChangeStatus.DELETE:
                    deletePrice(updatePriceAppReq.priceNo(), expertNo);
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
        Price price = updatePriceAppMapper.toPriceDomain(updatePriceAppReq);
        createPricePort.create(price, expertNo);
    }

    // Price 수정
    private void updatePrice(UpdatePriceAppReq updatePriceAppReq, String expertNo) {
        Price price = readPricePort.findById(updatePriceAppReq.priceNo());
        // 현재 사용자와 수정하는 Price의 사용자가 다른 경우 예외
        throwIfNotOwner(expertNo, price.getExpertNo());

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

        updatePricePort.update(price, deletedPriceRefImageNos, deletedPackageNos, deletedOptionNos);
    }

    // PriceReferenceImage 수정
    private void updatePriceReferenceImage(UpdatePriceAppReq updatePriceAppReq, Price price, List<String> deletedPriceRefImageNos) {
        List<UpdatePriceReferenceImageAppReq> refImagesCommands = updatePriceAppReq.priceReferenceImages();
        for (UpdatePriceReferenceImageAppReq refImagesCommand : refImagesCommands) {
            switch (refImagesCommand.status()) {
                case ChangeStatus.NEW:
                    price.addReferenceImage(updatePriceRefImageAppMapper.toDomain(refImagesCommand));
                    break;
                case ChangeStatus.UPDATE:
                    price.updateReferenceImage(updatePriceRefImageAppMapper.toDomain(refImagesCommand));
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
                    price.addPackage(updatePackageAppMapper.toDomain(pkgCmd));
                    break;
                case ChangeStatus.UPDATE:
                    price.updatePackage(updatePackageAppMapper.toDomain(pkgCmd));
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
                    price.addOption(updateOptionAppMapper.toDomain(optCmd));
                    break;
                case ChangeStatus.UPDATE:
                    price.updateOption(updateOptionAppMapper.toDomain(optCmd));
                    break;
                case ChangeStatus.DELETE:
                    price.deleteOption(optCmd.optionNo());
                    deletedOptionNos.add(optCmd.optionNo());
                    break;
            }
        }
    }

    // Price 삭제
    private void deletePrice(String deletePriceNo, String expertNo) {
        Price price = readPricePort.findById(deletePriceNo);
        // 현재 사용자와 수정하는 Price의 사용자가 다른 경우 예외
        throwIfNotOwner(expertNo, price.getExpertNo());

        deletePricePort.delete(deletePriceNo);
    }

    private void throwIfNotOwner(String expertNo, String priceExpertNo) {
        if(!expertNo.equals(priceExpertNo))
            throw new RestApiException(_FORBIDDEN);
    }
}
