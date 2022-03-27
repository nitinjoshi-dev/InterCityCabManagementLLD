package com.cabservice.lld.repository;

import com.cabservice.lld.exception.CabAlreadyRegisteredException;
import com.cabservice.lld.model.Cab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class CabRepository {

    private final Map<String, Cab> cabs;
    private long maxCabId;
    private final ReadWriteLock lock;

    public CabRepository() {
        cabs = new HashMap<>();
        maxCabId = 1L;
        lock = new ReentrantReadWriteLock(true);
    }

    public Cab registerCab(Cab cab) {
        try {
            lock.writeLock().lock();
            if (cabs.get(cab.getCabNumber()) == null) {
                cab.setCabId(maxCabId++);
                cabs.put(cab.getCabNumber(), cab);
                return cabs.get(cab.getCabNumber());
            }
            throw new CabAlreadyRegisteredException("Cab with given number, " + cab.getCabNumber() + ", already registered");
        } finally {
            lock.writeLock().unlock();
        }
    }

    public List<Cab> getAllCabs() {
        try {
            lock.readLock().lock();
            return new ArrayList<>(cabs.values());
        } finally {
            lock.readLock().unlock();
        }
    }


}
