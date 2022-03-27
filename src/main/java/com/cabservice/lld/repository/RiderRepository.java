package com.cabservice.lld.repository;

import com.cabservice.lld.exception.RiderAlreadyRegisteredException;
import com.cabservice.lld.model.Rider;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RiderRepository {
    private final Map<String, Rider> ridersLicenseMap;
    private long maxRiderId;
    private final ReadWriteLock lock;

    public RiderRepository() {
        ridersLicenseMap = new HashMap<>();
        lock = new ReentrantReadWriteLock(true);
        maxRiderId = 1L;
    }

    public Rider addRider(Rider rider) {
        try {
            lock.writeLock().lock();
            if (ridersLicenseMap.get(rider.getLicenseNumber()) == null) {
                rider.setRiderId(maxRiderId++);
                ridersLicenseMap.put(rider.getLicenseNumber(), rider);
                return ridersLicenseMap.get(rider.getLicenseNumber());
            }
            throw new RiderAlreadyRegisteredException("Rider with given license number, " + rider.getLicenseNumber() + ", already registered");
        } finally {
            lock.writeLock().unlock();
        }
    }
}
