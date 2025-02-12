package com.picus.core.domain.reservation.entity.history;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SelectedOption {

    private Long postNo;

    private String name;

    private Double price;

}
