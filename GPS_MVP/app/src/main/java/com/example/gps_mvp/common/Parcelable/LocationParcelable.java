package com.example.gps_mvp.common.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationParcelable implements Parcelable {

    private Long id;

    private String latitude;
    private String longitude;

    private TripParcelable trip;

    public LocationParcelable() {
    }

    public LocationParcelable(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected LocationParcelable(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        latitude = in.readString();
        longitude = in.readString();
        trip = in.readParcelable(TripParcelable.class.getClassLoader());
    }

    public static final Creator<LocationParcelable> CREATOR = new Creator<LocationParcelable>() {
        @Override
        public LocationParcelable createFromParcel(Parcel in) {
            return new LocationParcelable(in);
        }

        @Override
        public LocationParcelable[] newArray(int size) {
            return new LocationParcelable[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + this.id +
                ", latitude=" + this.latitude +
                ", longitude=" + this.longitude +
                "}";
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
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeParcelable(trip, flags);
    }
}
