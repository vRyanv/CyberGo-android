package com.tech.cybercars.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static Date ConvertTimestampToTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        String time = sdf.format(1642850900);
        return sdf.parse(time);
    }

    public static long ConvertTimeToTimestamp(String date_str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
        Date date = sdf.parse(date_str);
        assert date != null;
        return  (date.getTime() / 1_000_000);
    }
}
