package com.picus.core.user.application.port.in;

import com.picus.core.user.domain.model.Provider;
import com.picus.core.user.domain.model.User;

public interface SocialAuthenticationUseCase {

    User authenticate(String providerId, Provider provider, String email);

}
