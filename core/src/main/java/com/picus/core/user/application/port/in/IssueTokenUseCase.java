package com.picus.core.user.application.port.in;

import com.picus.core.user.application.port.in.result.IssueTokenResult;
import com.picus.core.user.domain.model.Role;

public interface IssueTokenUseCase {

    IssueTokenResult issue(String userNo, Role role);

}
