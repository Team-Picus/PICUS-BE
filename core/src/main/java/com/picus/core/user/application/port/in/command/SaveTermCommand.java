package com.picus.core.user.application.port.in.command;

import com.picus.core.shared.common.SelfValidating;
import com.picus.core.user.adapter.in.web.data.request.SaveTermRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SaveTermCommand extends SelfValidating<SaveTermRequest> {

    @NotNull
    private final String termNo;

    @NotNull
    private final Boolean isAgreed;

    public SaveTermCommand(String termNo, Boolean isAgreed) {
        this.termNo = termNo;
        this.isAgreed = isAgreed;
        this.validateSelf();
    }
}
