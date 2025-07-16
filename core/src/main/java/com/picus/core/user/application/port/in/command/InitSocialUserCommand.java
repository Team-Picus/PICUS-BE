package com.picus.core.user.application.port.in.command;

import com.picus.core.shared.common.SelfValidating;
import com.picus.core.user.domain.model.Provider;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class InitSocialUserCommand extends SelfValidating<InitSocialUserCommand> {

    @NotBlank
    private final String providerId;

    @NotNull
    private final Provider provider;

    private final String email;

    @NotBlank
    private final String name;

    private final String tel;

    public InitSocialUserCommand(String providerId, Provider provider, String email, String name, String tel) {
        this.providerId = providerId;
        this.provider = provider;
        this.email = email;
        this.name = name;
        this.tel = tel;
        this.validateSelf();
    }
}
