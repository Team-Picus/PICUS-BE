package com.picus.core.domain.client.application.usecase;

import com.picus.core.domain.client.application.dto.request.SignUpReq;
import com.picus.core.domain.client.domain.entity.Client;
import com.picus.core.domain.client.domain.entity.area.ClientDistrict;
import com.picus.core.domain.client.domain.service.ClientDistrictService;
import com.picus.core.domain.client.domain.service.ClientService;
import com.picus.core.domain.shared.area.domain.entity.District;
import com.picus.core.domain.shared.area.domain.service.AreaSearchService;
import com.picus.core.domain.user.domain.entity.User;
import com.picus.core.domain.user.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientInfoUseCase {

    private final AreaSearchService areaSearchService;
    private final ClientService clientService;
    private final ClientDistrictService clientDistrictService;
    private final UserService userService;

    @Transactional
    public Client save(Long userNo, SignUpReq request) {
        Client client = clientService.save(userNo);
        User user = userService.findById(userNo);
        user.updateProfile(request.nickname(), request.profileImgId());

        List<District> Districts = areaSearchService.findDistricts(request.preferredArea());
        List<ClientDistrict> clientDistricts = clientDistrictService.saveAll(client, Districts);
        client.updatePreferredArea(clientDistricts);

        return client;
    }
}
