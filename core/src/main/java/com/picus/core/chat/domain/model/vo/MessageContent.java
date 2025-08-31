package com.picus.core.chat.domain.model.vo;


public sealed interface MessageContent
        permits TextContent, ImageContent, FileContent, ReservationContent {}