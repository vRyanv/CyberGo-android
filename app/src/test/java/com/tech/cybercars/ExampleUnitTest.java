package com.tech.cybercars;

import org.junit.Test;

import static org.junit.Assert.*;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testPhone(){
        String swissNumberStr = "374463592";
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(swissNumberStr, "VN");
            System.out.println(swissNumberProto.getCountryCode());
            System.out.println(swissNumberProto.getNationalNumber());
            System.out.println(phoneUtil.isValidNumber(swissNumberProto));
            System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164));
            System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
            System.out.println(phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }
}