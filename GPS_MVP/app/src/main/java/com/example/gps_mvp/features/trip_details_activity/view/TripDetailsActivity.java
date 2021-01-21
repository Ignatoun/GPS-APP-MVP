package com.example.gps_mvp.features.trip_details_activity.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gps_mvp.cache.Cache;
import com.example.gps_mvp.common.Parcelable.LocationParcelable;
import com.example.gps_mvp.databinding.ActivityTripDetailsBinding;
import com.example.gps_mvp.features.Presenter;
import com.example.gps_mvp.features.trip_details_activity.presenter.TripDetailsPresenter;
import com.example.gps_mvp.features.trip_details_activity.presenter.TripDetailsPresenterImpl;

import java.util.ArrayList;
import java.util.UUID;

public class TripDetailsActivity extends AppCompatActivity implements TripDetailsView {

    private ActivityTripDetailsBinding activityTripDetailsBinding;

    private TripDetailsPresenter presenter;

    ArrayList<LocationParcelable> locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityTripDetailsBinding = ActivityTripDetailsBinding.inflate(getLayoutInflater());
        View view = activityTripDetailsBinding.getRoot();
        setContentView(view);
        presenter = new TripDetailsPresenterImpl(this);

        activityTripDetailsBinding.btnMap
                .setOnClickListener(v -> showTripMap());

        if(savedInstanceState == null) {
            showReceivedData();
        }

    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("presenter_uuid", presenter.getUuid().toString());
        cachePresenter(presenter);

        // TripName EditText
        outState.putString("tripName", activityTripDetailsBinding.tvTripName.getText().toString());

        // StartFinish EditText
        outState.putString("startFinish", activityTripDetailsBinding.tvStartFinish.getText().toString());

        // Locations EditText
        outState.putString("locations", activityTripDetailsBinding.tvLocations.getText().toString());

        // Locations List
        outState.putParcelableArrayList("locationsList", locations);

        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restorePresenter(UUID.fromString(savedInstanceState.getString("presenter_uuid")));

        // TripName EditText
        activityTripDetailsBinding.tvTripName
                .setText(savedInstanceState.getString("tripName"));

        // StartFinish EditText
        activityTripDetailsBinding.tvStartFinish
                .setText(savedInstanceState.getString("startFinish"));

        // Locations EditText
        activityTripDetailsBinding.tvLocations
                .setText(savedInstanceState.getString("locations"));

        // Locations List
        locations = savedInstanceState.getParcelableArrayList("locationsList");

        super.onRestoreInstanceState(savedInstanceState);
    }



    @Override
    public void showTripDetails(String tripName, String startFinishString, String locationsString) {
        activityTripDetailsBinding.tvTripName.setText(tripName);
        activityTripDetailsBinding.tvStartFinish.setText(startFinishString);
        activityTripDetailsBinding.tvLocations.setText(locationsString);
    }



    @Override
    public void showTripMap() {
        presenter.showTripMap(getApplicationContext(), locations);
    }



    @Override
    public void showReceivedData() {
        Intent intent = getIntent();
        locations = intent.getParcelableArrayListExtra("locations");
        presenter.showReceivedData(getApplicationContext(), intent);
    }



    @Override
    public void cachePresenter(Presenter presenter) {
        Cache.getInstance().cachePresenterFor(presenter.getUuid(), presenter);
    }



    @Override
    public void restorePresenter(UUID uuid) {
        presenter = (TripDetailsPresenter) Cache.getInstance().restorePresenterFor(uuid);
        presenter.setScreen(this);
    }
}