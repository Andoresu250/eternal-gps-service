package com.nanda.infinitebgservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.nanda.infinitebgservice.service.SensorService;

/**
 * Created by Nandagopal on 6/15/17.
 */
public class SensorRestartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestartReceiver.class.getSimpleName(), "Service Stopped!");
        context.startService(new Intent(context, SensorService.class));
    }

}
