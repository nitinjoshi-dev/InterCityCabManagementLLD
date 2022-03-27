package com.cabservice.lld.strategy;

import com.cabservice.lld.repository.CabStateMappingRepository;
import com.google.inject.Inject;
import com.cabservice.lld.constant.CabState;
import com.cabservice.lld.helper.CollectionUtil;
import com.cabservice.lld.model.Cab;
import com.cabservice.lld.model.CabStateMapping;
import com.cabservice.lld.model.City;

import java.util.List;
import java.util.Optional;

public class IdleTimeBasedCabMatchingStrategy implements CabMatchingStrategy {

    @Inject
    private CabStateMappingRepository cabStateMappingRepository;

    @Override
    public Cab matchCabForTrip(City fromCity, City toCity, List<Cab> cabList) {
        if (CollectionUtil.isEmpty(cabList)) {
            return null;
        }

        List<CabStateMapping> cabStateMappings = cabStateMappingRepository.getCabStateForCabs(cabList);
        Optional<CabStateMapping> cabStateMapping = cabStateMappings.stream()
                .filter(stateMapping -> stateMapping.getCabState() == CabState.IDLE)
                .min((o1, o2) -> {
                    if (o1.getFromTime().toEpochMilli() == o2.getFromTime().toEpochMilli()) {
                        return (int) (o1.getCab().getCabId() - o2.getCab().getCabId());
                    }
                    return (int) (o1.getFromTime().toEpochMilli() - o2.getFromTime().toEpochMilli());
                });

        return cabStateMapping.map(CabStateMapping::getCab).orElse(null);
    }
}
