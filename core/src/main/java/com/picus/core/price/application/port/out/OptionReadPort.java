package com.picus.core.price.application.port.out;

import com.picus.core.price.domain.Option;

import java.util.List;

public interface OptionReadPort {

    Option findById(String optionNo);
    List<Option> findByIds(List<String> optionNos);

}
