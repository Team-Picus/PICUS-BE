package com.picus.core.domain.studio.ui;

import com.picus.core.domain.studio.application.dto.request.StudioReq;
import com.picus.core.global.config.resolver.annotation.ExpertPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/studio")
public class StudioController {

    @PostMapping
    public void createStudio(@ExpertPrincipal UserPrincipal userPrincipal, @RequestBody StudioReq request) {

    }
}
