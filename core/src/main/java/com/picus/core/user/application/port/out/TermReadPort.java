package com.picus.core.user.application.port.out;

import com.picus.core.user.domain.model.Term;

import java.util.List;

public interface TermReadPort {

    List<Term> findAll();
    Term findById(String termNo);

}
