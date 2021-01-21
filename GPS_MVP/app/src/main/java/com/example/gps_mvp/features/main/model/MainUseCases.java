package com.example.gps_mvp.features.main.model;

import com.example.domain_layer.UserTripBean;
import com.example.domain_layer.UserTripBeanImpl;
import com.example.models.UserTrip;

import java.util.List;

public class MainUseCases {

    private UserTripBean userBean;

    public MainUseCases() {
        this.userBean = new UserTripBeanImpl();
    }

    public List<UserTrip> getAllUsers() {
        return userBean.getAllUsers();
    }

//    public String updateUser(UserTrip user) {
//        return userBean.updateUser(user);
//    }
    public void updateUser(UserTrip user) {
        userBean.updateUser(user);
    }

//    public String postUser(UserTrip user) {
//        return userBean.postUser(user);
//    }
    public void postUser(UserTrip user) {
        userBean.postUser(user);
    }

}
