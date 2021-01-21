package com.example.domain_layer;

import com.example.data_layer.dao.UserDao;
//import com.example.data_layer.dao.UserDaoImpl;
import com.example.data_layer.dao.UserDaoImpl;
import com.example.data_layer.dao.UserDaoImplRealDevice;
import com.example.models.UserTrip;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class UserTripBeanImpl implements UserTripBean {

    private UserDao userDao;

    // For Emulator Device
    public UserTripBeanImpl() {
        userDao = new UserDaoImpl();
    }

    // For Real Device
//    public UserTripBeanImpl() {
//        userDao = new UserDaoImplRealDevice();
//    }

    @Override
    public UserTrip getUserByName(String name) {
        return userDao.getUserByName(name);
    }

    @Override
    public List<UserTrip> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    public String updateUser(UserTrip user) {
        return userDao.updateUser(user);
    }

    @Override
    public String postUser(UserTrip user) {
        return userDao.postUser(user);
    }
}
