package com.tech.cybercars.services.eventbus;

public class ActionEvent {
    public static final String NOTIFY = "notify";
    public static final String START_SOCKET = "start_socket";
    public static final String STOP_SOCKET = "stop_socket";
    public static final String GO_TO_TRIP_FRAGMENT = "go_to_trip_fragment";
    public static final String UPDATE_DRAWER_INFO = "UPDATE_DRAWER_INFO";
    public static final String PASSENGER_REQUEST = "PASSENGER_REQUEST";
    public static final String REFRESH_TRIP_LIST = "REFRESH_TRIP_LIST";
    public static final String SHOW_ACCOUNT_BANNED_DIALOG = "SHOW_ACCOUNT_BANNED_DIALOG";
    public String action;
    public ActionEvent(String action){
        this.action = action;
    }
}
