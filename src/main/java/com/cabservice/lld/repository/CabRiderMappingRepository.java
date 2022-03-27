package com.cabservice.lld.repository;

import com.cabservice.lld.helper.TimeUtil;
import com.cabservice.lld.model.Cab;
import com.cabservice.lld.model.CabRiderMapping;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CabRiderMappingRepository {
    private final Map<Cab, CabRiderMapping> cabRiderMap;
    private final Map<Cab, List<CabRiderMapping>> cabRiderHistoricalMap;
    private final ReadWriteLock lock;


    public CabRiderMappingRepository() {
        this.cabRiderMap = new HashMap<>();
        this.cabRiderHistoricalMap = new HashMap<>();
        lock = new ReentrantReadWriteLock(true);
    }

    public CabRiderMapping addCabRiderMapping(CabRiderMapping cabRiderMapping) {
        try {
            lock.writeLock().lock();
            Cab cab = cabRiderMapping.getCab();
            CabRiderMapping currentCabRiderMapping = cabRiderMap.get(cab);
            if (currentCabRiderMapping == null
                    || !currentCabRiderMapping.getAssignedRider().equals(cabRiderMapping.getAssignedRider())) {
                Instant currentTime = TimeUtil.getCurrentInstant();
                if (currentCabRiderMapping != null) {
                    currentCabRiderMapping.setToTime(currentTime);
                }
                cabRiderMapping.setFromTime(currentTime);
                cabRiderMapping.setToTime(null);
                cabRiderMap.put(cab, cabRiderMapping);
                List<CabRiderMapping> cabRiderMappings = cabRiderHistoricalMap.getOrDefault(cab, new ArrayList<>());
                cabRiderMappings.add(cabRiderMapping);
                cabRiderHistoricalMap.put(cab, cabRiderMappings);
            }
            return cabRiderMap.get(cab);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public CabRiderMapping getCabRiderMappingForCab(Cab cab) {
        try {
            lock.readLock().lock();
            return cabRiderMap.get(cab);
        } finally {
            lock.readLock().unlock();
        }
    }
}
