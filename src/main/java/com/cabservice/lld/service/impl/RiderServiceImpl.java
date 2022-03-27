package com.cabservice.lld.service.impl;

import com.cabservice.lld.exception.ValidationException;
import com.cabservice.lld.repository.RiderRepository;
import com.cabservice.lld.service.intf.RiderService;
import com.google.inject.Inject;
import com.cabservice.lld.helper.RequestValidator;
import com.cabservice.lld.model.Rider;

public class RiderServiceImpl implements RiderService {

    @Inject
    private RiderRepository riderRepository;

    @Inject
    private RequestValidator requestValidator;

    @Override
    public Rider addRider(Rider rider) {
        boolean isValid = requestValidator.validateRider(rider);
        if(isValid) {
            return riderRepository.addRider(rider);
        }
        throw new ValidationException("Error in request " + rider);
    }
}
