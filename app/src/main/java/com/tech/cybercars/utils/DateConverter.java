package com.tech.cybercars.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class DateConverter {
    public static LocalDateTime TimestampsToDate(Long timestamp, String time_zone_id){
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.of(time_zone_id));
    }

    public static Long DateToTimestamps(LocalDateTime datetime){
        return datetime.atZone(ZoneOffset.UTC).toInstant().toEpochMilli();
    }
}
