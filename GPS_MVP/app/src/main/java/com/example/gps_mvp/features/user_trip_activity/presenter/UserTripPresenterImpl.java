package com.example.gps_mvp.features.user_trip_activity.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.gps_mvp.common.Parcelable.LocationParcelable;
import com.example.gps_mvp.common.Parcelable.TripParcelable;
import com.example.gps_mvp.features.Screen;
import com.example.gps_mvp.features.trip_details_activity.view.TripDetailsActivity;
import com.example.gps_mvp.features.user_trip_activity.model.UserTripUseCases;
import com.example.gps_mvp.features.user_trip_activity.view.UserTripActivityView;
import com.example.models.Trip;
import com.example.models.UserTrip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserTripPresenterImpl implements UserTripPresenter {

    private final UUID uuid;
    private UserTripActivityView view;
    private final UserTripUseCases model;

    GetUserTask getUserTask = null;

    public UserTripPresenterImpl(UserTripActivityView view) {
        this.uuid = UUID.randomUUID();
        this.view = view;
        this.model = new UserTripUseCases();
    }



    @Override
    public void displayTrips(String username, Context applicationContext) {
        getUserTask = new GetUserTask(model ,view, username);
        getUserTask.execute();
    }



    public static class GetUserTask extends AsyncTask<Void, Void, UserTrip> {

        UserTripUseCases model;
        UserTripActivityView view;
        String username;

        public GetUserTask(UserTripUseCases model, UserTripActivityView view, String username) {
            this.model = model;
            this.view = view;
            this.username = username;
        }

        @Override
        protected void onPreExecute() {
            view.showProgressBar();
        }

        @Override
        protected UserTrip doInBackground(Void... params) {
            if(isCancelled()) return null;
            UserTrip user = model.getUserByName(username);
            Log.d("USER", user.toString());
            return user;
        }

        @Override
        protected void onPostExecute(UserTrip user) {
            super.onPostExecute(user);

            if(user.getUserName().equals("error") && user.getTrips().size() < 1) {
                view.displayError();
            } else {
                List<Trip> trips = user.getTrips();
                view.postUsersTrips(trips);
            }
        }
    }

    @Override
    public void cancelTask() {
        if(getUserTask != null) {
            getUserTask.cancel(true);
        }
    }

    @Override
    public void restoreState(UserTrip user) {
        if(user.getUserName().equals("error") && user.getTrips().size() < 1) {
            view.displayError();
        } else {
            ArrayList<Trip> trips = (ArrayList<Trip>) user.getTrips();
            view.postUsersTrips(trips);
        }
    }



    @Override
    public void showClickedTripDetails(Context applicationContext, TripParcelable trip) {
        Intent intent = new Intent(applicationContext, TripDetailsActivity.class);
        intent.putExtra("tripName", trip.getName());
        intent.putExtra("startDate", trip.getStartDate());
        intent.putExtra("finishDate", trip.getFinishDate());

        ArrayList<LocationParcelable> locations = (ArrayList<LocationParcelable>) trip.getLocations();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putParcelableArrayListExtra("locations", locations);

        applicationContext.startActivity(intent);
    }



    @Override
    public void setScreen(Screen screen) {
        this.view = (UserTripActivityView) screen;
    }



    @Override
    public UUID getUuid() {
        return uuid;
    }

}
