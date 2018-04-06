package com.nanda.infinitebgservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.nanda.infinitebgservice.activity.MainActivity;
import com.nanda.infinitebgservice.helper.ObjectWrapperForBinder;
import com.nanda.infinitebgservice.service.SensorService;

/**
 * Created by Nandagopal on 6/15/17.
 */
public class SensorRestartReceiver extends BroadcastReceiver {

    String TAG = SensorRestartReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Service Stopped!");
        Log.i(TAG, "onReceive: service started i guess from receiver ");
        Bundle bundle = intent.getExtras();
        Intent serviceIntent = new Intent(context, SensorService.class);
        if(bundle != null){
            serviceIntent.putExtras(bundle);
//            Object object = (bundle.getBinder("MainActivity")) != null ? ((ObjectWrapperForBinder) bundle.getBinder("MainActivity")).getData() : null;
//            if(object != null && object instanceof MainActivity){
//                Log.i(TAG, "onReceive: MainActivity OK");
//            }else{
//                Log.i(TAG, "onReceive: MainActivity BAD");
//            }
        }
        context.startService(serviceIntent);
    }

}
