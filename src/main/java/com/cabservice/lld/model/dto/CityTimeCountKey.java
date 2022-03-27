package com.cabservice.lld.model.dto;

import com.cabservice.lld.model.City;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CityTimeCountKey implements Comparable<CityTimeCountKey> {
    private City city;
    private int hourOfDay;
    private int count;

    @Override
    public int compareTo(CityTimeCountKey that) {
        if(this.city.getCityId().equals(that.getCity().getCityId())
                && this.hourOfDay == that.getHourOfDay()) {
            return 0;
        }
        if(that.count == this.count) {
            return 1;
        }
        return that.count - this.count;
    }
}
