package com.cabservice.lld.strategy;

import com.cabservice.lld.model.Cab;
import com.cabservice.lld.model.City;

import java.util.List;

public interface CabMatchingStrategy {
    Cab matchCabForTrip(City fromCity, City toCity, List<Cab> cabList);
}
