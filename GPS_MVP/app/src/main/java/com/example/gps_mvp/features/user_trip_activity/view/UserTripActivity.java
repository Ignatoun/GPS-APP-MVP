package com.example.gps_mvp.features.user_trip_activity.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.gps_mvp.cache.Cache;
import com.example.gps_mvp.common.Parcelable.LocationParcelable;
import com.example.gps_mvp.common.Parcelable.TripParcelable;
import com.example.gps_mvp.databinding.ActivityUserTripBinding;
import com.example.gps_mvp.features.Presenter;
import com.example.gps_mvp.features.user_trip_activity.presenter.UserTripPresenter;
import com.example.gps_mvp.features.user_trip_activity.presenter.UserTripPresenterImpl;
import com.example.models.Location;
import com.example.models.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserTripActivity extends AppCompatActivity implements UserTripActivityView {

    private UserTripPresenter presenter;

    private ActivityUserTripBinding activityUserTripBinding;

    private List<Trip> trips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUserTripBinding = ActivityUserTripBinding.inflate(getLayoutInflater());
        View view = activityUserTripBinding.getRoot();
        setContentView(view);
        presenter = new UserTripPresenterImpl(this);

        if(savedInstanceState == null) {
            requestToDisplayUsersTrips();
        }
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("presenter_uuid", presenter.getUuid().toString());
        cachePresenter(presenter);

        // ListView Visibility
        outState.putInt("isListViewVisible", activityUserTripBinding.listViewTrips.getVisibility());

        // ErrorLayout
        outState.putInt("isErrorLayoutVisible", activityUserTripBinding.errorLayout.getVisibility());

        // Post Data Progress Bar Visibility
        outState.putInt("isProgressBarVisible", activityUserTripBinding.pBar.getVisibility());

        // List of trips
        if(trips != null) {
            ArrayList<TripParcelable> tripsToStore = convertToParcelable(trips);
            outState.putParcelableArrayList("trips", tripsToStore);
        }

        super.onSaveInstanceState(outState);

    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restorePresenter(UUID.fromString(savedInstanceState.getString("presenter_uuid")));

        // ListView Visibility
        activityUserTripBinding.listViewTrips
                .setVisibility(savedInstanceState.getInt("isListViewVisible") == View.VISIBLE ? View.VISIBLE : View.GONE);

        // ErrorLayout
        activityUserTripBinding.errorLayout
                .setVisibility(savedInstanceState.getInt("isErrorLayoutVisible") == View.VISIBLE ? View.VISIBLE : View.GONE);

        //
        activityUserTripBinding.pBar
                .setVisibility(savedInstanceState.getInt("isProgressBarVisible") == View.VISIBLE ? View.VISIBLE : View.GONE);

        // List of trips
        trips = convertFromParcelable(savedInstanceState.getParcelableArrayList("trips"));
        postUsersTrips(trips);

        super.onRestoreInstanceState(savedInstanceState);

    }



    private void requestToDisplayUsersTrips() {
        String username = getIntent().getStringExtra("username");
        presenter.displayTrips(username, getApplicationContext());
    }



    @Override
    public void postUsersTrips(List<Trip> trips) {

        this.trips = trips;

        TripListAdapter adapter = new TripListAdapter(UserTripActivity.this, trips);

        ArrayList<TripParcelable> tripParcelableArrayList = convertToParcelable(trips);

        activityUserTripBinding.listViewTrips.setOnItemClickListener((parent, view, position, id) -> presenter
                .showClickedTripDetails(getApplicationContext(), tripParcelableArrayList.get(position)));
        activityUserTripBinding.listViewTrips.setAdapter(adapter);
        activityUserTripBinding.pBar.setVisibility(View.GONE);
        activityUserTripBinding.listViewTrips.setVisibility(View.VISIBLE);

    }

    @Override
    public void showProgressBar() {
        activityUserTripBinding.listViewTrips.setVisibility(View.GONE);
        activityUserTripBinding.pBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayError() {
        activityUserTripBinding.pBar.setVisibility(View.GONE);
        activityUserTripBinding.errorLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        activityUserTripBinding.pBar.setVisibility(View.GONE);
    }


    @Override
    public void cachePresenter(Presenter presenter) {
        Cache.getInstance().cachePresenterFor(presenter.getUuid(), presenter);
    }



    @Override
    public void restorePresenter(UUID uuid) {
        presenter = (UserTripPresenter) Cache.getInstance().restorePresenterFor(uuid);
        presenter.setScreen(this);
    }



    @Override
    protected void onStop() {
        super.onStop();
        presenter.cancelTask();
    }



    private ArrayList<TripParcelable> convertToParcelable(List<Trip> trips) {
        ArrayList<TripParcelable> tripsToStore = new ArrayList<>();

        for(Trip trip : trips) {

            ArrayList<LocationParcelable> locationParcelableArrayList = new ArrayList<>();
            for(Location location: trip.getLocations()) {
                LocationParcelable locationParcelable = new LocationParcelable();
                locationParcelable.setId(location.getId());
                locationParcelable.setLatitude(location.getLatitude());
                locationParcelable.setLongitude(location.getLongitude());
                locationParcelableArrayList.add(locationParcelable);
            }

            TripParcelable tripParcelable = new TripParcelable();
            tripParcelable.setId(trip.getId());
            tripParcelable.setName(trip.getName());
            tripParcelable.setStartDate(trip.getStartDate());
            tripParcelable.setFinishDate(trip.getFinishDate());
            tripParcelable.setLocations(locationParcelableArrayList);
            tripsToStore.add(tripParcelable);
        }

        return tripsToStore;
    }



    private List<Trip> convertFromParcelable(ArrayList<TripParcelable> trips) {
        List<Trip> tripsToRestore = new ArrayList<>();

        for(TripParcelable tripParcelable : trips) {

            List<Location> locationList = new ArrayList<>();
            for(LocationParcelable locationParcelable: tripParcelable.getLocations()) {
                Location location = new Location();
                location.setId(locationParcelable.getId());
                location.setLatitude(locationParcelable.getLatitude());
                location.setLongitude(locationParcelable.getLongitude());
                locationList.add(location);
            }

            Trip trip = new Trip();
            trip.setId(tripParcelable.getId());
            trip.setName(tripParcelable.getName());
            trip.setStartDate(tripParcelable.getStartDate());
            trip.setFinishDate(tripParcelable.getFinishDate());
            trip.setLocations(locationList);
            tripsToRestore.add(trip);
        }

        return tripsToRestore;
    }
}