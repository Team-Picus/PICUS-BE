package com.picus.core.chat.application.port.in;

import com.picus.core.chat.application.port.in.result.SearchMyChatRoomResult;

import java.util.List;

public interface SearchMyChatRoomUseCase {

    List<SearchMyChatRoomResult> search(String currentUserNo, String keyword);
}
