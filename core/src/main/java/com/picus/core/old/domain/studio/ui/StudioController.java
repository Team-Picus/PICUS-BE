package com.picus.core.old.domain.studio.ui;

import com.picus.core.old.domain.studio.application.dto.request.StudioReq;
import com.picus.core.old.domain.studio.application.usecase.StudioInfoUseCase;
import com.picus.core.old.global.config.resolver.annotation.ExpertPrincipal;
import com.picus.core.old.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/studio")
public class StudioController {

    private final StudioInfoUseCase studioInfoUseCase;

    @PostMapping
    public void createStudio(@ExpertPrincipal UserPrincipal userPrincipal, @RequestBody StudioReq request) {
        studioInfoUseCase.save(userPrincipal.getUserId(), request);
    }
}
