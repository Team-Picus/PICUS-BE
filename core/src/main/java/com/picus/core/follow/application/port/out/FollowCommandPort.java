package com.picus.core.follow.application.port.out;

public interface FollowCommandPort {

    void save(String userNo, String expertNo);
    void delete(String userNo, String expertNo);

}
