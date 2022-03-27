package com.cabservice.lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CabCityMapping {
    private Cab cab;
    private City city;
}
