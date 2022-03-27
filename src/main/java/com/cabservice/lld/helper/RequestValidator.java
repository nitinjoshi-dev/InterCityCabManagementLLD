package com.cabservice.lld.helper;

import com.cabservice.lld.constant.CabState;
import com.cabservice.lld.model.Cab;
import com.cabservice.lld.model.City;
import com.cabservice.lld.model.Rider;
import com.cabservice.lld.model.Trip;

public class RequestValidator {

    public boolean validateTrip(Trip trip) {
        return trip != null && trip.getTripId() != null;
    }

    public boolean validateCab(Cab cab) {
        return cab != null && cab.getCabNumber() != null;
    }

    public boolean validateExistingCab(Cab cab) {
        return validateCab(cab) && cab.getCabId() != null;
    }

    public boolean validateRider(Rider rider) {
        return rider != null && rider.getLicenseNumber() != null;
    }

    public boolean validateExistingRider(Rider rider) {
        return validateRider(rider) && rider.getRiderId() != null;
    }

    public boolean validateCity(City city) {
        return city != null && city.getCityName() != null;
    }

    public boolean validateExistingCity(City city) {
        return validateCity(city) && city.getCityId() != null;
    }

    public boolean validateState(CabState cabState) {
        return cabState != null && cabState != CabState.UNKNOWN_CAB_STATE;
    }
}
