package com.example.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserTrip implements Serializable {

    private Long id;

    private String userName;

    List<Trip> trips = new ArrayList<Trip>();

    public UserTrip() {
    }

    public UserTrip(String name) {
        this.userName = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Trip> getTrips() {
        return trips;
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "UserTrip{"
                + "id:" + id
                + ", name:" + userName
                + ", trips:" + getTrips()
                + "}";
    }
}
