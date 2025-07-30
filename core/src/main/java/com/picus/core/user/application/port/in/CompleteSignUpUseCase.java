package com.picus.core.user.application.port.in;

import com.picus.core.user.application.port.in.command.CompleteProfileCommand;

public interface CompleteSignUpUseCase {

    void complete(CompleteProfileCommand command);

}
