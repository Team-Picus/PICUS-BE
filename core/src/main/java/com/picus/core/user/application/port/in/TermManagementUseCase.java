package com.picus.core.user.application.port.in;

import com.picus.core.user.application.port.in.command.SaveTermCommand;
import com.picus.core.user.domain.model.Term;

import java.util.List;

public interface TermManagementUseCase {

    List<Term> getTerms();

    void approve(String userNo, List<SaveTermCommand> commands);
}
