package com.picus.core.domain.reservation.entity.history;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "selected_option")
public class SelectedOption {

    @Id
    @Field(name = "reservation_no")
    private Long id;

    // Option
    private Long postNo;

    private String name;

    private Double price;

    @Field(name = "applied_discounts")
    private List<AppliedDiscount> appliedDiscounts = new ArrayList<>();
}
