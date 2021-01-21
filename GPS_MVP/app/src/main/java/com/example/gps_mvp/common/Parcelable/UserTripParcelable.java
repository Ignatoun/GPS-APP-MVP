package com.example.gps_mvp.common.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class UserTripParcelable implements Parcelable {

    private Long id;

    private String userName;

    List<TripParcelable> trips = new ArrayList<TripParcelable>();

    public UserTripParcelable() {
    }

    public UserTripParcelable(String name) {
        this.userName = name;
    }

    protected UserTripParcelable(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        userName = in.readString();
        trips = in.createTypedArrayList(TripParcelable.CREATOR);
    }

    public static final Creator<UserTripParcelable> CREATOR = new Creator<UserTripParcelable>() {
        @Override
        public UserTripParcelable createFromParcel(Parcel in) {
            return new UserTripParcelable(in);
        }

        @Override
        public UserTripParcelable[] newArray(int size) {
            return new UserTripParcelable[size];
        }
    };

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

    public List<TripParcelable> getTrips() {
        return trips;
    }

    public void setTrips(List<TripParcelable> trips) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(userName);
        dest.writeTypedList(trips);
    }
}
