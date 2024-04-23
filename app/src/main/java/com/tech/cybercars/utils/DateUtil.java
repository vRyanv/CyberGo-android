package com.tech.cybercars.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtil {
    public static String D_M_Y = "dd/MM/yyyy";
    public static String Y_M_D = "yyyy-MM-dd";
    public static String YMDFormat(Calendar calendar){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateUtil.Y_M_D, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }
    public static String GetCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DateTimePicker.M_D_Y, Locale.getDefault());
        return simpleDateFormat.format(calendar.getTime());
    }
    public static int GetCurrentTime(){
        return LocalTime.now().toSecondOfDay();
    }
    public static String ConvertSecondToHour(Double total_second){
        int hours = (int) (total_second / 3600);
        int minutes = (int) ((total_second % 3600) / 60);
        int seconds = (int) (total_second % 60);
        return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
    }
    public static Date ConvertTimestampToTime() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String time = sdf.format(1642850900);
        return sdf.parse(time);
    }

    public static long ConvertTimeToTimestamp(String date_str) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = sdf.parse(date_str);
        assert date != null;
        return  (date.getTime() / 1_000_000);
    }
}
