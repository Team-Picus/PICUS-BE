package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.ApproveTermUseCase;
import com.picus.core.user.application.port.in.command.SaveUserTermCommand;
import com.picus.core.user.application.port.out.UserTermCreatePort;
import com.picus.core.user.application.port.out.TermReadPort;
import com.picus.core.user.application.port.out.UserReadPort;
import com.picus.core.user.domain.model.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.REQUIRED_TERM_NOT_AGREED;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class ApproveTermService implements ApproveTermUseCase {

    private final TermReadPort termReadPort;
    private final UserTermCreatePort userTermCreatePort;
    private final UserReadPort userReadPort;

    @Override
    public void approve(String userNo, List<SaveUserTermCommand> commands) {
        if (!userReadPort.existsById(userNo))
            throw new RestApiException(_NOT_FOUND);

        for (SaveUserTermCommand command : commands) {
            Term term = termReadPort.findById(command.getTermNo());

            if (term.getIsRequired() && !command.getIsAgreed())
                throw new RestApiException(REQUIRED_TERM_NOT_AGREED);

            userTermCreatePort.create(userNo, command.getTermNo(), command.getIsAgreed());
        }
    }
}
