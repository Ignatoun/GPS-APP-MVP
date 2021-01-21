package com.example.gps_mvp.features.main.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.gps_mvp.R;
import com.example.gps_mvp.cache.Cache;
import com.example.gps_mvp.common.GPSUtils;
import com.example.gps_mvp.databinding.ActivityMainBinding;
import com.example.gps_mvp.features.Presenter;
import com.example.gps_mvp.features.main.presenter.MainPresenter;
import com.example.gps_mvp.features.main.presenter.MainPresenterImpl;
import com.example.gps_mvp.sqlite.SQLiteDBHelper;

import java.util.UUID;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter presenter;

    boolean pause = false;

    private static final int REQUEST_CODE_PERMISSION = 21452;

    private ActivityMainBinding activityMainBinding;

    private boolean isGPS = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        presenter = new MainPresenterImpl(this);

        activityMainBinding.btnStart
                .setOnClickListener(v -> startLocationScan());

        activityMainBinding.btnPauseResumeLocation
                .setOnClickListener(v -> pauseResumeLocationScan());

        activityMainBinding.btnStopLocation
                .setOnClickListener(v -> stopLocationScan());

        activityMainBinding.btnTrips
                .setOnClickListener(v -> showUsersTrips());

        activityMainBinding.btnDatabase
                .setOnClickListener(v -> showLocalDatabase());

        if(savedInstanceState == null) {
            getApplicationContext().deleteDatabase(SQLiteDBHelper.DATABASE_NAME);
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("presenter_uuid", presenter.getUuid().toString());
        cachePresenter(presenter);

        // Is GPS Enabled
        outState.putBoolean("isGPSEnabled", isGPS);

        // Longitude TextView
        outState.putString("longitude", activityMainBinding.tvLongitude.getText().toString());
        outState.putInt("longitudeVisibility", activityMainBinding.tvLongitude.getVisibility());

        // Longitude TextView
        outState.putString("latitude", activityMainBinding.tvLatitude.getText().toString());
        outState.putInt("latitudeVisibility", activityMainBinding.tvLatitude.getVisibility());

        // Username EditText
        outState.putString("username", activityMainBinding.editTextPersonName.getText().toString());
        outState.putBoolean("isUsernameEnabled", activityMainBinding.editTextPersonName.isEnabled());
        outState.putInt("isUsernameVisible", activityMainBinding.editTextPersonName.getVisibility());

        // TripName EditText
        outState.putString("tripName", activityMainBinding.editTextTripName.getText().toString());
        outState.putBoolean("isTripNameEnabled", activityMainBinding.editTextTripName.isEnabled());
        outState.putInt("isTripNameVisible", activityMainBinding.editTextTripName.getVisibility());

        // StartLocationScan Button
        outState.putBoolean("btnStartLocationScanIsEnabled", activityMainBinding.btnStart.isEnabled());
        outState.putBoolean("btnStartLocationIsClickable", activityMainBinding.btnStart.isClickable());

        // PauseResumeLocationScan Button
        outState.putBoolean("btnPauseResumeLocationScanIsEnabled", activityMainBinding.btnPauseResumeLocation.isEnabled());
        outState.putString("btnPauseResumeLocationScanText", activityMainBinding.btnPauseResumeLocation.getText().toString());
        outState.putBoolean("isServicePausedOrResumed", pause);

        // StopLocationScan Button
        outState.putBoolean("btnStopLocationScanIsEnabled", activityMainBinding.btnStopLocation.isEnabled());

        // Post Data Progress Bar Visibility
        outState.putInt("isProgressBarVisible", activityMainBinding.pBar.getVisibility());

        // ShowUsersTrips Button
        outState.putBoolean("btnShowUsersTripsIsEnabled", activityMainBinding.btnTrips.isEnabled());

        // ShowLocalDatabase Button
        outState.putBoolean("btnShowLocalDatabaseIsEnabled", activityMainBinding.btnDatabase.isEnabled());

        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restorePresenter(UUID.fromString(savedInstanceState.getString("presenter_uuid")));

        // Is GPS Enabled
        isGPS = savedInstanceState.getBoolean("isGPSEnabled");

        // Longitude TextView
        activityMainBinding.tvLongitude
                .setText(savedInstanceState.getString("longitude"));
        activityMainBinding.tvLongitude
                .setVisibility(savedInstanceState.getInt("longitudeVisibility") == View.VISIBLE ? View.VISIBLE : View.GONE);

        // Latitude TextView
        activityMainBinding.tvLatitude
                .setText(savedInstanceState.getString("latitude"));
        activityMainBinding.tvLatitude
                .setVisibility(savedInstanceState.getInt("latitudeVisibility") == View.VISIBLE ? View.VISIBLE : View.GONE);

        // Username EditText
        activityMainBinding.editTextPersonName
                .setText(savedInstanceState.getString("username"));
        activityMainBinding.editTextPersonName
                .setEnabled(savedInstanceState.getBoolean("isUsernameEnabled"));
        activityMainBinding.editTextPersonName
                .setVisibility(savedInstanceState.getInt("isUsernameVisible") == View.VISIBLE ? View.VISIBLE : View.GONE);

        // TripName EditText
        activityMainBinding.editTextTripName
                .setText(savedInstanceState.getString("tripName"));
        activityMainBinding.editTextTripName
                .setEnabled(savedInstanceState.getBoolean("isTripNameEnabled"));
        activityMainBinding.editTextTripName
                .setVisibility(savedInstanceState.getInt("isTripNameVisible") == View.VISIBLE ? View.VISIBLE : View.GONE);

        // StartLocationScan Button
        activityMainBinding.btnStart
                .setEnabled(savedInstanceState.getBoolean("btnStartLocationScanIsEnabled"));
        activityMainBinding.btnStart
                .setClickable(savedInstanceState.getBoolean("btnStartLocationScanIsClickable"));

        // PauseResumeLocationScan Button
        activityMainBinding.btnPauseResumeLocation
                .setEnabled(savedInstanceState.getBoolean("btnPauseResumeLocationScanIsEnabled"));
        activityMainBinding.btnPauseResumeLocation
                .setText(savedInstanceState.getString("btnPauseResumeLocationScanText"));
        pause = savedInstanceState.getBoolean("isServicePausedOrResumed");

        // StopLocationScan Button
        activityMainBinding.btnStopLocation
                .setEnabled(savedInstanceState.getBoolean("btnStopLocationScanIsEnabled"));

        // Post Data Progress Bar Visibility
        activityMainBinding.pBar
                .setVisibility(savedInstanceState.getInt("isProgressBarVisible") == View.VISIBLE ? View.VISIBLE : View.GONE);

        // ShowUsersTrips Button
        activityMainBinding.btnTrips
                .setEnabled(savedInstanceState.getBoolean("btnShowUsersTripsIsEnabled"));

        // ShowLocalDatabase Button
        activityMainBinding.btnDatabase
                .setEnabled(savedInstanceState.getBoolean("btnShowLocalDatabaseIsEnabled"));

        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void startLocationScan() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        } else if (activityMainBinding.editTextPersonName.getText().toString().equals("") ||
                activityMainBinding.editTextTripName.getText().toString().equals("")){
            hideKeyboard();
            Toast.makeText(getApplicationContext(), "Enter all fields", Toast.LENGTH_SHORT).show();
        } else {
            new GPSUtils(this).turnGPSOn(isGPSEnable -> {
                // turn on GPS
                isGPS = isGPSEnable;
            });
            if (isGPS) {
                presenter.startLocationScan(getApplicationContext(),
                        (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
                hideKeyboard();
            } else {
                Toast.makeText(getApplicationContext(), "Turn on the GPS to start location scan", Toast.LENGTH_SHORT).show();
            }
        }
    }



    @Override
    public void pauseResumeLocationScan() {
        if(!pause) {
            presenter.pauseLocationScan(getApplicationContext());
            pause = true;
        } else {
            presenter.resumeLocationScan(getApplicationContext());
            pause = false;
        }
    }



    @Override
    public void stopLocationScan() {
        isGPS = false;

        String username = activityMainBinding.editTextPersonName.getText().toString();
        String tripName = activityMainBinding.editTextTripName.getText().toString();

        muteButtonsWhilePerformingRequest();

        presenter.stopLocationScan(getApplicationContext(),
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE), pause,
                username, tripName);
    }



    @Override
    public void showUsersTrips() {
        String username = activityMainBinding.editTextPersonName.getText().toString();
        if(username.equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Name field", Toast.LENGTH_SHORT).show();
        } else {
            presenter.showUsersTrips(getApplicationContext(), username);
        }
    }



    @Override
    public void showLocalDatabase() {
        String tripName = activityMainBinding.editTextTripName.getText().toString();
        presenter.showLocalDatabase(getApplicationContext(), tripName);
    }



    @Override
    public void postStopLocationScan() {

        // Latitude & Longitude visibility
        activityMainBinding.tvLatitude.setVisibility(View.GONE);
        activityMainBinding.tvLongitude.setVisibility(View.GONE);

        // StartLocationScanButton
        activityMainBinding.btnStart.setClickable(true);
        activityMainBinding.btnStart.setEnabled(true);

        // PauseResumeLocationScan Button
        activityMainBinding.btnPauseResumeLocation.setClickable(false);
        activityMainBinding.btnPauseResumeLocation.setEnabled(false);

        // StopLocationScan Button
        activityMainBinding.btnStopLocation.setClickable(false);
        activityMainBinding.btnStopLocation.setEnabled(false);

        // Progress Bar
        activityMainBinding.pBar.setVisibility(View.GONE);

        // SQLite Database Delete
        presenter.clearLocalDatabase(getApplicationContext());
    }



    @Override
    public void postPauseLocationScan() {
        activityMainBinding.btnPauseResumeLocation.setText(R.string.resume_location_scan);
        activityMainBinding.tvLatitude.setVisibility(View.GONE);
        activityMainBinding.tvLongitude.setVisibility(View.GONE);
    }



    @Override
    public void postResumeLocationScan() {
        activityMainBinding.btnPauseResumeLocation.setText(getString(R.string.pause_location_scan));
        activityMainBinding.tvLatitude.setVisibility(View.VISIBLE);
        activityMainBinding.tvLongitude.setVisibility(View.VISIBLE);
    }



    @Override
    public void showProgressBar() {
        activityMainBinding.pBar.setVisibility(View.VISIBLE);
    }



    @Override
    public void toastSuccess() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        "Success!", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }



    @Override
    public void toastPostError() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(),
                        "Error. User hasn't been uploaded to server", Toast.LENGTH_SHORT).show();
            }
        }, 2000);
    }

    @Override
    public void muteButtonsWhilePerformingRequest() {

        // StopLocationScan Button
        activityMainBinding.btnStopLocation.setClickable(false);
        activityMainBinding.btnStopLocation.setEnabled(false);
        activityMainBinding.btnStopLocation.setFocusable(false);

        // PauseResumeLocationScan Button
        activityMainBinding.btnPauseResumeLocation.setClickable(false);
        activityMainBinding.btnPauseResumeLocation.setEnabled(false);
        activityMainBinding.btnPauseResumeLocation.setFocusable(false);
    }


    @Override
    public void postLocation(String latitude, String longitude) {

        // Longitude & Latitude visibility
        activityMainBinding.tvLatitude.setVisibility(View.VISIBLE);
        activityMainBinding.tvLongitude.setVisibility(View.VISIBLE);
        activityMainBinding.tvLatitude.setText(String.format("%s%s", getString(R.string.tvLatitude), latitude));
        activityMainBinding.tvLongitude.setText(String.format("%s%s", getString(R.string.tvLongitude), longitude));

        // StartLocationScan Button
        activityMainBinding.btnStart.setClickable(false);
        activityMainBinding.btnStart.setEnabled(false);

        // PauseResumeLocationScanButton
        activityMainBinding.btnPauseResumeLocation.setClickable(true);
        activityMainBinding.btnPauseResumeLocation.setEnabled(true);

        // StopLocationScan Button
        activityMainBinding.btnStopLocation.setClickable(true);
        activityMainBinding.btnStopLocation.setEnabled(true);

    }



    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.d("KEYBOARD EXCEPTION", "Keyboard had been already closed");
        }
    }



    @Override
    public void cachePresenter(Presenter presenter) {
        Cache.getInstance().cachePresenterFor(presenter.getUuid(), presenter);
    }



    @Override
    public void restorePresenter(UUID uuid) {
        presenter = (MainPresenter) Cache.getInstance().restorePresenterFor(uuid);
        presenter.setScreen(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        presenter.cancelTask();
    }
}