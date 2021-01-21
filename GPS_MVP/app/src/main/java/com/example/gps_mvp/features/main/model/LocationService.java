package com.example.gps_mvp.features.main.model;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.gps_mvp.R;
import com.example.gps_mvp.common.Constants;
import com.example.gps_mvp.features.database_activity.view.DBActivity;
import com.example.gps_mvp.sqlite.SQLiteDBHelper;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LocationService extends Service {


    double latitude, longitude;
    String date;
    SQLiteDBHelper dbHelper = new SQLiteDBHelper(this);



    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
            if (locationResult != null && locationResult.getLastLocation() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat(getString(R.string.date_format));
                longitude = locationResult.getLastLocation().getLongitude();
                latitude = locationResult.getLastLocation().getLatitude();
                date = sdf.format(new Date(locationResult.getLastLocation().getTime()));

                ContentValues contentValues = new ContentValues();
                contentValues.put(SQLiteDBHelper.KEY_LATITUDE, latitude);
                contentValues.put(SQLiteDBHelper.KEY_LONGITUDE, longitude);
                contentValues.put(SQLiteDBHelper.KEY_DATE, date);
                sqLiteDatabase.insert(SQLiteDBHelper.LOCATION_TABLE, null, contentValues);

                Log.d("LOCATION_UPDATE", latitude + ", " + longitude);
                sendMessage();
            }
        }
    };



    // Send Coordinates to main activity
    private void sendMessage() {
        Intent intent = new Intent("location");
        String strLongitude = String.valueOf(longitude);
        String strLatitude = String.valueOf(latitude);
        intent.putExtra("latitude", strLatitude);
        intent.putExtra("longitude", strLongitude);
        LocalBroadcastManager.getInstance(LocationService.this).sendBroadcast(intent);
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("");
    }

    private void startLocationService() {
        String channelId = "location_notification_channel";

        //  notification
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setContentTitle("Location Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null &&
                    notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        channelId,
                        "Location Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationChannel.setDescription("This channel is used by location service");
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(4000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        startForeground(Constants.LOCATION_SERVICE_ID, builder.build());
    }



    private void stopLocationService() {
        //  close database and disconnect
        dbHelper.close();
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback);
        stopForeground(true);
        stopSelf();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            String action = intent.getAction();

            if (action != null) {
                if (action.equals(Constants.ACTION_START_LOCATION_SERVICE)) {


                    startLocationService();
                    Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT).show();
                    getApplicationContext().deleteDatabase(SQLiteDBHelper.DATABASE_NAME);


                } else if (action.equals(Constants.SHOW_DATABASE)) {


                    showLocalDatabase(intent.getStringExtra("tripName"));


                } else if(action.equals(Constants.ACTION_PAUSE_LOCATION_SERVICE)) {


                    stopLocationService();
                    Toast.makeText(this, "Location service paused", Toast.LENGTH_SHORT).show();


                } else if(action.equals(Constants.ACTION_RESUME_LOCATION_SERVICE)) {


                    startLocationService();
                    Toast.makeText(this, "Location service resumed", Toast.LENGTH_SHORT).show();


                } else if (action.equals(Constants.ACTION_STOP_LOCATION_SERVICE)) {


                    stopLocationService();
                    Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();


                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void showLocalDatabase(String tripName) {

        dbHelper = new SQLiteDBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        if (sqLiteDatabase.isOpen()) {

            Intent databaseIntent = new Intent(LocationService.this,
                    DBActivity.class);

            Cursor cursor = sqLiteDatabase.query(SQLiteDBHelper.LOCATION_TABLE, null,
                    null, null, null, null, null);

            StringBuilder data = new StringBuilder();
            if (cursor.moveToFirst()) {
                databaseIntent.putExtra("tripName", tripName);

                int id = cursor.getColumnIndex(SQLiteDBHelper.KEY_ID);
                int latitudeId = cursor.getColumnIndex(SQLiteDBHelper.KEY_LATITUDE);
                int longitudeId = cursor.getColumnIndex(SQLiteDBHelper.KEY_LONGITUDE);
                int dateId = cursor.getColumnIndex(SQLiteDBHelper.KEY_DATE);
                do {
                    data.append(cursor.getInt(id)).append(". Latitude: ")
                            .append(cursor.getString(latitudeId)).append("; Longitude: ")
                            .append(cursor.getString(longitudeId)).append("; Date: ")
                            .append(cursor.getString(dateId)).append(";\n");
                } while (cursor.moveToNext());
            } else {
                data = new StringBuilder("No data found");
            }

            cursor.close();


            databaseIntent.putExtra("data", data.toString());
            databaseIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(databaseIntent);
            dbHelper.close();
        }

    }
}