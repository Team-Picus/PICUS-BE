package com.picus.core.reservation.application.service;

import com.picus.core.price.application.port.out.OptionReadPort;
import com.picus.core.price.application.port.out.PackageReadPort;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.reservation.application.port.in.SendReservationRequestUseCase;
import com.picus.core.reservation.application.port.in.request.SaveReservationCommand;
import com.picus.core.reservation.application.port.in.request.SaveReservationCommand.OptionSelection;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SendReservationRequestService implements SendReservationRequestUseCase {

    private final UserReadPort userReadPort;
    private final PriceReadPort priceReadPort;
    private final PackageReadPort packageReadPort;
    private final OptionReadPort optionReadPort;

    @Override
    public void send(String userNo, SaveReservationCommand command) {
        if (userReadPort.existsById(userNo))
            throw new RestApiException(_NOT_FOUND);

        Price selectedPrice = priceReadPort.findById(command.getPriceNo());
        Package selectedPackage = packageReadPort.findById(command.getPackageNo());

        List<String> optionNos = command.getSelectedOptions().stream()
                .map(OptionSelection::optionNo)
                .toList();

        List<Option> options = optionReadPort.findByIds(optionNos);




    }
}
