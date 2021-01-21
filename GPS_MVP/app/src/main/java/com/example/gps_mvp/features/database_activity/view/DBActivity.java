package com.example.gps_mvp.features.database_activity.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.gps_mvp.R;
import com.example.gps_mvp.cache.Cache;
import com.example.gps_mvp.databinding.ActivityDbBinding;
import com.example.gps_mvp.features.Presenter;
import com.example.gps_mvp.features.database_activity.presenter.DBActivityPresenter;
import com.example.gps_mvp.features.database_activity.presenter.DBActivityPresenterImpl;

import java.util.UUID;

public class DBActivity extends AppCompatActivity implements DBActivityView {

    private ActivityDbBinding activityDbBinding;

    private DBActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDbBinding = ActivityDbBinding.inflate(getLayoutInflater());
        View view = activityDbBinding.getRoot();
        setContentView(view);

        presenter = new DBActivityPresenterImpl(this);

        if(savedInstanceState == null) {
            showData();
        }
    }



    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("presenter_uuid", presenter.getUuid().toString());
        cachePresenter(presenter);

        // Data String
        outState.putString("data", activityDbBinding.tvData.getText().toString());

        // Data Label String
        outState.putString("label", activityDbBinding.tvLocalDatabaseLabel.getText().toString());

        super.onSaveInstanceState(outState);
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        restorePresenter(UUID.fromString(savedInstanceState.getString("presenter_uuid")));

        // Data String
        activityDbBinding.tvData.setText(savedInstanceState.getString("data"));

        // Data Label String
        activityDbBinding.tvLocalDatabaseLabel.setText(savedInstanceState.getString("label"));

        super.onRestoreInstanceState(savedInstanceState);
    }



    private void showData() {
        String data = getIntent().getStringExtra("data");
        String tripName = getIntent().getStringExtra("tripName");
        presenter.showData(data, tripName);
    }



    @Override
    public void display(String data, String tripName) {
        Toast.makeText(getApplicationContext(), "Database shown", Toast.LENGTH_SHORT).show();

        if(data.equals("No data found")) {
            activityDbBinding.tvLocalDatabaseLabel.setText(R.string.local_database_location_service_stopped);
        } else {
            String tripLabel = "Trip " + "\"" + tripName + "\":";
            activityDbBinding.tvLocalDatabaseLabel.setText(tripLabel);
        }
        activityDbBinding.tvData.setText(data);
    }



    @Override
    public void cachePresenter(Presenter presenter) {
        Cache.getInstance().cachePresenterFor(presenter.getUuid(), presenter);
    }



    @Override
    public void restorePresenter(UUID uuid) {
        presenter = (DBActivityPresenter) Cache.getInstance().restorePresenterFor(uuid);
        presenter.setScreen(this);
    }
}