package com.picus.core.domain;

import com.picus.core.global.config.resolver.annotation.CommonPrincipal;
import com.picus.core.global.config.resolver.annotation.ExpertPrincipal;
import com.picus.core.global.oauth.entity.UserPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/expert")
    public String expert(@ExpertPrincipal UserPrincipal userPrincipal) {
        return "expert";
    }

    @GetMapping("/client")
    public String client(UserPrincipal userPrincipal) {
        return "client";
    }

    @GetMapping("/common")
    public String common(@CommonPrincipal UserPrincipal userPrincipal) {
        return "common";
    }
}
