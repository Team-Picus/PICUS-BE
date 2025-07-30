package com.picus.core.user.adapter.in.web.data.request;

public record ApproveTermRequest(
        String termNo,
        Boolean isAgreed
) {}
