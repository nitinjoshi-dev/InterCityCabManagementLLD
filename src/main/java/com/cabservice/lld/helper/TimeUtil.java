package com.cabservice.lld.helper;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {
    private static final String DEFAULT_TIMEZONE = "Asia/Kolkata";
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.of(DEFAULT_TIMEZONE);

    public static Instant getCurrentInstant() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(DEFAULT_ZONE_ID);
        return zonedDateTime.toInstant(); //Returns instant in UTC
    }
}
