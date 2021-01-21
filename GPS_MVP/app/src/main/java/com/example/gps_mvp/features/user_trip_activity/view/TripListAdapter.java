package com.example.gps_mvp.features.user_trip_activity.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gps_mvp.R;
import com.example.models.Trip;

import java.util.List;

public class TripListAdapter extends BaseAdapter {

    private final Context context;
    private LayoutInflater inflater;
    private final List<Trip> tripList;

    public TripListAdapter(Context c, List<Trip> tripList) {
        context = c;
        this.tripList = tripList;
    }



    @Override
    public int getCount() {
        return tripList.size();
    }



    @Override
    public Object getItem(int position) {
        return null;
    }



    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.
                    LAYOUT_INFLATER_SERVICE);
        }

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView tripName = convertView.findViewById(R.id.listItemTripName);
        TextView tripStartFinish = convertView.findViewById(R.id.tripStartFinish);

        String tripNameString = position+1 + ". " + tripList.get(position).getName();
        String tripStartFinishString = "Start: " + tripList.get(position).getStartDate() +
                "\nFinish: " + tripList.get(position).getFinishDate();

        tripName.setText(tripNameString);
        tripStartFinish.setText(tripStartFinishString);

        return convertView;
    }

}
