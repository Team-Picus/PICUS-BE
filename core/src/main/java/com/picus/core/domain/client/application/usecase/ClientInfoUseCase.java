package com.picus.core.domain.client.application.usecase;

import com.picus.core.domain.client.application.dto.request.SignUpReq;
import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.client.domain.service.ClientService;
import com.picus.core.domain.user.domain.entity.User;
import com.picus.core.domain.user.domain.service.UserService;
import com.picus.core.global.utils.regex.BadWordFilterUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientInfoUseCase {

    private final ClientService clientService;
    private final UserService userService;
    private final BadWordFilterUtil badWordFilterUtil;

    @Transactional
    public Client save(Long userNo, SignUpReq request) {
        badWordFilterUtil.filterBadWord(request.nickname());

        Client client = clientService.save(userNo, request.preferredAreas());
        User user = userService.findById(userNo);
        user.updateProfile(request.nickname(), request.profileImgId());

        return client;
    }
}
