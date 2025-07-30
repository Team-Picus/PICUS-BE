package com.picus.core.user.application.port.out;

public interface UserTermCreatePort {

    void create(String userNo, String termNo, Boolean isAgreed);

}
