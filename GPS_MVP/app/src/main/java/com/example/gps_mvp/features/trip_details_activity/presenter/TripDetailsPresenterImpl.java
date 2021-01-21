package com.example.gps_mvp.features.trip_details_activity.presenter;

import android.content.Context;
import android.content.Intent;

import com.example.gps_mvp.R;
import com.example.gps_mvp.common.Parcelable.LocationParcelable;
import com.example.gps_mvp.features.Screen;
import com.example.gps_mvp.features.trip_details_activity.view.TripDetailsView;
import com.example.gps_mvp.features.trip_map_activity.view.MapActivity;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.UUID;

public class TripDetailsPresenterImpl implements TripDetailsPresenter {

    private final UUID uuid;
    private TripDetailsView view;

    public TripDetailsPresenterImpl(TripDetailsView view) {
        this.uuid = UUID.randomUUID();
        this.view = view;
    }

    @Override
    public void showReceivedData(Context applicationContext, Intent intent) {
        String tripName = intent.getStringExtra("tripName");
        String startDate = intent.getStringExtra("startDate");
        String finishDate = intent.getStringExtra("finishDate");


        ArrayList<LocationParcelable> locations = intent.getParcelableArrayListExtra("locations");


        StringBuilder locationsData = new StringBuilder();
        for(int i=0; i<locations.size(); i++) {
            locationsData.append("\t\t").append(i + 1).append(". Latitude: ")
                    .append(locations.get(i).getLatitude()).append("; Longitude: ")
                    .append(locations.get(i).getLongitude()).append(";\n");
        }


        String startFinishString = applicationContext.getString(R.string.start_date) +
                startDate + applicationContext.getString(R.string.finish_date) +
                finishDate + applicationContext.getString(R.string.semicolon);
        String locationsString = applicationContext.getString(R.string.locations) + locationsData;


        // Show trip details
        view.showTripDetails(tripName, startFinishString, locationsString);
    }

    @Override
    public void showTripMap(Context applicationContext, ArrayList<LocationParcelable> locations) {
        Intent intent = new Intent(applicationContext, MapActivity.class);

        ArrayList<GeoPoint> geoPointList = new ArrayList<>();
        for(int i=0; i<locations.size(); i++) {
            double latitude = Double.parseDouble(locations.get(i).getLatitude());
            double longitude = Double.parseDouble(locations.get(i).getLongitude());
            geoPointList.add(new GeoPoint(latitude, longitude));
        }

        intent.putParcelableArrayListExtra("geoPointList", geoPointList);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        applicationContext.startActivity(intent);
    }

    @Override
    public void setScreen(Screen screen) {
        this.view = (TripDetailsView) screen;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }
}
