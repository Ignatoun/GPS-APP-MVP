package com.example.gps_mvp.features.trip_map_activity.presenter;

import com.example.gps_mvp.features.Screen;
import com.example.gps_mvp.features.trip_map_activity.view.MapView;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.UUID;

public class MapPresenterImpl implements MapPresenter {

    private final UUID uuid;
    private MapView view;

    public MapPresenterImpl(MapView view) {
        this.uuid = UUID.randomUUID();
        this.view = view;
    }



    @Override
    public void showMap(ArrayList<GeoPoint> geoPointList) {
        view.drawRoute(geoPointList);
    }



    @Override
    public void setScreen(Screen screen) {
        this.view = (MapView) screen;
    }



    @Override
    public UUID getUuid() {
        return uuid;
    }

}
