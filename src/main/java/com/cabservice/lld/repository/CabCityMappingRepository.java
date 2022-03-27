package com.cabservice.lld.repository;

import com.cabservice.lld.helper.CollectionUtil;
import com.cabservice.lld.model.Cab;
import com.cabservice.lld.model.CabCityMapping;
import com.cabservice.lld.model.City;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CabCityMappingRepository {
    private final Map<Cab, CabCityMapping> cabCityMap;
    private final Map<City, List<CabCityMapping>> cityCabMap;
    private final ReadWriteLock lock;


    public CabCityMappingRepository() {
        this.cabCityMap = new HashMap<>();
        this.cityCabMap = new HashMap<>();
        this.lock = new ReentrantReadWriteLock(true);
    }

    public CabCityMapping addCabCityMapping(CabCityMapping cabCityMapping) {
        try {
            lock.writeLock().lock();
            Cab cab = cabCityMapping.getCab();
            CabCityMapping currentCabCityMapping = cabCityMap.get(cab);
            if (currentCabCityMapping == null
                    || !currentCabCityMapping.getCity().equals(cabCityMapping.getCity())) {
                if (currentCabCityMapping != null) {
                    cabCityMap.remove(cab);
                    List<CabCityMapping> cabCityMappings = cityCabMap.get(cabCityMapping.getCity());
                    if(CollectionUtil.isNotEmpty(cabCityMappings)) {
                        cabCityMappings.remove(cabCityMapping);
                    }
                }
                cabCityMap.put(cab, cabCityMapping);

                List<CabCityMapping> cabCityMappings = cityCabMap.getOrDefault(cabCityMapping.getCity(), new ArrayList<>());
                cabCityMappings.add(cabCityMapping);
                cityCabMap.put(cabCityMapping.getCity(), cabCityMappings);
            }
            return cabCityMap.get(cab);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean removeCabCityMappingForCab(Cab cab) {
        try {
            lock.readLock().lock();
            CabCityMapping cabCityMapping = cabCityMap.remove(cab);
            if(cabCityMapping != null) {
                List<CabCityMapping> cabCityMappings = cityCabMap.get(cabCityMapping.getCity());
                if(CollectionUtil.isNotEmpty(cabCityMappings)) {
                    cabCityMappings.remove(cabCityMapping);
                }
                return true;
            }
            return false;

        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Cab> getCabsForCity(City city) {
        try {
            lock.readLock().lock();
            return cityCabMap.get(city).stream()
                    .map(CabCityMapping::getCab)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public City getCabLocation(Cab cab) {
        try {
            lock.readLock().lock();
            CabCityMapping cabCityMapping = cabCityMap.get(cab);
            if(cabCityMapping == null) {
                return null;
            }
            return cabCityMapping.getCity();
        } finally {
            lock.readLock().unlock();
        }
    }
}
