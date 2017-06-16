package com.nanda.infinitebgservice.activity;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nanda.infinitebgservice.R;
import com.nanda.infinitebgservice.app.AppController;
import com.nanda.infinitebgservice.base.BaseActivity;
import com.nanda.infinitebgservice.events.ServiceRunningEvent;
import com.nanda.infinitebgservice.service.SensorService;
import com.squareup.otto.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_bg_service_status)
    TextView tvBgServiceStatus;
    @BindView(R.id.btn_start_service)
    Button btnStartService;
    @BindView(R.id.btn_stop_service)
    Button btnStopService;
    private SensorService mSensorService;
    private Intent mServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AppController.getInstance().getBus().register(this);

        mSensorService = new SensorService();
        mServiceIntent = new Intent(this, mSensorService.getClass());
    }

    @Subscribe
    public void onServiceStart(ServiceRunningEvent event) {
        if (event != null && event.getTime() != null && !event.getTime().isEmpty()) {
            tvBgServiceStatus.setText(event.getTime());
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

    @OnClick({R.id.btn_start_service, R.id.btn_stop_service})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_service:
                if (!isMyServiceRunning(mSensorService.getClass())) {
                    startService(mServiceIntent);
                }
                break;
            case R.id.btn_stop_service:
                stopService(mServiceIntent);
                break;
        }
    }
}


