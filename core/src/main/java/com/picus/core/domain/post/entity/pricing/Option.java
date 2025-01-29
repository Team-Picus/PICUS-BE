package com.picus.core.domain.post.entity.pricing;

import jakarta.persistence.*;

@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue
    @Column(name = "option_id")
    private Long id;

    @Column(name = "post_no")
    private Long postNo; // post id

    private String name;
    private Double price;
}
