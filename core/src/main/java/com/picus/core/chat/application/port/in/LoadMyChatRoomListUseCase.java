package com.picus.core.chat.application.port.in;

import com.picus.core.chat.application.port.in.result.LoadMyChatRoomListResult;

import java.util.List;
import java.util.Optional;

public interface LoadMyChatRoomListUseCase {

    List<LoadMyChatRoomListResult> load(String currentUserNo);
}
