package com.picus.core.domain.reservation.entity.order;

/**
 * 예약 내역을 저장하는 Value Entity
 *
 * 실제 Post에서 제시하는 BasePrice, Option 들을 가져오고 사용자가 얼만큼의 옵션을 선택했는지를 "COPY" 해서 저장해야한다.
 * 왜냐하면 Option 가격이 추후에 변동될 수도 있기 떄문
 */
public class SelectedAdditionalOption {

    // option capture
    private Integer pricePerUnit;

    private Integer base;

    private Integer increment;

    private String optionName;

    // client selection
    private Integer count;

    // result
    private Integer totalAmount;    // count * increment + base

    private Integer totalAdditionalPrice;   // pricePerUnit * count
}
