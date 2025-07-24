package com.picus.core.price.application.port.mapper;

import com.picus.core.price.application.port.in.command.PackageCommand;
import com.picus.core.price.domain.model.Package;
import org.springframework.stereotype.Component;

@Component
public class PackageCommandAppMapper {

    public Package toDomain(PackageCommand command) {
        return Package.builder()
                .packageNo(command.packageNo())
                .name(command.name())
                .price(command.price())
                .contents(command.contents())
                .notice(command.notice())
                .build();
    }


}
