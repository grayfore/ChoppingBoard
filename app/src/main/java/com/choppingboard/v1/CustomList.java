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
        try
        {
            if(convertView==null)
            {
                LayoutInflater inflater = context.getLayoutInflater();
                convertView = inflater.inflate(R.layout.customlist,parent, false);
            }
            String  price = getItem(position);
            TextView txtview = (TextView) convertView.findViewById(R.id.price);
            txtview.setText(price);
            return convertView;
        }
        catch(Exception e)
        {
        }
        return null;
    }

}
