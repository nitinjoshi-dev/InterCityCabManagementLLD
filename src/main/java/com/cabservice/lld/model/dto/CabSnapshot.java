package com.cabservice.lld.model.dto;

import com.cabservice.lld.constant.CabState;
import com.cabservice.lld.model.Cab;
import com.cabservice.lld.model.City;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CabSnapshot {
    private Cab cab;
    private CabState cabState;
    private City location;
}
