package com.picus.core.user.application.port.in.command;

import com.picus.core.shared.common.SelfValidating;
import com.picus.core.user.adapter.in.web.data.request.ApproveTermRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveUserTermCommand extends SelfValidating<ApproveTermRequest> {

    @NotNull
    private final String termNo;

    @NotNull
    private final Boolean isAgreed;

    public SaveUserTermCommand(String termNo, Boolean isAgreed) {
        this.termNo = termNo;
        this.isAgreed = isAgreed;
        this.validateSelf();
    }
}
