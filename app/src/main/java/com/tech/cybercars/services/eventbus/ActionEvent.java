package com.tech.cybercars.services.eventbus;

public class ActionEvent {
    public static final String NOTIFY = "notify";
    public static final String START_SOCKET = "start_socket";
    public String action;
    public ActionEvent(String action){
        this.action = action;
    }
}
