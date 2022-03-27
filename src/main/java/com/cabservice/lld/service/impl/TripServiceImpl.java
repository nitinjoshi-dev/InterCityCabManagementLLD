package com.cabservice.lld.service.impl;

import com.cabservice.lld.constant.CabState;
import com.cabservice.lld.exception.NoAvailableCabsException;
import com.cabservice.lld.exception.TripNotFoundException;
import com.cabservice.lld.exception.ValidationException;
import com.cabservice.lld.model.*;
import com.cabservice.lld.repository.CabCityMappingRepository;
import com.cabservice.lld.repository.CabRiderMappingRepository;
import com.cabservice.lld.repository.CabStateMappingRepository;
import com.cabservice.lld.repository.TripRepository;
import com.cabservice.lld.service.intf.TripService;
import com.cabservice.lld.strategy.CabMatchingStrategy;
import com.google.inject.Inject;
import com.cabservice.lld.helper.RequestValidator;

import java.util.List;

public class TripServiceImpl implements TripService {

    @Inject
    private CabCityMappingRepository cabCityMappingRepository;

    @Inject
    private TripRepository tripRepository;

    @Inject
    private CabRiderMappingRepository cabRiderMappingRepository;

    @Inject
    private CabMatchingStrategy cabMatchingStrategy;

    @Inject
    private CabStateMappingRepository cabStateMappingRepository;

    @Inject
    private RequestValidator requestValidator;

    private final Object lock = new Object();


    @Override
    public Trip bookCab(City fromLocation, City toLocation) {

        synchronized (lock) {
            List<Cab> availableCabs = cabCityMappingRepository.getCabsForCity(fromLocation);
            Cab cab = cabMatchingStrategy.matchCabForTrip(fromLocation, toLocation, availableCabs);
            if (cab == null) {
                throw new NoAvailableCabsException("Error, No cab available in this location " + fromLocation);
            }
            CabRiderMapping cabRiderMapping = cabRiderMappingRepository.getCabRiderMappingForCab(cab);
            Trip trip = Trip.builder()
                    .cabRiderMapping(cabRiderMapping)
                    .endLocation(toLocation)
                    .startLocation(fromLocation)
                    .build();

            cabCityMappingRepository.removeCabCityMappingForCab(cab);
            cabStateMappingRepository.setCabState(CabStateMapping.builder()
                    .cabState(CabState.ON_TRIP)
                    .cab(cab)
                    .build());

            return tripRepository.startTrip(trip);
        }
    }

    public Trip endTrip(Trip trip) {
        synchronized (lock) {
            if (requestValidator.validateTrip(trip)) {
                trip = tripRepository.endTrip(trip);

                if(trip == null) {
                    throw new TripNotFoundException("Trip with given id not found ");
                }
                cabStateMappingRepository.setCabState(CabStateMapping.builder()
                        .cabState(CabState.IDLE)
                        .cab(trip.getCabRiderMapping().getCab())
                        .build());
                cabCityMappingRepository.addCabCityMapping(CabCityMapping.builder()
                        .city(trip.getEndLocation())
                        .cab(trip.getCabRiderMapping().getCab())
                        .build());

                return trip;
            }
            throw new ValidationException("Trip validation failed " + trip);
        }
    }
}
