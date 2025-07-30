package com.picus.core.user.application.port.in;

import com.picus.core.user.application.port.in.command.SaveUserTermCommand;
import com.picus.core.user.domain.model.Term;

import java.util.List;

public interface ApproveTermUseCase {

    void approve(String userNo, List<SaveUserTermCommand> commands);

}
