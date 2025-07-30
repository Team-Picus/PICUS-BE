package com.picus.core.price.application.port.in.mapper;

import com.picus.core.price.application.port.in.command.UpdatePackageCommand;
import com.picus.core.price.domain.Package;
import org.springframework.stereotype.Component;

@Component
public class UpdatePackageCommandMapper {

    public Package toDomain(UpdatePackageCommand command) {
        return Package.builder()
                .packageNo(command.packageNo())
                .name(command.name())
                .price(command.price())
                .contents(command.contents())
                .notice(command.notice())
                .build();
    }


}
