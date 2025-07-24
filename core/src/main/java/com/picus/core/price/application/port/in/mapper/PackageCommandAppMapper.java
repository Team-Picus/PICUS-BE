package com.picus.core.price.application.port.in.mapper;

import com.picus.core.price.application.port.in.request.UpdatePackageAppReq;
import com.picus.core.price.domain.model.Package;
import org.springframework.stereotype.Component;

@Component
public class PackageCommandAppMapper {

    public Package toDomain(UpdatePackageAppReq command) {
        return Package.builder()
                .packageNo(command.packageNo())
                .name(command.name())
                .price(command.price())
                .contents(command.contents())
                .notice(command.notice())
                .build();
    }


}
