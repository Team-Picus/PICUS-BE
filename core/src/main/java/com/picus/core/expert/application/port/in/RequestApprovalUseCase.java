package com.picus.core.expert.application.port.in;

import com.picus.core.expert.application.port.in.request.RequestApprovalAppReq;

public interface RequestApprovalUseCase {

    void requestApproval(RequestApprovalAppReq command);

}
