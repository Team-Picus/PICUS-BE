package com.picus.core.user.adapter.in.web.data.response;

public record GetTermResponse (
        String termNo,
        String name,
        String content,
        String isRequired
) {}
