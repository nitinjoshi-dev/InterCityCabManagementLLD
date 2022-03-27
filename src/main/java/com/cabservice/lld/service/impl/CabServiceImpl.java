package com.cabservice.lld.service.impl;

import com.cabservice.lld.constant.CabState;
import com.cabservice.lld.exception.ValidationException;
import com.cabservice.lld.model.*;
import com.cabservice.lld.repository.CabCityMappingRepository;
import com.cabservice.lld.repository.CabRepository;
import com.cabservice.lld.repository.CabRiderMappingRepository;
import com.cabservice.lld.repository.CabStateMappingRepository;
import com.cabservice.lld.service.intf.CabService;
import com.google.inject.Inject;
import com.cabservice.lld.helper.RequestValidator;
import com.cabservice.lld.model.dto.CabSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CabServiceImpl implements CabService {

    @Inject
    private CabRepository cabRepository;

    @Inject
    private CabStateMappingRepository cabStateMappingRepository;

    @Inject
    private CabRiderMappingRepository cabRiderMappingRepository;

    @Inject
    private CabCityMappingRepository cabCityMappingRepository;

    @Inject
    private RequestValidator requestValidator;

    private final Object lock = new Object();

    @Override
    public Cab registerCab(Cab cab) {
        boolean isValid = requestValidator.validateCab(cab);
        if(isValid) {
            synchronized (lock) {
                cab = cabRepository.registerCab(cab);
                setDefaultCabState(cab);
                return cab;
            }
        }
        throw new ValidationException("Error in request " + cab);
    }

    private void setDefaultCabState(Cab cab) {
        CabStateMapping cabStateMapping = CabStateMapping
                .builder()
                .cabState(CabState.IDLE)
                .cab(cab)
                .build();
        cabStateMappingRepository.setCabState(cabStateMapping);
    }

    @Override
    public CabRiderMapping assignRiderToCab(Cab cab, Rider rider) {
        boolean isValid = requestValidator.validateExistingCab(cab) || requestValidator.validateExistingRider(rider);

        if(isValid) {
            CabRiderMapping cabRiderMapping = CabRiderMapping.builder()
                    .assignedRider(rider)
                    .cab(cab)
                    .build();

            return cabRiderMappingRepository.addCabRiderMapping(cabRiderMapping);
        }
        throw new ValidationException("Error in request " + cab + " " + rider);
    }



    @Override
    public CabCityMapping changeCabLocation(Cab cab, City city) {
        boolean isValid = requestValidator.validateExistingCab(cab) || requestValidator.validateCity(city);
        if(isValid) {
            CabCityMapping cabCityMapping = CabCityMapping.builder()
                    .cab(cab)
                    .city(city)
                    .build();
            return cabCityMappingRepository.addCabCityMapping(cabCityMapping);
        }
        throw new ValidationException("Error in request " + cab + " " + city);
    }

    @Override
    public CabStateMapping changeCabState(Cab cab, CabState cabState) {
        boolean isValid = requestValidator.validateExistingCab(cab) || requestValidator.validateState(cabState);
        if(isValid) {
            CabStateMapping cabStateMapping = CabStateMapping
                    .builder()
                    .cabState(cabState)
                    .cab(cab)
                    .build();
            return cabStateMappingRepository.setCabState(cabStateMapping);
        }
        throw new ValidationException("Error in request " + cab + " " + cabState);
    }

    @Override
    public List<CabSnapshot> getAllCabSnapshot() {
        List<CabStateMapping> cabStateMappings = cabStateMappingRepository.getAllCabStateMapping();
        List<CabSnapshot> cabSnapshots = new ArrayList<>(cabStateMappings.size());
        for(CabStateMapping cabStateMapping : cabStateMappings) {
            City city = cabCityMappingRepository.getCabLocation(cabStateMapping.getCab());
            CabSnapshot cabSnapshot = CabSnapshot.builder()
                    .cab(cabStateMapping.getCab())
                    .cabState(cabStateMapping.getCabState())
                    .location(city)
                    .build();
            cabSnapshots.add(cabSnapshot);
        }
        return cabSnapshots;
    }

    @Override
    public List<CabStateMapping> getCabStateHistory(Cab cab) {
        return cabStateMappingRepository.getCabStateHistoryForCab(cab);
    }
}
