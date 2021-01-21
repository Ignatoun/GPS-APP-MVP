package com.example.gps_mvp.features.trip_map_activity.view;

import com.example.gps_mvp.features.Screen;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;

public interface MapView extends Screen {

    void drawRoute(ArrayList<GeoPoint> geoPointList);

}
