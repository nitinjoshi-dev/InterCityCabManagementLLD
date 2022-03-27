package com.cabservice.lld.model;

import com.cabservice.lld.constant.CabState;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class CabStateMapping {
    private Cab cab;
    private CabState cabState;
    private Instant fromTime;
    private Instant toTime;
}
