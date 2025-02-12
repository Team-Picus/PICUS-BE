package com.picus.core.domain.post.entity.pricing;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class BasicOption {

    @Id @Tsid
    @Column(name = "basic_option_id")
    private Long id;

    private Integer basicPrice;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "basicOption")
    private List<AdditionalOption> additionalOptions;

}
