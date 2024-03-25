package com.tech.cybercars.services.eventbus;

public class ActionEvent {
    public static final String NOTIFY = "notify";
    public String action;
    public ActionEvent(String action){
        this.action = action;
    }
}
