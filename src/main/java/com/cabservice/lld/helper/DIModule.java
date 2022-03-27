package com.cabservice.lld.helper;

import com.cabservice.lld.repository.*;
import com.cabservice.lld.service.impl.CabServiceImpl;
import com.cabservice.lld.service.impl.CityServiceImpl;
import com.cabservice.lld.service.impl.RiderServiceImpl;
import com.cabservice.lld.service.impl.TripServiceImpl;
import com.cabservice.lld.service.intf.CabService;
import com.cabservice.lld.service.intf.CityService;
import com.cabservice.lld.service.intf.RiderService;
import com.cabservice.lld.service.intf.TripService;
import com.cabservice.lld.strategy.CabMatchingStrategy;
import com.cabservice.lld.strategy.IdleTimeBasedCabMatchingStrategy;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class DIModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RiderRepository.class).in(Singleton.class);
        bind(CabRepository.class).in(Singleton.class);
        bind(CabCityMappingRepository.class).in(Singleton.class);
        bind(CabStateMappingRepository.class).in(Singleton.class);
        bind(CabRiderMappingRepository.class).in(Singleton.class);
        bind(TripRepository.class).in(Singleton.class);
        bind(CityRepository.class).in(Singleton.class);
        bind(RequestValidator.class).in(Singleton.class);
        bind(CabMatchingStrategy.class).toInstance(new IdleTimeBasedCabMatchingStrategy());
        bind(CabService.class).toInstance(new CabServiceImpl());
        bind(RiderService.class).toInstance(new RiderServiceImpl());
        bind(TripService.class).toInstance(new TripServiceImpl());
        bind(CityService.class).toInstance(new CityServiceImpl());
    }
}
