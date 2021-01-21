package com.example.gps_mvp.features.main.presenter;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gps_mvp.common.Constants;
import com.example.gps_mvp.features.Screen;
import com.example.gps_mvp.features.main.model.LocationService;
import com.example.gps_mvp.features.main.model.MainUseCases;
import com.example.gps_mvp.features.main.view.MainView;
import com.example.gps_mvp.features.user_trip_activity.view.UserTripActivity;
import com.example.gps_mvp.sqlite.SQLiteDBHelper;
import com.example.models.Location;
import com.example.models.Trip;
import com.example.models.UserTrip;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainPresenterImpl implements MainPresenter {

    BroadcastReceiver broadcastReceiver;

    private final UUID uuid;
    private MainView view;
    private final MainUseCases model;

    StopLocationScanTask stopLocationScanTask = null;

    public MainPresenterImpl(MainView view) {
        this.uuid = UUID.randomUUID();
        this.view = view;
        this.model = new MainUseCases();
    }



    @Override
    public void startLocationScan(Context applicationContext, ActivityManager systemService) {
        if(!isLocationServiceRunning(systemService)) {
            Intent intent = new Intent(applicationContext, LocationService.class);
            intent.setAction(Constants.ACTION_START_LOCATION_SERVICE);
            applicationContext.startService(intent);
        }

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String latitude = intent.getStringExtra("latitude");
                String longitude = intent.getStringExtra("longitude");
                view.postLocation(latitude, longitude);
            }
        };

        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(broadcastReceiver,
                new IntentFilter("location"));
    }



    @Override
    public void stopLocationScan(Context applicationContext, ActivityManager systemService,
                                 boolean pause, String username, String tripName) {
        if (isLocationServiceRunning(systemService) || pause) {

            // StopLocationService Intent
            Intent intent = new Intent(applicationContext, LocationService.class);
            intent.setAction(Constants.ACTION_STOP_LOCATION_SERVICE);
            applicationContext.startService(intent);

            SQLiteDBHelper dbHelper = new SQLiteDBHelper(applicationContext);
            // Send request to server
            stopLocationScanTask = new StopLocationScanTask(model, dbHelper, username, tripName, view);
            stopLocationScanTask.execute();
        }
    }



    private static class StopLocationScanTask extends AsyncTask<Void, Void, List<UserTrip>> {

        MainUseCases model;
        String username;
        SQLiteDBHelper dbHelper;
        String tripName;
        MainView view;

        public StopLocationScanTask(MainUseCases model, SQLiteDBHelper dbHelper, String username, String tripName, MainView view) {
            this.model = model;
            this.username = username;
            this.dbHelper = dbHelper;
            this.tripName = tripName;
            this.view = view;
        }

        @Override
        protected void onPreExecute() {

            if(isCancelled()) return;

            view.muteButtonsWhilePerformingRequest();
            view.showProgressBar();

        }

        @Override
        protected List<UserTrip> doInBackground(Void... params) {
            List<UserTrip> users = model.getAllUsers();
            Log.d("USER", users.toString());
            return users;
        }

        @Override
        protected void onPostExecute(List<UserTrip> users) {
            super.onPostExecute(users);

            if(users.size() < 1) {

                view.postStopLocationScan();
                view.toastPostError();

            } else {

                // Check if user already exists
                boolean ifUserExists = false;
                int userIndex = 0;
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserName().equals(username)) {
                        ifUserExists = true;
                        userIndex = i;
                        break;
                    }
                }

                if (ifUserExists) {

                    final UserTrip user = addTripToUser(users.get(userIndex), tripName, dbHelper);
                    updateUser(user, model);

                } else {

                    final UserTrip user = addTripToUser(new UserTrip(username), tripName, dbHelper);
                    postUser(user, model);

                }

                view.postStopLocationScan();
                view.toastSuccess();
            }
        }
    }


    @Override
    public void cancelTask() {
        if(stopLocationScanTask != null) {
            stopLocationScanTask.cancel(true);
        }
    }

    @Override
    public void clearLocalDatabase(Context applicationContext) {
        applicationContext.deleteDatabase(SQLiteDBHelper.DATABASE_NAME);
    }



    private static void postUser(UserTrip user, MainUseCases model) {
        new PostUserTask(user, model).execute();
    }



    private static void updateUser(UserTrip user, MainUseCases model) {
        new UpdateUserTask(user, model).execute();
    }



    private static UserTrip addTripToUser(UserTrip user, String tripName, SQLiteDBHelper dbHelper) {
        List<String> dateList = new ArrayList<>();
        final Trip trip = new Trip(tripName);

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        final Cursor cursor = sqLiteDatabase.query(SQLiteDBHelper.LOCATION_TABLE, null,
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            //int id = cursor.getColumnIndex(SQLiteDBHelper.KEY_ID);
            final int latitudeId = cursor.getColumnIndex(SQLiteDBHelper.KEY_LATITUDE);
            final int longitudeId = cursor.getColumnIndex(SQLiteDBHelper.KEY_LONGITUDE);
            final int dateId = cursor.getColumnIndex(SQLiteDBHelper.KEY_DATE);
            do {

                final String latitude = cursor.getString(latitudeId);
                final String longitude = cursor.getString(longitudeId);
                final String date = cursor.getString(dateId);

                trip.getLocations().add(new Location(latitude, longitude));
                Log.d("DATE", date);
                dateList.add(date);

            } while (cursor.moveToNext());

        } else {
            Log.i("Error.POST", "Something went wrong");
        }
        cursor.close();
        dbHelper.close();

        trip.setStartDate(dateList.get(0));
        trip.setFinishDate(dateList.get(dateList.size() - 1));
        user.getTrips().add(trip);

        return user;
    }



    private static class UpdateUserTask extends AsyncTask<Void, Void, UserTrip> {

        UserTrip user;
        MainUseCases model;

        public UpdateUserTask(UserTrip user, MainUseCases model) {
            this.user = user;
            this.model = model;
        }

        @Override
        protected UserTrip doInBackground(Void... voids) {
            model.updateUser(user);
            return user;
        }
    }



    private static class PostUserTask extends AsyncTask<Void, Void, UserTrip> {

        UserTrip user;
        MainUseCases model;

        public PostUserTask(UserTrip user, MainUseCases model) {
            this.user = user;
            this.model = model;
        }

        @Override
        protected UserTrip doInBackground(Void... voids) {
            model.postUser(user);
            return user;
        }
    }



    @Override
    public void showLocalDatabase(Context applicationContext, String tripName) {
        Intent intent = new Intent(applicationContext, LocationService.class);
        intent.setAction(Constants.SHOW_DATABASE);
        intent.putExtra("tripName", tripName);
        applicationContext.startService(intent);
    }



    @Override
    public void pauseLocationScan(Context applicationContext) {
        Intent intent = new Intent(applicationContext, LocationService.class);
        intent.setAction(Constants.ACTION_PAUSE_LOCATION_SERVICE);
        applicationContext.startService(intent);

        view.postPauseLocationScan();
    }



    @Override
    public void resumeLocationScan(Context applicationContext) {
        Intent intent = new Intent(applicationContext, LocationService.class);
        intent.setAction(Constants.ACTION_RESUME_LOCATION_SERVICE);
        applicationContext.startService(intent);

        view.postResumeLocationScan();
    }



    @Override
    public void showUsersTrips(Context applicationContext, String username) {
        Intent tripIntent = new Intent(applicationContext, UserTripActivity.class);
        tripIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        tripIntent.putExtra("username", username);
        applicationContext.startActivity(tripIntent);
    }



    private boolean isLocationServiceRunning(ActivityManager systemService) {
        if (systemService != null) {
            for (ActivityManager.RunningServiceInfo service :
                    systemService.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }



    @Override
    public void setScreen(Screen screen) {
        this.view = (MainView) screen;
    }



    @Override
    public UUID getUuid() {
        return uuid;
    }

}
