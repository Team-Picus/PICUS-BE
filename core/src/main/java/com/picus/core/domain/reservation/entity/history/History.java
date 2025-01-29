package com.picus.core.domain.reservation.entity.history;


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
@Document(collection = "history")
public class History {

    @Id
    @Field(name = "reservation_no")
    private String id;

    @Field(name = "selected_options")
    private List<SelectedOption> selectedOptions = new ArrayList<>();
}
