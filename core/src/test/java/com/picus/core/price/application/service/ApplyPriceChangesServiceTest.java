package com.picus.core.price.application.service;

import com.picus.core.price.application.port.in.command.*;
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
import com.picus.core.user.application.port.out.ReadUserPort;
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
class ApplyPriceChangesServiceTest {

    @Mock private ReadUserPort readUserPort;
    @Mock private PriceQueryPort priceQueryPort;
    @Mock private PriceCommandPort priceCommandPort;
    @Mock private PriceCommandAppMapper priceCommandAppMapper;
    @Mock private PriceRefImageCommandAppMapper priceRefImageCommandAppMapper;
    @Mock private PackageCommandAppMapper packageCommandAppMapper;
    @Mock private OptionCommandAppMapper optionCommandAppMapper;

    private ApplyPriceChangesService service;

    @BeforeEach
    void setUp() {
        service = new ApplyPriceChangesService(
                readUserPort,
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
        given(readUserPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-1");

        PriceCommand cmd = new PriceCommand(
                "price-1",
                "FASHION",
                Collections.emptyList(),  // no images
                Collections.emptyList(),  // no packages
                Collections.emptyList(),  // no options
                ChangeStatus.NEW
        );
        ApplyPriceChangesCommand command = new ApplyPriceChangesCommand(
                Collections.singletonList(cmd)
        );

        Price priceDomain = mock(Price.class);
        when(priceCommandAppMapper.toPriceDomain(cmd)).thenReturn(priceDomain);

        // when
        service.apply(command, currentUserNo);

        // then: 순서대로 호출되었는지 검증
        InOrder inOrder = inOrder(readUserPort, user, priceCommandAppMapper, priceCommandPort);

        then(readUserPort).should(inOrder).findById(currentUserNo);
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
        given(readUserPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-2");

        PriceCommand cmd = new PriceCommand(
                "price-2",
                "FASHION",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.UPDATE
        );
        ApplyPriceChangesCommand command = new ApplyPriceChangesCommand(
                Collections.singletonList(cmd)
        );

        Price price = mock(Price.class);
        given(priceQueryPort.findById("price-2")).willReturn(price);

        // when
        service.apply(command, currentUserNo);

        // then: 도메인 메서드 실행 순서 검증
        // 순서 검증을 위한 InOrder 객체 준비
        InOrder order = inOrder(readUserPort, user, priceQueryPort, price, priceCommandPort);

        then(readUserPort).should(order).findById(currentUserNo);
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
        given(readUserPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-4");

        // 이미지 커맨드
        PriceReferenceImageCommand newImgCmd = createPriceRefImageCommand(null, "file-1", 1, ChangeStatus.NEW);
        PriceReferenceImageCommand updImgCmd = createPriceRefImageCommand("img-2", "file-2", 2, ChangeStatus.UPDATE);
        PriceReferenceImageCommand delImgCmd = createPriceRefImageCommand("img-3", "file-3", 3, ChangeStatus.DELETE);
        List<PriceReferenceImageCommand> imgCmds =
                List.of(newImgCmd, updImgCmd, delImgCmd);

        // 패키지 커맨드
        PackageCommand newPkgCmd = createPackageCommand(null, "PKG1", 1000, List.of("A", "B"), "note1", ChangeStatus.NEW);
        PackageCommand updPkgCmd = createPackageCommand("pkg-2", "PKG2", 2000, List.of("C"), "note2", ChangeStatus.UPDATE);
        PackageCommand delPkgCmd = createPackageCommand("pkg-3", "PKG3", 3000, List.of("D"), "note3", ChangeStatus.DELETE);
        List<PackageCommand> pkgCmds =
                List.of(newPkgCmd, updPkgCmd, delPkgCmd);

        // 옵션 커맨드
        OptionCommand newOptCmd = createOptionCommand(null, "OPT1", 1, 100, List.of("X"), ChangeStatus.NEW);
        OptionCommand updOptCmd = createOptionCommand("opt-2", "OPT2", 2, 200, List.of("Y"), ChangeStatus.UPDATE);
        OptionCommand delOptCmd = createOptionCommand("opt-3", "OPT3", 3, 300, List.of("Z"), ChangeStatus.DELETE);
        List<OptionCommand> optCmds =
                List.of(newOptCmd, updOptCmd, delOptCmd);

        PriceCommand cmd = new PriceCommand(
                "price-4",
                "FASHION",
                imgCmds,
                pkgCmds,
                optCmds,
                ChangeStatus.UPDATE
        );
        ApplyPriceChangesCommand command =
                new ApplyPriceChangesCommand(List.of(cmd));

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
                readUserPort, user,
                priceQueryPort, price,
                priceRefImageCommandAppMapper, price,
                packageCommandAppMapper, price,
                optionCommandAppMapper, price,
                priceCommandPort
        );

        then(readUserPort).should(order).findById(currentUserNo);
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
        given(readUserPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-4");

        // 이미지 커맨드
        PriceReferenceImageCommand newImgCmd1 = createPriceRefImageCommand(null, "file-1", 1, ChangeStatus.NEW);
        PriceReferenceImageCommand newImgCmd2 = createPriceRefImageCommand(null, "file-2", 1, ChangeStatus.NEW);
        List<PriceReferenceImageCommand> imgCmds =
                List.of(newImgCmd1, newImgCmd2);


        PriceCommand cmd = new PriceCommand(
                "price-4",
                "FASHION",
                imgCmds,
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.UPDATE
        );
        ApplyPriceChangesCommand command =
                new ApplyPriceChangesCommand(List.of(cmd));


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
        given(readUserPort.findById(currentUserNo)).willReturn(user);
        given(user.getExpertNo()).willReturn("expert-3");

        PriceCommand cmd = new PriceCommand(
                "price-3",
                "THEME_C",
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ChangeStatus.DELETE
        );
        ApplyPriceChangesCommand command = new ApplyPriceChangesCommand(
                Collections.singletonList(cmd)
        );

        // when
        service.apply(command, currentUserNo);

        // then: delete만 호출하고, update/create 관련 포트는 호출 안 함
        then(priceCommandPort).should().delete("price-3");
        then(priceCommandPort).shouldHaveNoMoreInteractions();
        then(priceQueryPort).shouldHaveNoInteractions();
    }

    private  PriceReferenceImageCommand createPriceRefImageCommand(String priceRefImageNo, String fileKey, int imageOrder, ChangeStatus changeStatus) {
        return PriceReferenceImageCommand.builder()
                .priceRefImageNo(priceRefImageNo)
                .fileKey(fileKey)
                .imageOrder(imageOrder)
                .status(changeStatus)
                .build();
    }

    private PackageCommand createPackageCommand(String packageNo, String name, int price, List<String> contents, String notice, ChangeStatus changeStatus) {
        return PackageCommand.builder()
                .packageNo(packageNo)
                .name(name)
                .price(price)
                .contents(contents)
                .notice(notice)
                .status(changeStatus)
                .build();
    }

    private OptionCommand createOptionCommand(String optionNo, String name, int count, int price, List<String> contents, ChangeStatus changeStatus) {
        return OptionCommand.builder()
                .optionNo(optionNo)
                .name(name)
                .count(count)
                .price(price)
                .contents(contents)
                .status(changeStatus)
                .build();
    }
}