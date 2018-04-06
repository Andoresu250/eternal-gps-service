package com.nanda.infinitebgservice.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.nanda.infinitebgservice.Constants;
import com.nanda.infinitebgservice.activity.MainActivity;
import com.nanda.infinitebgservice.app.AppController;
import com.nanda.infinitebgservice.base.BaseActivity;
import com.nanda.infinitebgservice.events.ServiceRunningEvent;
import com.nanda.infinitebgservice.helper.MainThreadBus;
import com.nanda.infinitebgservice.helper.ObjectWrapperForBinder;

import java.util.Timer;
import java.util.TimerTask;

public class SensorService extends Service implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final String TAG = SensorService.class.getSimpleName();

    /**
     * Contador que que incrementa cada segundo
     */
    public int counter = 0;

    /**
     * Broadcast que envia el contador y la localizacion
     */
    private MainThreadBus bus;
    /**
     * actividad que llama el servicio para posibles hacks
     */
//    private MainActivity activity;
    /**
     * Ubicacion actual
     */
    private Location currentLocation;

    GoogleApiClient mLocationClient;
    @SuppressLint("RestrictedApi")
    LocationRequest mLocationRequest = new LocationRequest();
    LocationManager locationManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            counter = bundle.getInt("counter", counter);
            currentLocation = bundle.getParcelable("Location");
        }
        startTimer();
        initLocationClient();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent("uk.ac.shef.oak.ActivityRecognition.RestartSensor");
        Bundle bundle = new Bundle();
        bundle.putInt("counter", counter);
        bundle.putParcelable("Location", currentLocation);
        broadcastIntent.putExtras(bundle);
        sendBroadcast(broadcastIntent);
        stopTimerTask();
        stopLocationListener();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime = 0;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();
        bus = AppController.getInstance().getBus();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    @SuppressLint("MissingPermission")
    public void initLocationClient(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationRequest.setInterval(Constants.LOCATION_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FAST_LOCATION_INTERVAL);

        int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;

        mLocationRequest.setPriority(priority);
        mLocationClient.connect();
        if(locationManager != null){
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  " + (counter++));
                counter = counter >= 2628000 ? 0 : counter;
                bus.post(new ServiceRunningEvent("Service Running... " + counter + "seconds", currentLocation));
            }
        };
    }

    /**
     * not needed
     */
    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            bus.post(new ServiceRunningEvent("Service Stopped...", currentLocation));
            bus.post(new ServiceRunningEvent("Service Restarted...", currentLocation));
            timer.cancel();
            timer = null;
        }
    }

    public void stopLocationListener(){
        mLocationClient.disconnect();
        mLocationClient = null;
        mLocationRequest = null;
        locationManager = null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //LocationService

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mLocationClient, mLocationRequest, this);
        Log.d(TAG, "Connected to Google API");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Failed to connect to Google API");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        if (location != null) {
            currentLocation = location;
        }
    }

}