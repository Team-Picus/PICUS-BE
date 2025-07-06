package com.picus.core.support.infra.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_support_answers")
public class CustomerSupportAnswerEntity {

    @Id @Tsid
    private String customerSupportAnswerNo;

    private String content;
}
