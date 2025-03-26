package com.picus.core.domain.studio.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    private String zipCode;     // 우편번호
    private String baseAddress;    // 서울특별시 강남구 테헤란로 123
    private String detailAddress;    // 카카오 빌딩 2층

    public Address(String zipCode, String baseAddress, String detailAddress) {
        this.zipCode = zipCode;
        this.baseAddress = baseAddress;
        this.detailAddress = detailAddress;
    }
}
