package com.cabservice.lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Cab {
    private Long cabId;
    private String cabNumber;
    private String manufacturer;
    private String model;
}
