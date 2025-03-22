package com.picus.core.domain.reservation.entity.order;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "selected_option")
public class SelectedOption {

    @Id
    @Field(name = "reservation_no")
    private Long id;

    @Field(name = "base_price")
    private Integer basePrice;

    @Field(name = "totalPrice")
    private Integer totalPrice;

    @Field(name = "additional_options")
    private List<SelectedAdditionalOption> additionalOptions = new ArrayList<>();
}
