package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.request.*;
import com.picus.core.price.application.port.mapper.OptionCommandAppMapper;
import com.picus.core.price.application.port.mapper.PackageCommandAppMapper;
import com.picus.core.price.application.port.mapper.PriceCommandAppMapper;
import com.picus.core.price.application.port.mapper.PriceRefImageCommandAppMapper;
import com.picus.core.price.application.port.out.PriceCommandPort;
import com.picus.core.price.application.port.out.PriceQueryPort;
import com.picus.core.price.domain.model.Option;
import com.picus.core.price.domain.model.Package;
import com.picus.core.price.domain.model.Price;
import com.picus.core.price.domain.model.PriceReferenceImage;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PriceInfoCommandServiceTest {

    @Mock private UserQueryPort userQueryPort;
    @Mock private PriceQueryPort priceQueryPort;
    @Mock private PriceCommandPort priceCommandPort;
    @Mock private PriceCommandAppMapper priceCommandAppMapper;
    @Mock private PriceRefImageCommandAppMapper priceRefImageCommandAppMapper;
    @Mock private PackageCommandAppMapper packageCommandAppMapper;
    @Mock private OptionCommandAppMapper optionCommandAppMapper;

    private PriceInfoCommandService service;

    @BeforeEach
    void setUp() {
        service = new PriceInfoCommandService(
                userQueryPort,
                priceQueryPort,
                priceCommandPort,
                priceCommandAppMapper,
                priceRefImageCommandAppMapper,
                packageCommandAppMapper,
                optionCommandAppMapper
        );
    }

    @Test
    @DisplayName("새로운 Price를 생성한다.")
    void apply_shouldCreatePrice_whenStatusIsNew() {
        // given
        String currentUserNo = "user-1";
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-1");

        PriceCommandAppReq cmd = new PriceCommandAppReq(
                "price-1",
                "FASHION",
                Collections.emptyList(),  // no images
                Collections.emptyList(),  // no packages
                Collections.emptyList(),  // no options
                ChangeStatus.NEW
        );
        PriceInfoCommandAppReq command = new PriceInfoCommandAppReq(
                Collections.singletonList(cmd)
        );

        Price priceDomain = mock(Price.class);
        when(priceCommandAppMapper.toPriceDomain(cmd)).thenReturn(priceDomain);

        // when
        service.apply(command, currentUserNo);

        // then: 순서대로 호출되었는지 검증
        InOrder inOrder = inOrder(userQueryPort, user, priceCommandAppMapper, priceCommandPort);

        then(userQueryPort).should(inOrder).findById(currentUserNo);
        then(user).should(inOrder).getExpertNo();
        then(priceCommandAppMapper).should(inOrder).toPriceDomain(cmd);
        then(priceCommandPort).should(inOrder).save(priceDomain, "expert-1");
        then(priceCommandPort).shouldHaveNoMoreInteractions();

    }

    @Test
    @DisplayName("기존 Price를 수정한다. PriceRefImage, Package, Option 정보가 없다면 해당 정보는 수정하지 않는다.")
    void apply_shouldUpdatePrice_whenStatusIsUpdate() {
        // given
        String currentUserNo = "user-2";
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-2");

        PriceCommandAppReq cmd = new PriceCommandAppReq(
                "price-2",
                "FASHION",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.UPDATE
        );
        PriceInfoCommandAppReq command = new PriceInfoCommandAppReq(
                Collections.singletonList(cmd)
        );

        Price price = mock(Price.class);
        given(priceQueryPort.findById("price-2")).willReturn(price);

        // when
        service.apply(command, currentUserNo);

        // then: 도메인 메서드 실행 순서 검증
        // 순서 검증을 위한 InOrder 객체 준비
        InOrder order = inOrder(userQueryPort, user, priceQueryPort, price, priceCommandPort);

        then(userQueryPort).should(order).findById(currentUserNo);
        then(user).should(order).getExpertNo();
        then(priceQueryPort).should(order).findById("price-2");
        then(price).should(order).changePriceTheme("FASHION");
        then(priceCommandPort).should(order).update(price, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        then(priceCommandPort).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("기존 Price를 수정할 때 이미지, 패키지, 옵션이 모두 반영된다")
    void apply_updatePriceWithAllChanges() {
        // given
        String currentUserNo = "user-4";
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-4");

        // 이미지 커맨드
        PriceReferenceImageCommandAppReq newImgCmd = createPriceRefImageCommand(null, "file-1", 1, ChangeStatus.NEW);
        PriceReferenceImageCommandAppReq updImgCmd = createPriceRefImageCommand("img-2", "file-2", 2, ChangeStatus.UPDATE);
        PriceReferenceImageCommandAppReq delImgCmd = createPriceRefImageCommand("img-3", "file-3", 3, ChangeStatus.DELETE);
        List<PriceReferenceImageCommandAppReq> imgCmds =
                List.of(newImgCmd, updImgCmd, delImgCmd);

        // 패키지 커맨드
        PackageCommandAppReq newPkgCmd = createPackageCommand(null, "PKG1", 1000, List.of("A", "B"), "note1", ChangeStatus.NEW);
        PackageCommandAppReq updPkgCmd = createPackageCommand("pkg-2", "PKG2", 2000, List.of("C"), "note2", ChangeStatus.UPDATE);
        PackageCommandAppReq delPkgCmd = createPackageCommand("pkg-3", "PKG3", 3000, List.of("D"), "note3", ChangeStatus.DELETE);
        List<PackageCommandAppReq> pkgCmds =
                List.of(newPkgCmd, updPkgCmd, delPkgCmd);

        // 옵션 커맨드
        OptionCommandAppReq newOptCmd = createOptionCommand(null, "OPT1", 1, 100, List.of("X"), ChangeStatus.NEW);
        OptionCommandAppReq updOptCmd = createOptionCommand("opt-2", "OPT2", 2, 200, List.of("Y"), ChangeStatus.UPDATE);
        OptionCommandAppReq delOptCmd = createOptionCommand("opt-3", "OPT3", 3, 300, List.of("Z"), ChangeStatus.DELETE);
        List<OptionCommandAppReq> optCmds =
                List.of(newOptCmd, updOptCmd, delOptCmd);

        PriceCommandAppReq cmd = new PriceCommandAppReq(
                "price-4",
                "FASHION",
                imgCmds,
                pkgCmds,
                optCmds,
                ChangeStatus.UPDATE
        );
        PriceInfoCommandAppReq command =
                new PriceInfoCommandAppReq(List.of(cmd));

        // 도메인 객체 및 매핑 설정
        Price price = mock(Price.class);
        given(priceQueryPort.findById("price-4")).willReturn(price);

        PriceReferenceImage imgNew = mock(PriceReferenceImage.class);
        PriceReferenceImage imgUpd = mock(PriceReferenceImage.class);
        given(priceRefImageCommandAppMapper.toDomain(newImgCmd)).willReturn(imgNew);
        given(priceRefImageCommandAppMapper.toDomain(updImgCmd)).willReturn(imgUpd);

        Package pkgNew = mock(Package.class);
        Package pkgUpd = mock(Package.class);
        given(packageCommandAppMapper.toDomain(newPkgCmd)).willReturn(pkgNew);
        given(packageCommandAppMapper.toDomain(updPkgCmd)).willReturn(pkgUpd);

        Option optNew = mock(Option.class);
        Option optUpd = mock(Option.class);
        given(optionCommandAppMapper.toDomain(newOptCmd)).willReturn(optNew);
        given(optionCommandAppMapper.toDomain(updOptCmd)).willReturn(optUpd);

        // when
        service.apply(command, currentUserNo);

        // then: 순서 검증
        InOrder order = inOrder(
                userQueryPort, user,
                priceQueryPort, price,
                priceRefImageCommandAppMapper, price,
                packageCommandAppMapper, price,
                optionCommandAppMapper, price,
                priceCommandPort
        );

        then(userQueryPort).should(order).findById(currentUserNo);
        then(user).should(order).getExpertNo();
        then(priceQueryPort).should(order).findById("price-4");
        then(price).should(order).changePriceTheme("FASHION");

        then(priceRefImageCommandAppMapper).should(order).toDomain(newImgCmd);
        then(price).should(order).addReferenceImage(imgNew);
        then(priceRefImageCommandAppMapper).should(order).toDomain(updImgCmd);
        then(price).should(order).updateReferenceImage(imgUpd);
        then(price).should(order).deleteReferenceImage("img-3");

        then(packageCommandAppMapper).should(order).toDomain(newPkgCmd);
        then(price).should(order).addPackage(pkgNew);
        then(packageCommandAppMapper).should(order).toDomain(updPkgCmd);
        then(price).should(order).updatePackage(pkgUpd);
        then(price).should(order).deletePackage("pkg-3");

        then(optionCommandAppMapper).should(order).toDomain(newOptCmd);
        then(price).should(order).addOption(optNew);
        then(optionCommandAppMapper).should(order).toDomain(updOptCmd);
        then(price).should(order).updateOption(optUpd);
        then(price).should(order).deleteOption("opt-3");

        then(priceCommandPort).should(order).update(price, List.of("img-3"), List.of("pkg-3"), List.of("opt-3"));
        then(priceCommandPort).shouldHaveNoMoreInteractions();
    }

    @Test
    @DisplayName("Price를 추가/수정/삭제할 때, 이미지의 순서가 겹친다면 예외가 발생한다.")
    void apply_image_order_wrong() {
        // given
        String currentUserNo = "user-4";
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-4");

        // 이미지 커맨드
        PriceReferenceImageCommandAppReq newImgCmd1 = createPriceRefImageCommand(null, "file-1", 1, ChangeStatus.NEW);
        PriceReferenceImageCommandAppReq newImgCmd2 = createPriceRefImageCommand(null, "file-2", 1, ChangeStatus.NEW);
        List<PriceReferenceImageCommandAppReq> imgCmds =
                List.of(newImgCmd1, newImgCmd2);


        PriceCommandAppReq cmd = new PriceCommandAppReq(
                "price-4",
                "FASHION",
                imgCmds,
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.UPDATE
        );
        PriceInfoCommandAppReq command =
                new PriceInfoCommandAppReq(List.of(cmd));


        // when // then
        Assertions.assertThatThrownBy(() -> service.apply(command, currentUserNo))
                .isInstanceOf(RestApiException.class);
    }

    @Test
    @DisplayName("Price를 삭제한다.")
    void apply_shouldDeletePrice_whenStatusIsDelete() {
        // given
        String currentUserNo = "user-3";
        User user = mock(User.class);
        given(userQueryPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-3");

        PriceCommandAppReq cmd = new PriceCommandAppReq(
                "price-3",
                "THEME_C",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.DELETE
        );
        PriceInfoCommandAppReq command = new PriceInfoCommandAppReq(
                Collections.singletonList(cmd)
        );

        // when
        service.apply(command, currentUserNo);

        // then: delete만 호출하고, update/create 관련 포트는 호출 안 함
        then(priceCommandPort).should().delete("price-3");
        then(priceCommandPort).shouldHaveNoMoreInteractions();
        then(priceQueryPort).shouldHaveNoInteractions();
    }

    private PriceReferenceImageCommandAppReq createPriceRefImageCommand(String priceRefImageNo, String fileKey, int imageOrder, ChangeStatus changeStatus) {
        return PriceReferenceImageCommandAppReq.builder()
                .priceRefImageNo(priceRefImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .status(changeStatus)
                .build();
    }

    private PackageCommandAppReq createPackageCommand(String packageNo, String name, int price, List<String> contents, String notice, ChangeStatus changeStatus) {
        return PackageCommandAppReq.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .status(changeStatus)
                .build();
    }

    private OptionCommandAppReq createOptionCommand(String optionNo, String name, int count, int price, List<String> contents, ChangeStatus changeStatus) {
        return OptionCommandAppReq.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .status(changeStatus)
                .build();
    }
}