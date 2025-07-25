package com.picus.core.user.application.port.in.command;

import com.picus.core.shared.common.SelfValidating;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompleteProfileCommand extends SelfValidating<CompleteProfileCommand> {

    @NotBlank
    private final String userNo;

    @NotBlank
    private final String nickname;

    private final String tel;

    private final String email;

    public CompleteProfileCommand(String userNo, String nickname, String tel, String email) {
        this.userNo = userNo;
        this.nickname = nickname;
        this.tel = tel;
        this.email = email;
        this.validateSelf();
    }
}
