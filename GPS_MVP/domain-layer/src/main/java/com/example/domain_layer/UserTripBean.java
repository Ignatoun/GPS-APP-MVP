package com.example.domain_layer;

import com.example.models.UserTrip;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface UserTripBean {

    UserTrip getUserByName(String name);

    List<UserTrip> getAllUsers();

    String updateUser(UserTrip user);

    String postUser(UserTrip user);

}
