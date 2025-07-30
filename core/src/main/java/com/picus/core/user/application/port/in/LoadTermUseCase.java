package com.picus.core.user.application.port.in;

import com.picus.core.user.domain.model.Term;

import java.util.List;

public interface LoadTermUseCase {

    List<Term> load();

}
