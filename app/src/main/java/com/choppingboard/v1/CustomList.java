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

    //.........FIELDS..........//
    private final Activity context;
    private ArrayList<JSONObject> orders;
    private ArrayList<String> ordernums;
    private ArrayList<String> listOfStatus;
    DatabaseHandler db;

    //........ Constructor ........//
    public CustomList(Activity context, ArrayList<JSONObject> orders, ArrayList<String> ordernums, ArrayList<String> listOfStatus) {
        super(context, R.layout.customlist, orders);
        this.context = context;
        this.orders = orders;
        this.ordernums = ordernums;
        this.listOfStatus = listOfStatus;
        db = new DatabaseHandler(this.context);

    }

    //........ Main method that populates the listview, style and info ........//
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
            TextView status = (TextView)convertView.findViewById(R.id.status);
            status.setText(listOfStatus.get(position));
            return convertView;

        }
        catch(Exception e)
        {
        }
        return null;
    }

    //........ Used to update the list view when new info is added ........//
    public void updateList(ArrayList<JSONObject> a, ArrayList<String> b, ArrayList<String> c) {
        orders.clear();
        ordernums.clear();
        listOfStatus.clear();

        orders.addAll(a);
        ordernums.addAll(b);
        listOfStatus.addAll(c);

        this.notifyDataSetChanged();
    }

}
