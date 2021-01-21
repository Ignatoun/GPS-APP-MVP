package com.example.gps_mvp.features.main.presenter;

import android.app.ActivityManager;
import android.content.Context;

import com.example.gps_mvp.features.Presenter;

public interface MainPresenter extends Presenter {

    void startLocationScan(Context context, ActivityManager systemService);

    void stopLocationScan(Context applicationContext, ActivityManager systemService, boolean pause, String username, String tripName);

    void showLocalDatabase(Context applicationContext, String tripName);

    void pauseLocationScan(Context applicationContext);

    void resumeLocationScan(Context applicationContext);

    void showUsersTrips(Context applicationContext, String username);

    void clearLocalDatabase(Context applicationContext);

    void cancelTask();

}
