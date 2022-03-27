package com.cabservice.lld.service.intf;

import com.cabservice.lld.model.dto.CityTimeCountKey;
import com.cabservice.lld.model.City;

import java.util.Set;

public interface CityService {
    City addCity(City city);
    Set<CityTimeCountKey> getDemandWiseCityList();
}
