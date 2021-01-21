package com.example.data_layer.dao;

import com.example.models.UserTrip;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public interface UserDao {

    List<UserTrip> getAllUsers();

    UserTrip getUserByName(String username);

//    BlockingQueue<UserTrip> getUserByName(String username);

    String postUser(UserTrip user);

    String updateUser(long id, UserTrip user);

    String updateUser(UserTrip user);
}
