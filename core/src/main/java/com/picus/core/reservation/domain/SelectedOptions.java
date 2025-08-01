package com.picus.core.reservation.domain;

import java.util.List;

public class SelectedOptions {

    private String name;
    private Integer pricePerUnit;   // 유닛 당 가격
    private Integer unitSize;   // 유닛 개수
    private Integer orderCount;  // 주문 수
    private List<String> contents;

}