package com.nanda.infinitebgservice;

import android.Manifest;


public class Constants {

    public static long LOCATION_INTERVAL        = 5 * 1000;
    public static long FAST_LOCATION_INTERVAL   = 2 * 1000;
    public static String[] permission           = {Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION};

}
