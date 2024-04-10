package com.tech.cybercars.utils;

import java.text.DecimalFormat;

public class Helper {
    public static Double ConvertMeterToKiloMeter(Double meter){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return meter/1000;
    }

    public static String ConvertMeterToKiloMeterString(Double meter){
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(meter/1000);
    }

    public static String PadStart(int number, int length) {
        return String.format("%0" + length + "d", number);
    }
}
