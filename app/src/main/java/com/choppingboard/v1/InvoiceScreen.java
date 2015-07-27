package com.choppingboard.v1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import org.json.JSONObject;

import java.util.ArrayList;


public class InvoiceScreen extends ListActivity {

    DatabaseHandler db;
    InvoiceCustomList adapter;
    ArrayList<JSONObject> orders;
    ArrayList<String> ordernums;
    ArrayList<String> listOfStatus;
    InvoicePopupWindow pwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_screen);

        db = new DatabaseHandler(this);

        Intent intent = getIntent();

        int firstMonth = Integer.parseInt(intent.getStringExtra("firstMonth"));
        int firstDay = Integer.parseInt(intent.getStringExtra("firstDay"));
        int firstYear = Integer.parseInt(intent.getStringExtra("firstYear"));

        int secondMonth = Integer.parseInt(intent.getStringExtra("secondMonth"));
        int secondDay = Integer.parseInt(intent.getStringExtra("secondDay"));
        int secondYear = Integer.parseInt(intent.getStringExtra("secondYear"));

        orders = db.getInvoice(firstYear, firstMonth, firstDay, secondYear, secondMonth, secondDay);

//        orders = db.getAllOrders();

        adapter = new InvoiceCustomList(InvoiceScreen.this, orders);
        getListView().setAdapter(adapter);


        //Sets up the on click listener to open a popup window
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(SeeScreen.this, "You clicked on " + orders.get(position), Toast.LENGTH_SHORT).show();
                pwindow = new InvoicePopupWindow(InvoiceScreen.this, orders.get(position),findViewById(R.id.invoicescreen), adapter);
                pwindow.show(findViewById(R.id.invoicescreen), 0, 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invoice_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
