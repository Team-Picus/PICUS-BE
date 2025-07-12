package com.picus.core.expert.application.port.in;

import com.picus.core.expert.domain.model.Expert;

public interface RequestApprovalUseCase {

    Expert requestApproval(RequestApprovalRequest requestApprovalRequest);

}
