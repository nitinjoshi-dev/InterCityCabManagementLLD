package com.cabservice.lld.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class City {
    private Long cityId;
    private String cityName;
}
