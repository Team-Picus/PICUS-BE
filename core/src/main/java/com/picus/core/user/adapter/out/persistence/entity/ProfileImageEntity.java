package com.picus.core.user.adapter.out.persistence.entity;

import io.hypersistence.utils.hibernate.id.Tsid;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Builder
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ProfileImageEntity {

    @Id @Tsid
    private String profileImageNo;

    @Column(nullable = false)
    private String file_key;

    @Column(nullable = false)
    private String userNo;

    public void updateFileKey(String fileKey) {
        this.file_key = fileKey;
    }

}
