package com.picus.core.user.adapter.in.web.mapper;

import com.picus.core.user.adapter.in.web.data.request.SignUpRequest;
import com.picus.core.user.application.port.in.command.CompleteProfileCommand;
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
}
