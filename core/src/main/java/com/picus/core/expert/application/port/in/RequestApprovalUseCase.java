package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.request.RequestApprovalCommand;

public interface RequestApprovalUseCase {

    void requestApproval(RequestApprovalCommand command);

}
