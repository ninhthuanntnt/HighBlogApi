package com.high.highblog.helper;

import java.time.Instant;

public class DateTimeHelper {

    public static Long toMilli(final Instant instant) {
        if (instant == null) {
            return null;
        } else {
            return instant.toEpochMilli();
        }
    }
}
