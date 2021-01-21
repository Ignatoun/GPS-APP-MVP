package com.example.gps_mvp.features.trip_map_activity.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

import com.example.gps_mvp.R;
import com.example.gps_mvp.cache.Cache;
import com.example.gps_mvp.databinding.ActivityMapBinding;
import com.example.gps_mvp.features.Presenter;
import com.example.gps_mvp.features.trip_map_activity.presenter.MapPresenter;
import com.example.gps_mvp.features.trip_map_activity.presenter.MapPresenterImpl;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.UUID;

public class MapActivity extends AppCompatActivity implements com.example.gps_mvp.features.trip_map_activity.view.MapView {

    private ActivityMapBinding activityMapBinding;

    private MapPresenter presenter;

    ArrayList<GeoPoint> geoPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMapBinding = ActivityMapBinding.inflate(getLayoutInflater());
        View view = activityMapBinding.getRoot();
        setContentView(view);
        presenter = new MapPresenterImpl(this);

        configureMapView();

        if(savedInstanceState == null) {
            geoPointList = getIntent().getParcelableArrayListExtra("geoPointList");
            presenter.showMap(geoPointList);
        }
    }



    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("presenter_uuid", presenter.getUuid().toString());
        cachePresenter(presenter);

        // GeoPointList
        outState.putParcelableArrayList("geoPointList", geoPointList);

        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restorePresenter(UUID.fromString(savedInstanceState.getString("presenter_uuid")));

        // GeoPointList
        geoPointList = savedInstanceState.getParcelableArrayList("geoPointList");
        drawRoute(geoPointList);

        super.onRestoreInstanceState(savedInstanceState);
    }



    private void configureMapView() {
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx,
                PreferenceManager.getDefaultSharedPreferences(ctx));


        activityMapBinding.map.setTileSource(TileSourceFactory.MAPNIK);
        activityMapBinding.map.setMultiTouchControls(true);

        requestPermissionsIfNecessary(new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        });
    }



    @Override
    public void drawRoute(ArrayList<GeoPoint> geoPointList) {
        IMapController mapController = activityMapBinding.map.getController();
        mapController.setZoom(18);
        mapController.setCenter(geoPointList.get(0));

        Polyline line = new Polyline();
        line.setTitle("trip");
        line.setColor(R.color.colorAccent);
        line.setRelatedObject(this);
        line.setPoints(geoPointList);
        line.setGeodesic(true);
        activityMapBinding.map.getOverlayManager().add(line);
        activityMapBinding.map.invalidate();
    }



    @Override
    public void cachePresenter(Presenter presenter) {
        Cache.getInstance().cachePresenterFor(presenter.getUuid(), presenter);
    }



    @Override
    public void restorePresenter(UUID uuid) {
        presenter = (MapPresenter) Cache.getInstance().restorePresenterFor(uuid);
        presenter.setScreen(this);
    }
}