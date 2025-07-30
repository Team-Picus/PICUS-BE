package com.picus.core.price.adapter.out.persistence.mapper;

import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.domain.Package;
import org.springframework.stereotype.Component;

@Component
public class PackagePersistenceMapper {

    public Package toDomain(PackageEntity packageEntity) {
        return Package.builder()
                .packageNo(packageEntity.getPackageNo())
                .name(packageEntity.getName())
                .price(packageEntity.getPrice())
                .contents(packageEntity.getContents())
                .notice(packageEntity.getNotice())
                .build();
    }

    public PackageEntity toEntity(Package p) {
        return PackageEntity.builder()
                .name(p.getName())
                .price(p.getPrice())
                .contents(p.getContents())
                .notice(p.getNotice())
                .build();
    }
}
