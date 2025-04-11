package com.picus.core.domain.reservation.application.usecase;

import com.picus.core.domain.post.application.dto.response.PostDetailResponse;
import com.picus.core.domain.post.application.usecase.PostManagementUseCase;
import com.picus.core.domain.reservation.application.dto.request.*;
import com.picus.core.domain.reservation.domain.entity.Reservation;
import com.picus.core.domain.reservation.domain.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReservationUseCase {

    private final PostManagementUseCase postManagementUseCase;
    private final ReservationService reservationService;

    /**
     * 최초에 클라이언트가 예약을 위해 예약을 생성합니다.
     * @param userId
     * @param postId
     */
    @Transactional
    public void createReservation(Long userId, Long postId) {
        PostDetailResponse postDetail = postManagementUseCase.findPostDetail(postId, false);
        reservationService.createReservation(userId, postId, postDetail.basicOption().basicPrice());
    }

    /**
     * 클라이언트가 작성한 정보를 저장합니다.
     * @param userId
     * @param reservationRegister
     */
    @Transactional
    public void registerReservation(Long userId, ReservationRegister reservationRegister) {
        // 1. Reservation 조회
        Reservation reservation = reservationService.findById(reservationRegister.reservationId());

        // 2. Reservation 의 사용자 일치 여부 확인
        if (!Objects.equals(reservation.getClientId(), userId)) {
            throw new IllegalArgumentException("해당 예약의 사용자와 일치하지 않습니다. reservationId: " + reservation.getId());
        }

        // 3. Reservation 등록
        Reservation registeredReservation = reservationService.registerReservation(reservation.getId(),
                reservationRegister.schedule(),
                reservationRegister.detail());

        // 4. 추가 옵션 등록
        for (SelectedAdditionalOptionRegister request : reservationRegister.selectedOptions()) {
            reservationService.addAdditionalOption(registeredReservation.getId(), request.additionalOptionId(), request.count());
        }
    }

    @Transactional
    public void updateReservation(Long expertId, ReservationUpdate reservationUpdate) {
        // 1. Reservation 조회
        Reservation findReservation = reservationService.findById(reservationUpdate.reservationId());

        // 2. Reservation 의 전문가 일치 여부 확인
        Long postId = findReservation.getPostId();
        PostDetailResponse postDetail = postManagementUseCase.findPostDetail(postId, false);

        // TODO 3. PostDetailResponse의 stduioNo에 포함된 expertId와 주어진 expertId를 비교해서 전문가 일치여부 확인해야함.

        // 4. 클라이언트가 제시한 옵션 수정
        List<SelectedAdditionalOptionUpdate> selectedAdditionalOptionUpdates = reservationUpdate.selectedAdditionalOptions();
        for (SelectedAdditionalOptionUpdate selectedAdditionalOptionUpdate : selectedAdditionalOptionUpdates) {
            reservationService.changeAdditionalOption(findReservation.getId(),
                    selectedAdditionalOptionUpdate.selectedAdditionalOptionId(),
                    selectedAdditionalOptionUpdate.count());
        }

        // 5. 추가 비용 등록
        List<AdditionalFeeRegister> additionalFeeRegisters = reservationUpdate.additionalFeeRegisters();
        for (AdditionalFeeRegister additionalFeeRegister : additionalFeeRegisters) {
            reservationService.addAdditionalFee(findReservation.getId(),
                    additionalFeeRegister.feePrice(),
                    additionalFeeRegister.feeDetail());
        }

    }
}
