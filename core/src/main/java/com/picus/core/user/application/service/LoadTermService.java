package com.picus.core.user.application.service;

import com.picus.core.shared.annotation.UseCase;
import com.picus.core.user.application.port.in.LoadTermUseCase;
import com.picus.core.user.application.port.out.TermReadPort;
import com.picus.core.user.domain.model.Term;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Transactional
@RequiredArgsConstructor
public class LoadTermService implements LoadTermUseCase {

    private final TermReadPort termReadPort;

    @Override
    public List<Term> load() {
        return termReadPort.findAll();
    }
}
