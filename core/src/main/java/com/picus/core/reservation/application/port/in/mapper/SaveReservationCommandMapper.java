package com.picus.core.reservation.application.port.in.mapper;

import com.picus.core.post.domain.Post;
import com.picus.core.post.domain.vo.PostMoodType;
import com.picus.core.post.domain.vo.PostThemeType;
import com.picus.core.price.domain.Option;
import com.picus.core.price.domain.Package;
import com.picus.core.price.domain.Price;
import com.picus.core.reservation.application.port.in.request.SaveReservationCommand;
import com.picus.core.reservation.domain.*;
import com.picus.core.shared.exception.RestApiException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.PACKAGE_NOT_FOUND;

@Component
public class SaveReservationCommandMapper {

    public Reservation toDomain(String userNo, SaveReservationCommand command, Price price, Post post) {
        return Reservation.builder()
                .reservationStatus(ReservationStatus.REQUESTED)
                .place(command.getPlace())
                .startTime(command.getStartTime())
                .themeType(price.getPriceThemeType().getText())
                .requestDetail(command.getRequestDetail())
                .selectedPackage(toSelectedPackage(price, command))
                .selectedOptions(toSelectedOptions(price, command))
                .totalPrice(toTotalPrice(price, command))
                .userNo(userNo)
                .expertNo(price.getExpertNo())
                .selectedPost(toSelectedPost(post))
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
                    optionsTotal += option.getPricePerUnit() * sel.count();
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
                                .unitSize(o.getUnitSize())
                                .pricePerUnit(o.getPricePerUnit())
                                .orderCount(sel.count())
                                .contents(o.getContents())
                                .build()))
                .toList();
    }

    private SelectedPost toSelectedPost(Post post) {
        return SelectedPost.builder()
                .themes(post.getPostThemeTypes().stream()
                        .map(PostThemeType::getText).toList()
                )
                .moods(post.getPostMoodTypes().stream()
                        .map(PostMoodType::getText).toList()
                )
                .expertName(post.getAuthorNo())
                .thumbnailImageKey(post.getFirstImage())
                .title(post.getTitle())
                .build();
    }
}
