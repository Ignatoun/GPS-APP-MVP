package com.example.gps_mvp.features.user_trip_activity.view;

import com.example.gps_mvp.features.Screen;
import com.example.models.Trip;
import com.example.models.UserTrip;

import java.util.List;

public interface UserTripActivityView extends Screen {

    void postUsersTrips(List<Trip> trips);

    void showProgressBar();

    void displayError();

    void hideProgressBar();

}
