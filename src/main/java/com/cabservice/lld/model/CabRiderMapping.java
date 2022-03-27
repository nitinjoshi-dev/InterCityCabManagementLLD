package com.cabservice.lld.model;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CabRiderMapping {
    private Cab cab;
    private Rider assignedRider;
    private Instant fromTime;
    private Instant toTime;
}
