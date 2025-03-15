package com.picus.core.domain.post.domain.entity.pricing;

import com.picus.core.domain.post.domain.entity.Post;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasicOption {

    @Id
    @Tsid
    @Column(name = "basic_option_id")
    private Long id;

    private Integer basicPrice;

    @OneToOne
    @JoinColumn(name = "post_no")
    private Post post;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "basicOption")
    private List<AdditionalOption> additionalOptions = new ArrayList<>();

    // ======================================
    // =            Constructors            =
    // ======================================
    public BasicOption(Post post, Integer basicPrice) {
        this.post = post;
        this.basicPrice = basicPrice;
    }

    // ======================================
    // =          Business methods          =
    // ======================================
    public boolean addAdditionalOption(String name,
                                       Integer pricePerUnit,
                                       Integer max,
                                       Integer base,
                                       Integer increment) {
        additionalOptions.add(
                new AdditionalOption(this,
                        name,
                        pricePerUnit,
                        max,
                        base,
                        increment)
        );

        return true;
    }

    public boolean removeAdditionalOption(Long id) {
        additionalOptions.removeIf(option -> option.getId().equals(id));

        return true;
    }

}
