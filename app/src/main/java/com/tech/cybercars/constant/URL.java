package com.tech.cybercars.constant;

public class URL {
    //HOST
    public static final String AVATAR_RES_PATH = "/public/assets/media/avatar/";
    public static final String DRIVER_REGISTRATION_RES_PATH = "/public/assets/media/driver-registration/";
    public static final String ID_CARD_RES_PATH = "/public/assets/media/id-card/";
    public static final String BASE_URL = "https://cybercars.tech";

    //security
    public static final String FORGOT_PASSWORD = "/api/security/forgot-password";
    public static final String RESET_PASSWORD = "/api/security/reset-password";

    //chat
    public static final String CHAT_LIST = "/api/chat/list";
    public static final String PRIVATE_CHAT = "/api/chat/private-chat/{receiver_id}";

    //rating
    public static final String CREATE_RATING = "/api/rating/create";
    public static final String RATING_LIST = "/api/rating/list/{user_id}";

    //member
    public static final String UPDATE_MEMBER_STATUS = "/api/trip/member/update-status";
    public static final String MEMBER_LEAVE_TRIP = "/api/trip/member/leave";

    //trip
    public static final String DELETE_TRIP = "/api/trip/delete/{trip_id}";
    public static final String UPDATE_TRIP_LOCATION = "/api/trip/update-location";
    public static final String UPDATE_TRIP_INFORMATION = "/api/trip/update-information";
    public static final String UPDATE_TRIP_STATUS = "/api/trip/update-status";
    public static final String TRIP_LIST = "/api/trip/list";
    public static final String MEMBER_REQUEST_TO_JOIN = "/api/trip/member/request-to-join";
    public static final String CREATE_TRIP = "/api/trip/create";
    public static final String PASSENGER_FIND_TRIP = "/api/trip/passenger-find-trip";

    //vehicle
    public static final String VEHICLE_ACCEPTED_LIST = "/api/user/vehicle/accepted-list";
    public static final String VEHICLE_LIST = "/api/user/vehicle/list";
    public static final String DELETE_VEHICLE = "/api/user/vehicle/delete-vehicle/{vehicle_id}";

    //notification
    public static final String NOTIFICATIONS = "/api/user/notification";

    //Security
    public static final String SIGN_IN = "/api/security/sign-in";
    public static final String SIGN_UP = "/api/security/sign-up";
    public static final String ACTIVE_ACCOUNT = "/api/security/sign-up/activate-account";


    //Map
    public static final String MAP_REVERSE_GEOCODING = "/api/map/reverse-geocoding/{lat}/{lng}";
    public static final String SEARCH_ADDRESS = "/api/map/search-address/{address}/{limit}";

    //User
    public static final String UPDATE_PASSWORD = "/api/security/update-password";
    public static final String VIEW_STATISTIC = "/api/user/statistic/{start_date}/{end_date}";
    public static final String VIEW_USER_PROFILE = "/api/user/view-user-profile/{user_id}";
    public static final String DRIVER_REGISTRATION = "/api/user/driver-registration";
    public static final String PROFILE = "/api/user/profile";
    public static final String UPDATE_PROFILE = "/api/user/profile/update";
    public static final String UPDATE_ID_CARD = "/api/user/profile/update-id-card";
    public static final String UPDATE_PHONE_NUMBER_CARD = "/api/user/profile/update-phone-number";
    public static final String RESEND_OTP_CODE = "/api/resend-otp-code";

    //firebase token
    public static final String UPDATE_FIREBASE_TOKEN = "/api/security/firebase-token";
}
