package com.cabservice.lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Rider {
    private Long riderId;
    private String fullName;
    private String licenseNumber;
}
