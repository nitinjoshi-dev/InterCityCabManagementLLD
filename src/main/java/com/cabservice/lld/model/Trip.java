package com.cabservice.lld.model;

import com.cabservice.lld.constant.TripStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class Trip {
    private Long tripId;
    private Instant startTime;
    private Instant endTime;
    private City startLocation;
    private City endLocation;
    private Double price;
    private CabRiderMapping cabRiderMapping;
    private TripStatus tripStatus;
}
