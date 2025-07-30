package com.picus.core.user.adapter.in.web.mapper;

import com.picus.core.user.adapter.in.web.data.request.ApproveTermRequest;
import com.picus.core.user.adapter.in.web.data.response.LoadTermResponse;
import com.picus.core.user.application.port.in.command.SaveUserTermCommand;
import com.picus.core.user.domain.model.Term;
import org.springframework.stereotype.Component;

@Component
public class TermWebMapper {

    public LoadTermResponse toResponse(Term term) {
        return LoadTermResponse
                .builder()
                .termNo(term.getTermNo())
                .name(term.getName())
                .content(term.getContent())
                .isRequired(term.getIsRequired())
                .build();
    }

    public SaveUserTermCommand toCommand(ApproveTermRequest request) {
        return SaveUserTermCommand
                .builder()
                .termNo(request.termNo())
                .isAgreed(request.isAgreed())
                .build();
    }
}
