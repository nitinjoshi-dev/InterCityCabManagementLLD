package com.cabservice.lld.service.impl;

import com.cabservice.lld.exception.ValidationException;
import com.cabservice.lld.model.dto.CityTimeCountKey;
import com.cabservice.lld.repository.CityRepository;
import com.cabservice.lld.repository.TripRepository;
import com.cabservice.lld.service.intf.CityService;
import com.google.inject.Inject;
import com.cabservice.lld.helper.RequestValidator;
import com.cabservice.lld.model.City;

import java.util.Set;

public class CityServiceImpl implements CityService {

    @Inject
    private CityRepository cityRepository;

    @Inject
    private TripRepository tripRepository;

    @Inject
    private RequestValidator requestValidator;

    @Override
    public City addCity(City city) {
        if(requestValidator.validateCity(city)) {
            return cityRepository.addCity(city);
        }
        throw new ValidationException("Error in request " + city);
    }

    @Override
    public Set<CityTimeCountKey> getDemandWiseCityList() {
        return tripRepository.getOrderWiseDemand();
    }
}
