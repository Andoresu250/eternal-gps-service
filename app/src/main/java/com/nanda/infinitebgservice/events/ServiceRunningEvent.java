package com.nanda.infinitebgservice.events;

/**
 * Created by Nandagopal on 6/16/17.
 */

public class ServiceRunningEvent {

    private String time;

    public ServiceRunningEvent(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
