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
public class InvoiceCustomList extends ArrayAdapter<JSONObject>{

    //.........FIELDS..........//
    private final Activity context;
    private ArrayList<JSONObject> orders;

    DatabaseHandler db;

    //........ Constructor ........//
    public InvoiceCustomList(Activity context, ArrayList<JSONObject> orders) {
        super(context, R.layout.customlist, orders);
        this.context = context;
        this.orders = orders;
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
                convertView = inflater.inflate(R.layout.invoicecustomlist,parent, false);
            }
            JSONObject  orderlist = getItem(position);
            TextView txtview = (TextView) convertView.findViewById(R.id.name);
            String name = orderlist.getString("custName");
            txtview.setText(name);

            TextView pricetxt = (TextView) convertView.findViewById(R.id.invoiceprice);
            String price = orderlist.getString("subtotal");
            pricetxt.setText(price);

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

        orders.addAll(a);

        this.notifyDataSetChanged();
    }

}
