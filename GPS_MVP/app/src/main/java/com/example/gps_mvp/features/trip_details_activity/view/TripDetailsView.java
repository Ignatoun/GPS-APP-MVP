package com.example.gps_mvp.features.trip_details_activity.view;

import com.example.gps_mvp.features.Screen;

public interface TripDetailsView extends Screen {

    void showTripDetails(String tripName, String startDate, String finishDate);

    void showTripMap();

    void showReceivedData();

}
