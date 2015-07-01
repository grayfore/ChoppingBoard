package com.choppingboard.v1;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by Jeff on 6/22/15.
 */
public class CustomList extends ArrayAdapter<JSONObject>{

    private final Activity context;
    private final ArrayList<JSONObject> orders;

    public CustomList(Activity context, ArrayList<JSONObject> orders) {
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
            JSONObject  orderlist = getItem(position);
            TextView txtview = (TextView) convertView.findViewById(R.id.price);
            String price = orderlist.getString("subtotal");
            txtview.setText(price);
            return convertView;
        }
        catch(Exception e)
        {
        }
        return null;
    }

}
