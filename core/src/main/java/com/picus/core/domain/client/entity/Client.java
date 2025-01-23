package com.picus.core.domain.client.entity;

import com.picus.core.domain.expert.entity.Area;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client {

    @Id
    @Column(name = "client_no")
    private Long id;

    private Area preferredArea;

    public Client(Area preferredArea) {
        this.preferredArea = preferredArea;
    }
}
