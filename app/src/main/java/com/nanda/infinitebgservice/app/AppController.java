package com.nanda.infinitebgservice.app;

import android.app.Application;

import com.nanda.infinitebgservice.helper.MainThreadBus;

/**
 * Created by OrgwareTechnologies on 6/16/17.
 */

public class AppController extends Application {

    private static AppController appController;
    private MainThreadBus bus;

    @Override
    public void onCreate() {
        super.onCreate();

        appController = this;
        bus = new MainThreadBus();

    }

    public static AppController getInstance() {
        return appController;
    }

    public MainThreadBus getBus() {
        return bus;
    }

}
