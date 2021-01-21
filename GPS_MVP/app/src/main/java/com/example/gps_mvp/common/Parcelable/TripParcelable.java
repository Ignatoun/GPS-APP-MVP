package com.example.gps_mvp.common.Parcelable;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;


public class TripParcelable implements Parcelable {

    private Long id;

    private String name;

    List<LocationParcelable> locations = new ArrayList<LocationParcelable>();

    private UserTripParcelable userTrip;

    private String startDate;

    private String finishDate;

    public TripParcelable(String name) {
        this.name = name;
    }

    public TripParcelable() {
    }

    protected TripParcelable(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        locations = in.createTypedArrayList(LocationParcelable.CREATOR);
        userTrip = in.readParcelable(UserTripParcelable.class.getClassLoader());
        startDate = in.readString();
        finishDate = in.readString();
    }

    public static final Creator<TripParcelable> CREATOR = new Creator<TripParcelable>() {
        @Override
        public TripParcelable createFromParcel(Parcel in) {
            return new TripParcelable(in);
        }

        @Override
        public TripParcelable[] newArray(int size) {
            return new TripParcelable[size];
        }
    };

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<LocationParcelable> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationParcelable> locations) {
        this.locations = locations;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "locations:" + getLocations() +
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
        dest.writeString(name);
        dest.writeTypedList(locations);
        dest.writeParcelable(userTrip, flags);
        dest.writeString(startDate);
        dest.writeString(finishDate);
    }
}
