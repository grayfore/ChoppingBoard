package com.choppingboard.v1;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SeeScreen extends ListActivity implements SwipeActionAdapter.SwipeActionListener {

    SwipeActionAdapter mAdapter;
    CustomList adapter;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    PopupWindow pwindow;
    Context c;
    ArrayList<JSONObject> orders;

    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_screen);

        DatabaseHandler db = new DatabaseHandler(this);

        // Intent Message sent from Broadcast Receiver
        String str = getIntent().getStringExtra("msg");


        // Check if Google Play Service is installed in Device
        // Play services is needed to handle GCM stuffs
        if (!checkPlayServices()) {
            Toast.makeText(
                    getApplicationContext(),
                    "This device doesn't support Play services, App will not work normally",
                    Toast.LENGTH_LONG).show();
        }

        Log.v("lambogallardo",""+db.getOrderCount());
        orders = new ArrayList<>();
        orders = db.getAllOrders();
        adapter = new CustomList(SeeScreen.this, orders);
        getListView().setAdapter(adapter);
        mAdapter = new SwipeActionAdapter(adapter);
        mAdapter.setSwipeActionListener(this)
//                .setDimBackgrounds(true)
                .setListView(getListView());
        setListAdapter(mAdapter);


        mAdapter
                .addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.leftswipe)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.leftswipe)
                .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT, R.layout.rightswipe)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.rightswipe);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(SeeScreen.this, "You clicked on " + orders.get(position), Toast.LENGTH_SHORT).show();
                pwindow = new com.choppingboard.v1.PopupWindow(SeeScreen.this,orders.get(position));
                pwindow.show(findViewById(R.id.seescreen), 0, 0);
            }
        });

        adapter.notifyDataSetChanged();



        if (str != null) {
            // Set the message
            try {
                JSONObject order = new JSONObject(str);
                adapter.add(order);

            }catch (JSONException e){
                e.printStackTrace();
            }

        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("order"));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("msg");
            Log.d("receiver", "Got message: " + message);
            if (message != null) {
                // Set the message
                try {
                    JSONObject order = new JSONObject(message);
                    adapter.add(order);
                    adapter.notifyDataSetChanged();

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_see_screen, menu);
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

    @Override
    public boolean hasActions(int i) {
        return true;
    }

    @Override
    public boolean shouldDismiss(int i, int i1) {
        return false;
    }

    @Override
    public void onSwipe(int[] ints, int[] ints1) {
        for(int i=0;i<ints.length;i++) {
            int direction = ints1[i];
            int position = ints[i];
            String dir = "";

            switch (direction) {
                case SwipeDirections.DIRECTION_FAR_LEFT:
                    dir = "Far left";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    dir = "Left";
                    break;
                case SwipeDirections.DIRECTION_FAR_RIGHT:
                    dir = "Far right";
                    break;
                case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                    dir = "Right";
                    break;
            }
            Toast.makeText(
                    SeeScreen.this,dir + " swipe Action triggered on " + mAdapter.getItem(position),
                    Toast.LENGTH_SHORT
            ).show();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    // Check if Google Playservices is installed in Device or not
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        // When Play services not found in device
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                // Show Error dialog to install Play services
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "This device supports Play services, App will work normally",
                    Toast.LENGTH_LONG).show();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,DashBoard.class);
        SeeScreen.this.startActivity(intent);
        SeeScreen.this.finish();
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
}
