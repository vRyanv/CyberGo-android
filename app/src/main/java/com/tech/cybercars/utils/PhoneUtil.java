package com.tech.cybercars.utils;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneUtil {
    private static PhoneUtil phone_util;
    private PhoneUtil(){}
    public static PhoneUtil getInstance(){
        if(phone_util == null){
            phone_util = new PhoneUtil();
        }
        return phone_util;
    }

    /**
     * Convert a phone number base to phone national by country code,
     * example:
     * phone_number_base => 37446592 |
     * country_code => VN |
     * output => "0374463592"
     * @param phone_number_base
     * @param country_code
     * @return a phone number national
     */
    public String formatToPhoneNational(String phone_number_base, String country_code){
        PhoneNumberUtil phone_number_util = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber phone_number_format;
        String phone_national = "";
        boolean is_valid_phone = false;
        try {
            phone_number_format = phone_number_util.parse(phone_number_base, country_code);
            is_valid_phone = phone_number_util.isValidNumber(phone_number_format);
            phone_national = phone_number_util.format(phone_number_format, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);

        } catch (NumberParseException e) {
            e.printStackTrace();
        }

        if(is_valid_phone){
            return phone_national;
        } else {
            return "";
        }
    }
}
