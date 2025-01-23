package com.picus.core.domain.tag.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostTagId implements Serializable {

    @EqualsAndHashCode.Include
    private Long user_no;

    @EqualsAndHashCode.Include
    private Long term_id;

    public PostTagId(Long user_no, Long term_id) {
        this.user_no = user_no;
        this.term_id = term_id;
    }
}
