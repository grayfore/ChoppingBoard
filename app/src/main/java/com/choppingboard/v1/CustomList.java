package com.choppingboard.v1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Jeff on 6/22/15.
 */
public class CustomList extends ArrayAdapter<String>{

    private final Activity context;
    private final ArrayList<String> orders;

    public CustomList(Activity context, ArrayList<String> orders) {
        super(context, R.layout.customlist, orders);
        this.context = context;
        this.orders = orders;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.customlist, null, true);
        TextView price = (TextView)rowView.findViewById(R.id.price);
        price.setText(orders.get(position));

        return rowView;

    }
}
