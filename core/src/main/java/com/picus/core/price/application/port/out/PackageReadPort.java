package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Package;

public interface PackageReadPort {

    Package findById(String packageNo);

}
