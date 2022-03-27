package com.cabservice.lld.repository;

import com.cabservice.lld.helper.TimeUtil;
import com.cabservice.lld.model.Cab;
import com.cabservice.lld.model.CabStateMapping;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class CabStateMappingRepository {

    private final Map<Cab, CabStateMapping> cabStateMap;
    private final Map<Cab, List<CabStateMapping>> cabStateHistoricalMap;
    private final ReadWriteLock lock;

    public CabStateMappingRepository() {
        cabStateMap = new HashMap<>();
        cabStateHistoricalMap = new HashMap<>();
        lock = new ReentrantReadWriteLock(true);
    }

    public CabStateMapping setCabState(CabStateMapping cabStateMapping) {
        try {
            lock.writeLock().lock();
            Cab cab = cabStateMapping.getCab();
            CabStateMapping currentCabStateMapping = cabStateMap.get(cab);
            if (currentCabStateMapping == null
                    || !cabStateMapping.getCabState().equals(currentCabStateMapping.getCabState())) {
                Instant currentTime = TimeUtil.getCurrentInstant();
                if (currentCabStateMapping != null) {
                    currentCabStateMapping.setToTime(currentTime);
                }
                cabStateMapping.setFromTime(currentTime);
                cabStateMapping.setToTime(null);

                List<CabStateMapping> cabStateMappings = cabStateHistoricalMap.getOrDefault(cab, new ArrayList<>());
                cabStateMappings.add(cabStateMapping);
                cabStateHistoricalMap.put(cab, cabStateMappings);
                cabStateMap.put(cab, cabStateMapping);
            }
            return cabStateMap.get(cab);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<CabStateMapping> getAllCabStateMapping() {
        try {
            lock.readLock().lock();
            return new ArrayList<>(cabStateMap.values());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<CabStateMapping> getCabStateForCabs(List<Cab> cabs) {
        try {
            lock.readLock().lock();
            return cabs.stream()
                    .map(cabStateMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<CabStateMapping> getCabStateHistoryForCab(Cab cab) {
        try {
            lock.readLock().lock();
            return cabStateHistoricalMap.get(cab);
        } finally {
            lock.readLock().unlock();
        }
    }

}
