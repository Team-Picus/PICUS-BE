package com.picus.core.domain.term.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(UserTermId.class)
public class UserTerm {

    @Id
    private Long user_no;

    @Id
    private Long term_id;

    public UserTerm(Long user_no, Long term_id) {
        this.user_no = user_no;
        this.term_id = term_id;
    }
}
