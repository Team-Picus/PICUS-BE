package com.picus.core.user.application.port.in;

import com.picus.core.user.application.port.in.command.InitSocialUserCommand;
import com.picus.core.user.domain.model.User;

public interface SocialAuthenticationUseCase {

    User authenticate(InitSocialUserCommand command);

}
