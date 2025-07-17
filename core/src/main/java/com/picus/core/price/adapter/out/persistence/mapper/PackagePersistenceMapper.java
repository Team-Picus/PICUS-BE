package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.domain.model.Package;
import org.springframework.stereotype.Component;

@Component
public class PackagePersistenceMapper {

    public Package toDomain(PackageEntity packageEntity) {
        return Package.builder()
                .name(packageEntity.getName())
                .price(packageEntity.getPrice())
                .contents(packageEntity.getContents())
                .notice(packageEntity.getNotice())
                .build();
    }
}
