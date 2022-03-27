package com.cabservice.lld.repository;

import com.cabservice.lld.model.dto.CityTimeCountKey;
import com.cabservice.lld.constant.TripStatus;
import com.cabservice.lld.exception.TripAlreadyStartedException;
import com.cabservice.lld.exception.TripNotFoundException;
import com.cabservice.lld.helper.TimeUtil;
import com.cabservice.lld.model.CabRiderMapping;
import com.cabservice.lld.model.City;
import com.cabservice.lld.model.Trip;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TripRepository {
    private final Map<CabRiderMapping, Trip> cabRiderMappingTripMap;
    private final TreeSet<CityTimeCountKey> cityWiseHourlyTripCount;
    private long maxTripId;
    private final ReadWriteLock lock;

    public TripRepository() {
        cabRiderMappingTripMap = new HashMap<>();
        cityWiseHourlyTripCount = new TreeSet<>();
        maxTripId = 1L;
        lock = new ReentrantReadWriteLock(true);
    }

    public Trip startTrip(Trip trip) {
        try {
            CabRiderMapping cabRiderMapping = trip.getCabRiderMapping();
            lock.writeLock().lock();
            if (cabRiderMappingTripMap.get(cabRiderMapping) == null) {
                Instant startTime = TimeUtil.getCurrentInstant();
                trip.setStartTime(startTime);
                trip.setTripId(maxTripId++);
                trip.setTripStatus(TripStatus.IN_PROGRESS);
                cabRiderMappingTripMap.put(trip.getCabRiderMapping(), trip);
                addDemandInfo(trip.getStartLocation(), startTime);

                return cabRiderMappingTripMap.get(trip.getCabRiderMapping());
            } else {
                throw new TripAlreadyStartedException("Already a trip is started with the given cab rider mapping " + trip.getCabRiderMapping());
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void addDemandInfo(City startLocation, Instant startTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime.toEpochMilli());
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        CityTimeCountKey cityTimeCountKey = CityTimeCountKey.builder()
                .city(startLocation)
                .hourOfDay(hourOfDay)
                .build();
        CityTimeCountKey existingCityTimeCountKey = cityWiseHourlyTripCount.ceiling(cityTimeCountKey);
        if (existingCityTimeCountKey != null && cityTimeCountKey.getCity().getCityId().equals(existingCityTimeCountKey.getCity().getCityId())
                && cityTimeCountKey.getHourOfDay() == existingCityTimeCountKey.getHourOfDay()) {
            cityTimeCountKey.setCount(existingCityTimeCountKey.getCount() + 1);
            cityWiseHourlyTripCount.remove(existingCityTimeCountKey);
        } else {
            cityTimeCountKey.setCount(1);
        }
        cityWiseHourlyTripCount.add(cityTimeCountKey);
    }

    public Trip endTrip(Trip trip) {
        try {
            CabRiderMapping cabRiderMapping = trip.getCabRiderMapping();
            lock.writeLock().lock();
            Trip existingTrip = cabRiderMappingTripMap.get(trip.getCabRiderMapping());
            if (existingTrip == null || !existingTrip.equals(trip)) {
                throw new TripNotFoundException("The given trip is not started or already completed");
            }
            trip.setEndTime(TimeUtil.getCurrentInstant());
            trip.setTripStatus(TripStatus.FINISHED);
            return cabRiderMappingTripMap.remove(trip.getCabRiderMapping());
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Trip getCurrentExistingTrip(CabRiderMapping cabRiderMapping) {
        try {
            lock.readLock().lock();
            return cabRiderMappingTripMap.get(cabRiderMapping);
        } finally {
            lock.readLock().unlock();
        }
    }

    public Set<CityTimeCountKey> getOrderWiseDemand() {
        try {
            lock.readLock().lock();
            return new TreeSet<>(cityWiseHourlyTripCount);
        } finally {
            lock.readLock().unlock();
        }
    }
}
