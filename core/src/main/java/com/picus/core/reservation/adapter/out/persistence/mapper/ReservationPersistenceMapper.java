package com.picus.core.reservation.adapter.out.persistence.mapper;

import com.picus.core.reservation.adapter.out.persistence.entity.ReservationEntity;
import com.picus.core.reservation.adapter.out.persistence.entity.vo.OptionSnapshot;
import com.picus.core.reservation.adapter.out.persistence.entity.vo.PackageSnapshot;
import com.picus.core.reservation.adapter.out.persistence.entity.vo.PostSnapshot;
import com.picus.core.reservation.domain.Reservation;
import com.picus.core.reservation.domain.SelectedOption;
import com.picus.core.reservation.domain.SelectedPackage;
import com.picus.core.reservation.domain.SelectedPost;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReservationPersistenceMapper {

    public ReservationEntity toEntity(Reservation reservation) {
        return ReservationEntity.builder()
                .reservationStatus(reservation.getReservationStatus())
                .place(reservation.getPlace())
                .startTime(reservation.getStartTime())
                .themeType(reservation.getThemeType())
                .requestDetail(reservation.getRequestDetail())
                .userNo(reservation.getUserNo())
                .expertNo(reservation.getExpertNo())
                .totalPrice(reservation.getTotalPrice())
                .optionSnapshots(toOptionSnapshot(reservation.getSelectedOptions()))
                .packageSnapshot(toPackageSnapshot(reservation.getSelectedPackage()))
                .postSnapshot(toPostSnapshot(reservation.getSelectedPost()))
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

    private PostSnapshot toPostSnapshot(SelectedPost selectedPost) {
        return PostSnapshot.builder()
                .title(selectedPost.getTitle())
                .expertName(selectedPost.getExpertName())
                .moods(selectedPost.getMoods())
                .themes(selectedPost.getThemes())
                .thumbnailImageKey(selectedPost.getThumbnailImageKey())
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
                .selectedPost(toSelectedPost(entity.getPostSnapshot()))
                .totalPrice(entity.getTotalPrice())
                .userNo(entity.getUserNo())
                .expertNo(entity.getExpertNo())
                .build();
    }

    private SelectedPost toSelectedPost(PostSnapshot postSnapshot) {
        return SelectedPost.builder()
                .title(postSnapshot.getTitle())
                .themes(postSnapshot.getThemes())
                .thumbnailImageKey(postSnapshot.getThumbnailImageKey())
                .expertName(postSnapshot.getExpertName())
                .moods(postSnapshot.getMoods())
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
