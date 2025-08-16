package com.picus.core.reservation.adapter.out.persistence.mapper;

import com.picus.core.reservation.adapter.out.persistence.entity.ReservationEntity;
import com.picus.core.reservation.adapter.out.persistence.entity.vo.OptionSnapshot;
import com.picus.core.reservation.adapter.out.persistence.entity.vo.PackageSnapshot;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.SelectedOption;
import com.picus.core.reservation.domain.SelectedPackage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationPersistenceMapper {

    public ReservationEntity toEntity(Reservation reservation) {
        return ReservationEntity.builder()
                .reservationStatus(reservation.getReservationStatus())
                .place(reservation.getPlace())
                .startTime(reservation.getStartTime())
                .requestDetail(reservation.getRequestDetail())
                .userNo(reservation.getUserNo())
                .expertNo(reservation.getExpertNo())
                .optionSnapshots(toOptionSnapshot(reservation.getSelectedOptions()))
                .packageSnapshot(toPackageSnapshot(reservation.getSelectedPackage()))
                .build();
    }

    private List<OptionSnapshot> toOptionSnapshot(List<SelectedOption> selectedOptions) {
        return selectedOptions.stream()
                .map(selectedOption -> OptionSnapshot.builder()
                        .name(selectedOption.getName())
                        .pricePerUnit(selectedOption.getPricePerUnit())
                        .unitSize(selectedOption.getUnitSize())
                        .orderCount(selectedOption.getOrderCount())
                        .contents(selectedOption.getContents())
                        .build())
                .toList();
    }

    private PackageSnapshot toPackageSnapshot(SelectedPackage selectedPackage) {
        return PackageSnapshot.builder()
                .name(selectedPackage.getName())
                .price(selectedPackage.getPrice())
                .contents(selectedPackage.getContents())
                .notice(selectedPackage.getNotice())
                .build();
    }

    public Reservation toDomain(ReservationEntity entity) {
        return Reservation.builder()
                .reservationNo(entity.getReservationNo())
                .reservationStatus(entity.getReservationStatus())
                .place(entity.getPlace())
                .startTime(entity.getStartTime())
                .themeType(entity.getThemeType())
                .requestDetail(entity.getRequestDetail())
                .selectedPackage(toSelectedPackage(entity.getPackageSnapshot()))
                .selectedOptions(toSelectedOptions(entity.getOptionSnapshots()))
                .totalPrice(entity.getTotalPrice())
                .userNo(entity.getUserNo())
                .expertNo(entity.getExpertNo())
                .build();
    }

    private List<SelectedOption> toSelectedOptions(List<OptionSnapshot> optionSnapshots) {
        return optionSnapshots.stream()
                .map(snapshot -> SelectedOption.builder()
                        .name(snapshot.getName())
                        .pricePerUnit(snapshot.getPricePerUnit())
                        .unitSize(snapshot.getUnitSize())
                        .orderCount(snapshot.getOrderCount())
                        .contents(snapshot.getContents())
                        .build())
                .toList();
    }

    private SelectedPackage toSelectedPackage(PackageSnapshot packageSnapshot) {
        return SelectedPackage.builder()
                .name(packageSnapshot.getName())
                .price(packageSnapshot.getPrice())
                .contents(packageSnapshot.getContents())
                .notice(packageSnapshot.getNotice())
                .build();
    }
}
