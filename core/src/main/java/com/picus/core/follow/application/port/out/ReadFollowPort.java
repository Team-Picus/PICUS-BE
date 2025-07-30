package com.picus.core.follow.application.port.out;

import com.picus.core.follow.domain.Follow;

import java.util.List;

public interface ReadFollowPort {

    Boolean existsById(String userNo, String expertNo);

    List<Follow> findByUserNo(String userNo);
}
