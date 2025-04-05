package com.picus.core.domain.reservation.ui;

import com.picus.core.domain.reservation.application.usecase.ReservationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;

//    @PostMapping
//    public ResponseEntity<>
}
