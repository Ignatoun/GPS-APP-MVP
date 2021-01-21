package com.example.gps_mvp.features.trip_map_activity.presenter;

import com.example.gps_mvp.features.Presenter;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public interface MapPresenter extends Presenter {

    void showMap(ArrayList<GeoPoint> geoPointList);

}
