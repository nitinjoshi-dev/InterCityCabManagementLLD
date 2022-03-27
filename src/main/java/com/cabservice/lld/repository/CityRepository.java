package com.cabservice.lld.repository;

import com.cabservice.lld.exception.CityAlreadyRegisteredException;
import com.cabservice.lld.model.City;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CityRepository {
    private final Map<String, City> cities;
    private long maxCityId;
    private final ReadWriteLock lock;

    public CityRepository() {
        cities = new HashMap<>();
        maxCityId = 1L;
        lock = new ReentrantReadWriteLock(true);
    }

    public City addCity(City city) {
        try {
            lock.writeLock().lock();
            if(cities.get(city.getCityName()) == null) {
                city.setCityId(maxCityId++);
                cities.put(city.getCityName(), city);
                return cities.get(city.getCityName());
            }
            throw new CityAlreadyRegisteredException("City with given name, " + city.getCityName() + ", already registered");
        } finally {
            lock.writeLock().unlock();
        }
    }
}
