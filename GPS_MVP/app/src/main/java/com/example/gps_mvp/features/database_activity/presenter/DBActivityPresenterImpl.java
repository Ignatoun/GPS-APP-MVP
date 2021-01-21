package com.example.gps_mvp.features.database_activity.presenter;

import com.example.gps_mvp.features.Screen;
import com.example.gps_mvp.features.database_activity.view.DBActivityView;

import java.util.UUID;

public class DBActivityPresenterImpl implements DBActivityPresenter {

    private final UUID uuid;
    private DBActivityView view;

    public DBActivityPresenterImpl(DBActivityView view) {
        this.uuid = UUID.randomUUID();
        this.view = view;
    }

    @Override
    public void setScreen(Screen screen) {
        this.view = (DBActivityView) screen;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void showData(String data, String tripName) {
        view.display(data, tripName);
    }
}
