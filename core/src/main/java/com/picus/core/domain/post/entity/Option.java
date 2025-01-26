package com.picus.core.domain.post.entity;

import jakarta.persistence.*;

@Entity
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
