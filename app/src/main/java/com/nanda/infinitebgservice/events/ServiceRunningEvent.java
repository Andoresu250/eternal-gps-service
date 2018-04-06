package com.nanda.infinitebgservice.events;

import android.location.Location;

import com.nanda.infinitebgservice.base.BaseActivity;

/**
 * Created by Nandagopal on 6/16/17.
 */

public class ServiceRunningEvent {

    private String time;
    private Location location;

    public ServiceRunningEvent(String time, Location location) {
        this.time = time;
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
