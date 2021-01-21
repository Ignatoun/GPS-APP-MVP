package com.example.gps_mvp.features.user_trip_activity.presenter;

import android.content.Context;

import com.example.gps_mvp.common.Parcelable.TripParcelable;
import com.example.gps_mvp.features.Presenter;
import com.example.models.UserTrip;

public interface UserTripPresenter extends Presenter {

    void displayTrips(String username, Context applicationContext);

    void showClickedTripDetails(Context applicationContext, TripParcelable trip);

    void cancelTask();

    void restoreState(UserTrip user);

}
