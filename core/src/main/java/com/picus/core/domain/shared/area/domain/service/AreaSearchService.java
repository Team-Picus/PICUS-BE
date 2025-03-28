package com.picus.core.domain.shared.area.domain.service;

import com.picus.core.domain.shared.area.domain.entity.District;
import com.picus.core.domain.shared.area.domain.repository.DistrictRepository;
import com.picus.core.global.common.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._NOT_FOUND;

@Service
@RequiredArgsConstructor
public class AreaSearchService {

    private final DistrictRepository districtRepository;

    public District findByDistrictName(String fullName) {
        return districtRepository.findByName(convertToDistrictName(fullName))
                .orElseThrow(() -> new RestApiException(_NOT_FOUND));
    }

    private String convertToDistrictName(String fullName) {
        return fullName.substring(fullName.indexOf(" ") + 1);
    }

    public Set<District> findDistricts(Set<String> fullNames) {
        Set<String> name = fullNames.stream()
                .map(this::convertToDistrictName)
                .collect(Collectors.toSet());

        return districtRepository.findPreferredAreas(name);
    }
}
