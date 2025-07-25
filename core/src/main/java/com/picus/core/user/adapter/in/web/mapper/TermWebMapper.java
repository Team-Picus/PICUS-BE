package com.picus.core.user.adapter.in.web.mapper;

import com.picus.core.user.adapter.in.web.data.request.SaveTermRequest;
import com.picus.core.user.adapter.in.web.data.response.GetTermResponse;
import com.picus.core.user.application.port.in.command.SaveTermCommand;
import com.picus.core.user.domain.model.Term;
import org.springframework.stereotype.Component;

@Component
public class TermWebMapper {

    public GetTermResponse toResponse(Term term) {
        return GetTermResponse
                .builder()
                .termNo(term.getTermNo())
                .name(term.getName())
                .content(term.getContent())
                .isRequired(term.getIsRequired())
                .build();
    }

    public SaveTermCommand toCommand(SaveTermRequest request) {
        return SaveTermCommand
                .builder()
                .termNo(request.termNo())
                .isAgreed(request.isAgreed())
                .build();
    }
}
