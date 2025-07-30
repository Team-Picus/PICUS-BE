package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.request.*;
import com.picus.core.price.application.port.in.mapper.UpdateOptionAppMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePackageAppMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePriceAppMapper;
import com.picus.core.price.application.port.in.mapper.UpdatePriceRefImageAppMapper;
import com.picus.core.price.application.port.out.PriceCreatePort;
import com.picus.core.price.application.port.out.PriceDeletePort;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.application.port.out.PriceUpdatePort;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.price.domain.PriceReferenceImage;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UpdatePriceServiceTest {

    @Mock private UserReadPort userReadPort;

    @Mock private PriceReadPort priceReadPort;
    @Mock private PriceCreatePort priceCreatePort;
    @Mock private PriceUpdatePort priceUpdatePort;
    @Mock private PriceDeletePort priceDeletePort;

    @Mock private UpdatePriceAppMapper updatePriceAppMapper;
    @Mock private UpdatePriceRefImageAppMapper updatePriceRefImageAppMapper;
    @Mock private UpdatePackageAppMapper updatePackageAppMapper;
    @Mock private UpdateOptionAppMapper updateOptionAppMapper;

    private UpdatePriceService service;

    @BeforeEach
    void setUp() {
        service = new UpdatePriceService(
                userReadPort,
                priceReadPort,
                priceCreatePort,
                priceUpdatePort,
                priceDeletePort,
                updatePriceAppMapper,
                updatePriceRefImageAppMapper,
                updatePackageAppMapper,
                updateOptionAppMapper
        );
    }

    @Test
    @DisplayName("새로운 Price를 생성한다.")
    void updatePrice_shouldCreate_whenStatusIsNew() {
        // given
        String currentUserNo = "user-1";
        User user = mock(User.class);
        given(userReadPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-1");

        UpdatePriceAppReq cmd = new UpdatePriceAppReq(
                "price-1",
                "FASHION",
                Collections.emptyList(),  // no images
                Collections.emptyList(),  // no packages
                Collections.emptyList(),  // no options
                ChangeStatus.NEW
        );
        UpdatePriceListCommand command = new UpdatePriceListCommand(
                Collections.singletonList(cmd)
        );

        Price priceDomain = mock(Price.class);
        when(updatePriceAppMapper.toPriceDomain(cmd)).thenReturn(priceDomain);

        // when
        service.update(command, currentUserNo);

        // then: 순서대로 호출되었는지 검증
        InOrder inOrder = inOrder(userReadPort, user, updatePriceAppMapper, priceCreatePort);

        then(userReadPort).should(inOrder).findById(currentUserNo);
        then(user).should(inOrder).getExpertNo();
        then(updatePriceAppMapper).should(inOrder).toPriceDomain(cmd);
        then(priceCreatePort).should(inOrder).create(priceDomain, "expert-1");
        then(priceCreatePort).shouldHaveNoMoreInteractions();

    }

    @Test
    @DisplayName("기존 Price를 수정한다. PriceRefImage, Package, Option 정보가 없다면 해당 정보는 수정하지 않는다.")
    void updatePrice_shouldUpdatePrice_whenStatusIsUpdate() {
        // given
        String currentUserNo = "user-2";
        String currentExpertNo = "expert-2";
        User user = mock(User.class);
        given(userReadPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(currentExpertNo);

        UpdatePriceAppReq cmd = new UpdatePriceAppReq(
                "price-2",
                "FASHION",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.UPDATE
        );
        UpdatePriceListCommand command = new UpdatePriceListCommand(
                Collections.singletonList(cmd)
        );

        Price price = mock(Price.class);
        given(priceReadPort.findById("price-2")).willReturn(price);
        given(price.getExpertNo()).willReturn(currentExpertNo);

        // when
        service.update(command, currentUserNo);

        // then: 도메인 메서드 실행 순서 검증
        // 순서 검증을 위한 InOrder 객체 준비
        InOrder order = inOrder(userReadPort, user, priceReadPort, price, priceUpdatePort, priceCreatePort);

        then(userReadPort).should(order).findById(currentUserNo);
        then(user).should(order).getExpertNo();
        then(priceReadPort).should(order).findById("price-2");
        then(price).should(order).changePriceTheme("FASHION");
        then(priceUpdatePort).should(order).update(price, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        then(priceCreatePort).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("기존 Price를 수정할 때 이미지, 패키지, 옵션이 모두 반영된다")
    void updatePrice_updateWithAllChanges() {
        // given
        String currentUserNo = "user-4";
        String currentExpertNo = "expert-4";
        User user = mock(User.class);
        given(userReadPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(currentExpertNo);

        // 이미지 커맨드
        UpdatePriceReferenceImageCommand newImgCmd = createPriceRefImageCommand(null, "file-1", 1, ChangeStatus.NEW);
        UpdatePriceReferenceImageCommand updImgCmd = createPriceRefImageCommand("img-2", "file-2", 2, ChangeStatus.UPDATE);
        UpdatePriceReferenceImageCommand delImgCmd = createPriceRefImageCommand("img-3", "file-3", 3, ChangeStatus.DELETE);
        List<UpdatePriceReferenceImageCommand> imgCmds =
                List.of(newImgCmd, updImgCmd, delImgCmd);

        // 패키지 커맨드
        UpdatePackageCommand newPkgCmd = createPackageCommand(null, "PKG1", 1000, List.of("A", "B"), "note1", ChangeStatus.NEW);
        UpdatePackageCommand updPkgCmd = createPackageCommand("pkg-2", "PKG2", 2000, List.of("C"), "note2", ChangeStatus.UPDATE);
        UpdatePackageCommand delPkgCmd = createPackageCommand("pkg-3", "PKG3", 3000, List.of("D"), "note3", ChangeStatus.DELETE);
        List<UpdatePackageCommand> pkgCmds =
                List.of(newPkgCmd, updPkgCmd, delPkgCmd);

        // 옵션 커맨드
        UpdateOptionCommand newOptCmd = createOptionCommand(null, "OPT1", 1, 100, List.of("X"), ChangeStatus.NEW);
        UpdateOptionCommand updOptCmd = createOptionCommand("opt-2", "OPT2", 2, 200, List.of("Y"), ChangeStatus.UPDATE);
        UpdateOptionCommand delOptCmd = createOptionCommand("opt-3", "OPT3", 3, 300, List.of("Z"), ChangeStatus.DELETE);
        List<UpdateOptionCommand> optCmds =
                List.of(newOptCmd, updOptCmd, delOptCmd);

        UpdatePriceAppReq cmd = new UpdatePriceAppReq(
                "price-4",
                "FASHION",
                imgCmds,
                pkgCmds,
                optCmds,
                ChangeStatus.UPDATE
        );
        UpdatePriceListCommand command =
                new UpdatePriceListCommand(List.of(cmd));

        // 도메인 객체 및 매핑 설정
        Price price = mock(Price.class);
        given(priceReadPort.findById("price-4")).willReturn(price);
        given(price.getExpertNo()).willReturn(currentExpertNo);

        PriceReferenceImage imgNew = mock(PriceReferenceImage.class);
        PriceReferenceImage imgUpd = mock(PriceReferenceImage.class);
        given(updatePriceRefImageAppMapper.toDomain(newImgCmd)).willReturn(imgNew);
        given(updatePriceRefImageAppMapper.toDomain(updImgCmd)).willReturn(imgUpd);

        Package pkgNew = mock(Package.class);
        Package pkgUpd = mock(Package.class);
        given(updatePackageAppMapper.toDomain(newPkgCmd)).willReturn(pkgNew);
        given(updatePackageAppMapper.toDomain(updPkgCmd)).willReturn(pkgUpd);

        Option optNew = mock(Option.class);
        Option optUpd = mock(Option.class);
        given(updateOptionAppMapper.toDomain(newOptCmd)).willReturn(optNew);
        given(updateOptionAppMapper.toDomain(updOptCmd)).willReturn(optUpd);

        // when
        service.update(command, currentUserNo);

        // then: 순서 검증
        InOrder order = inOrder(
                userReadPort, user,
                priceReadPort, price,
                updatePriceRefImageAppMapper, price,
                updatePackageAppMapper, price,
                updateOptionAppMapper, price,
                priceUpdatePort, priceCreatePort
        );

        then(userReadPort).should(order).findById(currentUserNo);
        then(user).should(order).getExpertNo();
        then(priceReadPort).should(order).findById("price-4");
        then(price).should(order).changePriceTheme("FASHION");

        then(updatePriceRefImageAppMapper).should(order).toDomain(newImgCmd);
        then(price).should(order).addReferenceImage(imgNew);
        then(updatePriceRefImageAppMapper).should(order).toDomain(updImgCmd);
        then(price).should(order).updateReferenceImage(imgUpd);
        then(price).should(order).deleteReferenceImage("img-3");

        then(updatePackageAppMapper).should(order).toDomain(newPkgCmd);
        then(price).should(order).addPackage(pkgNew);
        then(updatePackageAppMapper).should(order).toDomain(updPkgCmd);
        then(price).should(order).updatePackage(pkgUpd);
        then(price).should(order).deletePackage("pkg-3");

        then(updateOptionAppMapper).should(order).toDomain(newOptCmd);
        then(price).should(order).addOption(optNew);
        then(updateOptionAppMapper).should(order).toDomain(updOptCmd);
        then(price).should(order).updateOption(optUpd);
        then(price).should(order).deleteOption("opt-3");

        then(priceUpdatePort).should(order).update(price, List.of("img-3"), List.of("pkg-3"), List.of("opt-3"));
        then(priceCreatePort).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Price를 수정할 때, 수정하려는 Price의 expertNo와 현재 expertNo가 다르면 예외가 발생한다.")
    void updatePrice_fail_when_updatedPrice_isNotEqual_currentExpertNo() {
        // given
        String currentUserNo = "user-2";
        String currentExpertNo = "expert-2";
        User user = mock(User.class);
        given(userReadPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(currentExpertNo);

        UpdatePriceAppReq cmd = new UpdatePriceAppReq(
                "price-2",
                "FASHION",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.UPDATE
        );
        UpdatePriceListCommand command = new UpdatePriceListCommand(
                Collections.singletonList(cmd)
        );

        Price price = mock(Price.class);
        given(priceReadPort.findById("price-2")).willReturn(price);
        given(price.getExpertNo()).willReturn("diff_expert_no");

        // when // then
        assertThatThrownBy(() -> service.update(command, currentUserNo))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Price를 추가/수정/삭제할 때, 이미지의 순서가 겹친다면 예외가 발생한다.")
    void update_image_order_wrong() {
        // given
        String currentUserNo = "user-4";
        User user = mock(User.class);
        given(userReadPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-4");

        // 이미지 커맨드
        UpdatePriceReferenceImageCommand newImgCmd1 = createPriceRefImageCommand(null, "file-1", 1, ChangeStatus.NEW);
        UpdatePriceReferenceImageCommand newImgCmd2 = createPriceRefImageCommand(null, "file-2", 1, ChangeStatus.NEW);
        List<UpdatePriceReferenceImageCommand> imgCmds =
                List.of(newImgCmd1, newImgCmd2);


        UpdatePriceAppReq cmd = new UpdatePriceAppReq(
                "price-4",
                "FASHION",
                imgCmds,
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.UPDATE
        );
        UpdatePriceListCommand command =
                new UpdatePriceListCommand(List.of(cmd));


        // when // then
        assertThatThrownBy(() -> service.update(command, currentUserNo))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Price를 삭제한다.")
    void updatePrice_shouldDelete_whenStatusIsDelete() {
        // given
        String currentUserNo = "user-3";
        String currentExpertNo = "expert-3";
        User user = mock(User.class);
        given(userReadPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(currentExpertNo);

        UpdatePriceAppReq cmd = new UpdatePriceAppReq(
                "price-3",
                "THEME_C",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.DELETE
        );
        UpdatePriceListCommand command = new UpdatePriceListCommand(
                Collections.singletonList(cmd)
        );

        Price price = mock(Price.class);
        given(priceReadPort.findById("price-3")).willReturn(price);
        given(price.getExpertNo()).willReturn(currentExpertNo);

        // when
        service.update(command, currentUserNo);

        // then
        then(priceReadPort).should().findById("price-3");
        then(priceDeletePort).should().delete("price-3");
    }

    @Test
    @DisplayName("Price를 삭제할 때, 삭제하려는 Price의 expertNo와 현재 expertNo가 다르면 예외가 발생한다.")
    void updatePrice_fail_when_deletedPriceExpertNo_isNotEqual_currentExpertNo() {
        // given
        String currentUserNo = "user-3";
        String currentExpertNo = "expert-3";
        User user = mock(User.class);
        given(userReadPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn(currentExpertNo);

        UpdatePriceAppReq cmd = new UpdatePriceAppReq(
                "price-3",
                "THEME_C",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.DELETE
        );
        UpdatePriceListCommand command = new UpdatePriceListCommand(
                Collections.singletonList(cmd)
        );

        Price price = mock(Price.class);
        given(priceReadPort.findById("price-3")).willReturn(price);
        given(price.getExpertNo()).willReturn("different_expert_no");

        // when then
        assertThatThrownBy(() -> service.update(command, currentUserNo))
                .isInstanceOf(RestApiException.class);
    }

    private UpdatePriceReferenceImageCommand createPriceRefImageCommand(String priceRefImageNo, String fileKey, int imageOrder, ChangeStatus changeStatus) {
        return UpdatePriceReferenceImageCommand.builder()
                .priceRefImageNo(priceRefImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .status(changeStatus)
                .build();
    }

    private UpdatePackageCommand createPackageCommand(String packageNo, String name, int price, List<String> contents, String notice, ChangeStatus changeStatus) {
        return UpdatePackageCommand.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .status(changeStatus)
                .build();
    }

    private UpdateOptionCommand createOptionCommand(String optionNo, String name, int count, int price, List<String> contents, ChangeStatus changeStatus) {
        return UpdateOptionCommand.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .status(changeStatus)
                .build();
    }
}