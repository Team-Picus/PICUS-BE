package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.shared.exception.RestApiException;
import com.picus.core.user.application.port.in.TermManagementUseCase;
import com.picus.core.user.application.port.in.command.SaveTermCommand;
import com.picus.core.user.application.port.out.TermCommandPort;
import com.picus.core.user.application.port.out.TermQueryPort;
import com.picus.core.user.application.port.out.UserQueryPort;
import com.picus.core.user.domain.model.Term;
import com.picus.core.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus.REQUIRED_TERM_NOT_AGREED;
import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@UseCase
@Transactional
@RequiredArgsConstructor
public class TermManagementService implements TermManagementUseCase {

    private final TermQueryPort termQueryPort;
    private final TermCommandPort termCommandPort;
    private final UserQueryPort userQueryPort;

    @Override
    public List<Term> getTerms() {
        return termQueryPort.findAll();
    }

    @Override
    public void approve(String userNo, List<SaveTermCommand> commands) {
        if (!userQueryPort.existsById(userNo))
            throw new RestApiException(_NOT_FOUND);

        for (SaveTermCommand command : commands) {
            Term term = termQueryPort.findById(command.getTermNo());

            if (term.getIsRequired() && !command.getIsAgreed())
                throw new RestApiException(REQUIRED_TERM_NOT_AGREED);

            termCommandPort.save(userNo, command.getTermNo(), command.getIsAgreed());
        }
    }
}
