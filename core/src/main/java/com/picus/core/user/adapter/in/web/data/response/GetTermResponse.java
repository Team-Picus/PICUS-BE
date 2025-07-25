package com.picus.core.user.adapter.in.web.data.response;

import lombok.Builder;

@Builder
public record GetTermResponse (
        String termNo,
        String name,
        String content,
        Boolean isRequired
) {}
