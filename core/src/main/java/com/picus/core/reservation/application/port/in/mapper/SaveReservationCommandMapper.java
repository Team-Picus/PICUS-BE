package com.picus.core.reservation.application.port.in.mapper;

import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.reservation.application.port.in.request.SaveReservationCommand;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.ReservationStatus;
import com.picus.core.reservation.domain.SelectedOption;
import com.picus.core.reservation.domain.SelectedPackage;
import com.picus.core.shared.exception.RestApiException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.PACKAGE_NOT_FOUND;

@Component
public class SaveReservationCommandMapper {

    public Reservation toDomain(String userNo, SaveReservationCommand command, Price price) {
        return Reservation.builder()
                .reservationStatus(ReservationStatus.REQUESTED)
                .place(command.getPlace())
                .startTime(command.getStartTime())
                .themeType(price.getPriceThemeType())
                .requestDetail(command.getRequestDetail())
                .selectedPackage(toSelectedPackage(price, command))
                .selectedOptions(toSelectedOptions(price, command))
                .totalPrice(toTotalPrice(price, command))
                .userNo(userNo)
                .expertNo(price.getExpertNo())
                .build();

    }

    private Integer toTotalPrice(Price price, SaveReservationCommand command) {
        int packagePrice = price.getPackages().stream()
                .filter(p -> p.getPackageNo().equals(command.getPackageNo()))
                .findFirst()
                .map(Package::getPrice)
                .orElseThrow(() -> new RestApiException(PACKAGE_NOT_FOUND));

        int optionsTotal = 0;
        for (SaveReservationCommand.OptionSelection sel : command.getOptionSelection()) {
            for (Option option : price.getOptions()) {
                if (option.getOptionNo().equals(sel.optionNo())) {
                    optionsTotal += option.getPrice() * sel.count();
                }
            }
        }

        return packagePrice + optionsTotal;
    }


    private SelectedPackage toSelectedPackage(Price price, SaveReservationCommand command) {
        return price.getPackages().stream()
                .filter(p -> p.getPackageNo().equals(command.getPackageNo()))
                .findFirst()
                .map(p -> SelectedPackage.builder()
                        .name(p.getName())
                        .price(p.getPrice())
                        .contents(p.getContents())
                        .notice(p.getNotice())
                        .build())
                .orElseThrow(() -> new RestApiException(PACKAGE_NOT_FOUND));
    }

    private List<SelectedOption> toSelectedOptions(Price price, SaveReservationCommand command) {
        return command.getOptionSelection().stream()
                .flatMap(sel -> price.getOptions().stream()
                        .filter(o -> o.getOptionNo().equals(sel.optionNo()))
                        .map(o -> SelectedOption.builder()
                                .name(o.getName())
                                .unitSize(o.getCount())
                                .pricePerUnit(o.getPrice())
                                .orderCount(sel.count())
                                .contents(o.getContents())
                                .build()))
                .toList();
    }
}
