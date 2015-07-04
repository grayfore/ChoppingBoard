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
    private ArrayList<JSONObject> orders;
    private ArrayList<String> ordernums;
    DatabaseHandler db;

    public CustomList(Activity context, ArrayList<JSONObject> orders, ArrayList<String> ordernums) {
        super(context, R.layout.customlist, orders);
        this.context = context;
        this.orders = orders;
        this.ordernums = ordernums;
        db = new DatabaseHandler(this.context);

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
            String price = orderlist.getString("custName");
            TextView num = (TextView)convertView.findViewById(R.id.number);
//            int thinger = db.getNumber(orderlist.toString());
//            Log.v("bobobo", "" + thinger);
            num.setText(ordernums.get(position));
            txtview.setText(price);
            return convertView;

        }
        catch(Exception e)
        {
        }
        return null;
    }

    //Used to update the list view when new info is added
    public void updateList(ArrayList<JSONObject> a, ArrayList<String> b) {
        orders.clear();
        ordernums.clear();
        orders.addAll(a);
        ordernums.addAll(b);
        this.notifyDataSetChanged();
    }

}
