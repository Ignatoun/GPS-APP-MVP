package com.example.gps_mvp.features.trip_details_activity.presenter;

import android.content.Context;
import android.content.Intent;

import com.example.gps_mvp.common.Parcelable.LocationParcelable;
import com.example.gps_mvp.features.Presenter;

import java.util.ArrayList;

public interface TripDetailsPresenter extends Presenter {

    void showReceivedData(Context applicationContext, Intent intent);

    void showTripMap(Context applicationContext, ArrayList<LocationParcelable> locations);

}
