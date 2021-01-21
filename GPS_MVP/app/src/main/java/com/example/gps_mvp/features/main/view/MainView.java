package com.example.gps_mvp.features.main.view;

import com.example.gps_mvp.features.Screen;

public interface MainView extends Screen {

    void startLocationScan();

    void pauseResumeLocationScan();

    void stopLocationScan();

    void showUsersTrips();

    void showLocalDatabase();

    void postLocation(String latitude, String longitude);

    void postStopLocationScan();

    void postPauseLocationScan();

    void postResumeLocationScan();

    void showProgressBar();

    void toastSuccess();

    void toastPostError();

    void muteButtonsWhilePerformingRequest();

}
