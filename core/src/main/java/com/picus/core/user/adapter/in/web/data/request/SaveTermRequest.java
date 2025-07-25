package com.picus.core.user.adapter.in.web.data.request;

public record SaveTermRequest (
        String termNo,
        Boolean isAgreed
) {}
