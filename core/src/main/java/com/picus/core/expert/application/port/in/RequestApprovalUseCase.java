package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.command.RequestApprovalCommand;

public interface RequestApprovalUseCase {

    void requestApproval(RequestApprovalCommand command);

}
