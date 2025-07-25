package com.picus.core.user.adapter.in.web.mapper;

import com.picus.core.user.adapter.in.web.data.request.SignUpRequest;
import com.picus.core.user.adapter.in.web.data.response.GetProfileResponse;
import com.picus.core.user.application.port.in.command.CompleteProfileCommand;
import com.picus.core.user.domain.model.User;
import org.springframework.stereotype.Component;

@Component
public class SignUpWebMapper {

    public CompleteProfileCommand toCommand(String userNo, SignUpRequest request) {
        return CompleteProfileCommand
                .builder()
                .userNo(userNo)
                .nickname(request.nickname())
                .tel(request.tel())
                .build();
    }

    public GetProfileResponse toResponse(User user, String fileKey) {
        return GetProfileResponse
                .builder()
                .nickname(user.getNickname())
                .email(user.getEmail())
                .fileKey(fileKey)
                .reservationHistoryCount(user.getReservationHistoryCount())
                .followCount(user.getFollowCount())
                .myMoodboardCount(user.getMyMoodboardCount())
                .build();
    }
}
