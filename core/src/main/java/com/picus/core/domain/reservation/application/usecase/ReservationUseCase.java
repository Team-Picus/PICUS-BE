package com.picus.core.domain.reservation.application.usecase;

import com.picus.core.domain.post.application.dto.response.PostDetailDto;
import com.picus.core.domain.post.application.usecase.PostUseCase;
import com.picus.core.domain.post.domain.service.PostService;
import com.picus.core.domain.reservation.application.dto.request.ReservationRequestDto;
import com.picus.core.domain.reservation.application.dto.request.SelectedAdditionalOptionRequest;
import com.picus.core.domain.reservation.domain.entity.Reservation;
import com.picus.core.domain.reservation.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationUseCase {

    private final PostUseCase postUseCase;
    private final ReservationService reservationService;

    @Transactional
    public void createReservation(Long userId, Long postId) {
        PostDetailDto postDetail = postUseCase.findPostDetail(postId, false);
        reservationService.createReservation(userId, postId, postDetail.basicOption().basicPrice());
    }

    @Transactional
    public void registerReservation(Long userId, ReservationRequestDto reservationRequestDto) {
        // 1. Reservation 조회
        Reservation reservation = reservationService.findById(reservationRequestDto.reservationId());

        // 2. Reservation 의 사용자 일치 여부 확인
        if (!Objects.equals(reservation.getClientId(), userId)) {
            throw new IllegalArgumentException("해당 예약의 사용자와 일치하지 않습니다. reservationId: " + reservation.getId());
        }

        // 3. Reservation 등록
        Reservation registeredReservation = reservationService.registerReservation(reservation.getId(),
                reservationRequestDto.schedule(),
                reservationRequestDto.detail());

        // 4. 추가 옵션 등록
        for (SelectedAdditionalOptionRequest request : reservationRequestDto.selectedOptions()) {
            reservationService.addAdditionalOption(registeredReservation.getId(), request.additionalOptionId(), request.count());
        }
    }
}
