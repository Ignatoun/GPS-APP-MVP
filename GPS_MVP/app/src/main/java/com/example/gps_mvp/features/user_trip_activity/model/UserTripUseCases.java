package com.example.gps_mvp.features.user_trip_activity.model;

import com.example.domain_layer.UserTripBean;
import com.example.domain_layer.UserTripBeanImpl;
import com.example.models.UserTrip;

import java.util.concurrent.BlockingQueue;

public class UserTripUseCases {

    private final UserTripBean userBean;

    public UserTripUseCases() {
        this.userBean = new UserTripBeanImpl();
    }

    public UserTrip getUserByName(String username) {
        return userBean.getUserByName(username);
    }

}
