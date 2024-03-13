package com.tech.cybercars.constant;

public class URL {
    //Security
    public static final String SIGN_IN = "/security/sign-in";
    public static final String SIGN_UP = "/security/sign-up";
    public static final String ACTIVE_ACCOUNT = "/security/sign-up/activate-account";


    //Map
    public static final String MAP_REVERSE_GEOCODING = "/map/reverse-geocoding/{lat}/{lng}";
    public static final String SEARCH_ADDRESS = "/map/search-address/{address}/{limit}";

    //User
    public static final String DRIVER_REGISTRATION = "/user/driver-registration";
}
