package com.picus.core.user.application.port.in;

public interface LogoutUseCase {

    void logout(String userNo, String token);

}
