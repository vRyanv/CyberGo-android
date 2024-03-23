package com.tech.cybercars.constant;

public class URL {
    //HOST
    public static final String AVATAR_RES_PATH = "/assets/media/avatar/";
    public static final String DRIVER_REGISTRATION_RES_PATH = "/assets/media/driver-registration/";
    public static final String ID_CARD_RES_PATH = "/assets/media/id-card/";
    public static final String BASE_URL = "http://192.168.2.6:2108";

    //Security
    public static final String SIGN_IN = "/security/sign-in";
    public static final String SIGN_UP = "/security/sign-up";
    public static final String ACTIVE_ACCOUNT = "/security/sign-up/activate-account";


    //Map
    public static final String MAP_REVERSE_GEOCODING = "/map/reverse-geocoding/{lat}/{lng}";
    public static final String SEARCH_ADDRESS = "/map/search-address/{address}/{limit}";

    //User
    public static final String DRIVER_REGISTRATION = "/user/driver-registration";
    public static final String PROFILE = "/user/profile";
    public static final String UPDATE_PROFILE = "/user/profile/update";
    public static final String UPDATE_ID_CARD = "/user/profile/update-id-card";
    public static final String UPDATE_PHONE_NUMBER_CARD = "/user/profile/update-phone-number";

}
