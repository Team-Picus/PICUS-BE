package com.picus.core.price.adapter.out.persistence;

import com.picus.core.price.adapter.out.persistence.entity.PackageEntity;
import com.picus.core.price.adapter.out.persistence.mapper.PackagePersistenceMapper;
import com.picus.core.price.adapter.out.persistence.repository.PackageJpaRepository;
import com.picus.core.price.application.port.out.PackageReadPort;
import com.picus.core.price.domain.Package;
import com.picus.core.shared.annotation.PersistenceAdapter;
import com.picus.core.shared.exception.RestApiException;
import lombok.RequiredArgsConstructor;

import static com.picus.core.shared.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@PersistenceAdapter
@RequiredArgsConstructor
public class PackagePersistenceAdapter implements PackageReadPort {

    private final PackageJpaRepository packageJpaRepository;
    private final PackagePersistenceMapper packagePersistenceMapper;

    @Override
    public Package findById(String packageNo) {
        PackageEntity entity = packageJpaRepository.findById(packageNo)
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));

        return packagePersistenceMapper.toDomain(entity);
    }
}
