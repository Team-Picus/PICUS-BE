package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.command.RequestApprovalRequest;

public interface RequestApprovalUseCase {

    void requestApproval(RequestApprovalRequest command);

}
