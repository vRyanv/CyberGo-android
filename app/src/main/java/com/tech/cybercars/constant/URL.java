package com.tech.cybercars.constant;

public class URL {
    //HOST
    public static final String AVATAR_RES_PATH = "/assets/media/avatar/";
    public static final String DRIVER_REGISTRATION_RES_PATH = "/assets/media/driver-registration/";
    public static final String ID_CARD_RES_PATH = "/assets/media/id-card/";
    public static final String BASE_URL = "http://192.168.4.47:2108";

    //chat
    public static final String CHAT_LIST = "/chat/list";
    public static final String PRIVATE_CHAT = "/chat/private-chat/{receiver_id}";

    //rating
    public static final String CREATE_RATING = "/rating/create";
    public static final String RATING_LIST = "/rating/list/{user_id}";

    //member
    public static final String UPDATE_MEMBER_STATUS = "/trip/member/update-status";
    public static final String MEMBER_LEAVE_TRIP = "/trip/member/leave";

    //trip
    public static final String DELETE_TRIP = "/trip/delete/{trip_id}";
    public static final String UPDATE_TRIP_LOCATION = "/trip/update-location";
    public static final String UPDATE_TRIP_INFORMATION = "/trip/update-information";
    public static final String UPDATE_TRIP_STATUS = "/trip/update-status";
    public static final String TRIP_LIST = "/trip/list";
    public static final String MEMBER_REQUEST_TO_JOIN = "/trip/member/request-to-join";
    public static final String CREATE_TRIP = "/trip/create";
    public static final String PASSENGER_FIND_TRIP = "/trip/passenger-find-trip";

    //vehicle
    public static final String VEHICLE_ACCEPTED_LIST = "/user/vehicle/accepted-list";
    public static final String VEHICLE_LIST = "/user/vehicle/list";

    //notification
    public static final String NOTIFICATIONS = "/user/notification";

    //Security
    public static final String SIGN_IN = "/security/sign-in";
    public static final String SIGN_UP = "/security/sign-up";
    public static final String ACTIVE_ACCOUNT = "/security/sign-up/activate-account";


    //Map
    public static final String MAP_REVERSE_GEOCODING = "/map/reverse-geocoding/{lat}/{lng}";
    public static final String SEARCH_ADDRESS = "/map/search-address/{address}/{limit}";

    //User
    public static final String VIEW_USER_PROFILE = "/user/view-user-profile/{user_id}";
    public static final String DRIVER_REGISTRATION = "/user/driver-registration";
    public static final String PROFILE = "/user/profile";
    public static final String UPDATE_PROFILE = "/user/profile/update";
    public static final String UPDATE_ID_CARD = "/user/profile/update-id-card";
    public static final String UPDATE_PHONE_NUMBER_CARD = "/user/profile/update-phone-number";

    //firebase token
    public static final String UPDATE_FIREBASE_TOKEN = "/security/firebase-token";
}
