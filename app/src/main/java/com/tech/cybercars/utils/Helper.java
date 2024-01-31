package com.tech.cybercars.utils;

import android.annotation.SuppressLint;
import android.location.Location;

import java.util.Locale;

public class Helper {
    public static String ConvertSecondToHour(Double total_second){
        int hours = (int) (total_second / 3600);
        int minutes = (int) ((total_second % 3600) / 60);
        int seconds = (int) (total_second % 60);
        return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static Double ConvertMeterToKiloMeter(Double meter){
       return meter/1000;
    }
}
