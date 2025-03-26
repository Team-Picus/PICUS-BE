package com.picus.core.domain.expert.domain.service;

import com.picus.core.domain.expert.domain.entity.Expert;
import com.picus.core.domain.expert.domain.entity.area.ExpertDistrict;
import com.picus.core.domain.expert.domain.repository.ExpertDistrictRepository;
import com.picus.core.domain.shared.area.domain.entity.District;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ExpertDistrictService {

    private final ExpertDistrictRepository expertDistrictRepository;

    public Set<ExpertDistrict> save(Expert expert, Set<District> districts) {
        List<ExpertDistrict> expertDistricts = districts.stream()
                .map(district -> new ExpertDistrict(expert, district))
                .toList();

        return new HashSet<>(expertDistrictRepository.saveAll(expertDistricts));
    }
}
