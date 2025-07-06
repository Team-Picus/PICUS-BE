package com.picus.core.support.infra.adapter.out.persistence.entity;

import com.picus.core.support.domain.model.vo.AnswerStatus;
import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.*;

@Entity
@Table(name = "customer_supports")
public class CustomerSupportEntity {

    @Id @Tsid
    private String customerSupportNo;

    private String title;
    private String content;
    @Enumerated(EnumType.STRING)
    private AnswerStatus answerStatus;
}
