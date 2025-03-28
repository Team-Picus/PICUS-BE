package com.picus.core.domain.shared.category.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private Type type;

    private Category(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public static Category createForAdmin(String name, Type type) {
        return new Category(name, type);
    }
}
