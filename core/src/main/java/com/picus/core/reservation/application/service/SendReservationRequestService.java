package com.picus.core.reservation.application.service;

import com.picus.core.post.application.port.out.PostReadPort;
import com.picus.core.post.domain.Post;
import com.picus.core.price.application.port.out.PriceReadPort;
import com.picus.core.price.domain.Price;
import com.picus.core.reservation.application.port.in.SendReservationRequestUseCase;
import com.picus.core.reservation.application.port.in.mapper.SaveReservationCommandMapper;
import com.picus.core.reservation.application.port.in.request.SaveReservationCommand;
import com.picus.core.reservation.application.port.out.ReservationCreatePort;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.out.UserReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.*;

@UseCase
@Transactional
@RequiredArgsConstructor
public class SendReservationRequestService implements SendReservationRequestUseCase {

    private final UserReadPort userReadPort;
    private final PostReadPort postReadPort;
    private final PriceReadPort priceReadPort;
    private final ReservationCreatePort reservationCreatePort;
    private final SaveReservationCommandMapper saveReservationCommandMapper;

    @Override
    public void send(String userNo, SaveReservationCommand command) {
        if (!userReadPort.existsById(userNo))
            throw new RestApiException(_NOT_FOUND);

        Price price = priceReadPort.findById(command.getPriceNo());

        if (userNo.equals(price.getExpertNo()))
            throw new RestApiException(SELF_REQUEST_NOT_ALLOWED);

        Post post = postReadPort.findById(command.getPostNo())
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        Reservation reservation = saveReservationCommandMapper.toDomain(userNo, command, price, post);

        reservationCreatePort.create(reservation);
    }
}
