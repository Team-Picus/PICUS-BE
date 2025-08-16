package com.picus.core.reservation.application.port.out;

public interface ReservationBlacklistReadPort {

    boolean isBlacklist(String userNo);

}
