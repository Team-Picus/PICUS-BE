package com.picus.core.domain.user.entity.term;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserTermId implements Serializable {

    @EqualsAndHashCode.Include
    private Long user_no;

    @EqualsAndHashCode.Include
    private Long term_id;

    public UserTermId(Long user_no, Long term_id) {
        this.user_no = user_no;
        this.term_id = term_id;
    }
}
