package com.cabservice.lld.service.intf;

import com.cabservice.lld.model.City;
import com.cabservice.lld.model.Trip;

public interface TripService {

    Trip bookCab(City fromLocation, City toLocation);

    Trip endTrip(Trip trip);
}
