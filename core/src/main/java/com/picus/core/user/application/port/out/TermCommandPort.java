package com.picus.core.user.application.port.out;

public interface TermCommandPort {

    void save(String userNo, String termNo, Boolean isAgreed);

}
