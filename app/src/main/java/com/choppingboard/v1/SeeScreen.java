package com.choppingboard.v1;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
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
    ArrayList<String> ordernums;
    ArrayList<String> listOfStatus;
    DatabaseHandler db;

    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_screen);

        //Access the local Database
        db = new DatabaseHandler(this);

        // Check if Google Play Service is installed in Device
        // Play services is needed to handle GCM stuffs
        if (!checkPlayServices()) {
            Toast.makeText(
                    getApplicationContext(),
                    "This device doesn't support Play services, App will not work normally",
                    Toast.LENGTH_LONG).show();
        }

        //Create the list view using Arraylist pulled from Database
        Log.v("lambogallardo",""+db.getOrderCount());
        listOfStatus = new ArrayList<>();
        ordernums = new ArrayList<>();
        orders = new ArrayList<>();
        ordernums = db.getAllNums();
        orders = db.getAllOrders();
        listOfStatus = db.getAllStatus();
        adapter = new CustomList(SeeScreen.this, orders,ordernums, listOfStatus);
        getListView().setAdapter(adapter);

        // madapter is used for the swiping in listview
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

        //Sets up the on click listener to open a popup window
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Toast.makeText(SeeScreen.this, "You clicked on " + orders.get(position), Toast.LENGTH_SHORT).show();
                pwindow = new PopupWindow(SeeScreen.this, orders.get(position),findViewById(R.id.seescreen), ordernums.get(position), adapter);
                pwindow.show(findViewById(R.id.seescreen), 0, 0);
            }
        });

        //Local broadcast Manager recieves intents if the see screen is already open
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("order"));
    }

    // Recieves local broadcasts from GCMNotificationIntentService
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("msg");
            Log.d("receiver", "Got message: " + message);
            if (message != null) {
                db.addOrder(message);
                ArrayList<JSONObject> one = db.getAllOrders();
                ArrayList<String> two = db.getAllNums();
                ArrayList<String> three = db.getAllStatus();
                adapter.updateList(one,two, three);
            }
        }
    };

    @Override
    public void onSwipe(int[] ints, int[] ints1) {
        for(int i=0;i<ints.length;i++) {
            int direction = ints1[i];
            // CAREFUL ABOUT THE FINAL... DOUBLE CHECK THIS WITH MULTIPLE ENTRIESt
            final int position = ints[i];
            String dir = "";

            /**
             * Dialog box that double checks if the user wants to cancel an order
             * It shows up when there is a left swipe.
             */
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            try {
                                Intent intent = new Intent(SeeScreen.this, UpdateStatus.class);
                                intent.putExtra("orderId", orders.get(position).getString("id"));
                                intent.putExtra("status", "" + 0);
                                intent.putExtra("ordKeyId", ordernums.get(position));
                                SeeScreen.this.startService(intent);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    refreshPage();
                                }
                            }, 200);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(SeeScreen.this);
            /**
             * Switch that controls what action occur when a certain direction is swiped.
             */
            switch (direction) {
                case SwipeDirections.DIRECTION_FAR_LEFT:
                    dir = "Far left";
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    break;
                case SwipeDirections.DIRECTION_NORMAL_LEFT:
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                    dir = "Left";
                    break;
                case SwipeDirections.DIRECTION_FAR_RIGHT:
                    dir = "Far right";
                    try {
                        Intent intent = new Intent(SeeScreen.this, UpdateStatus.class);
                        intent.putExtra("orderId", orders.get(position).getString("id"));
                        intent.putExtra("status", "" + 4);
                        intent.putExtra("ordKeyId", ordernums.get(position));
                        SeeScreen.this.startService(intent);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
                case SwipeDirections.DIRECTION_NORMAL_RIGHT:
                    dir = "Right";
                    try {
                        Intent intent = new Intent(SeeScreen.this, UpdateStatus.class);
                        intent.putExtra("orderId", orders.get(position).getString("id"));
                        intent.putExtra("status", "" + 4);
                        intent.putExtra("ordKeyId", ordernums.get(position));
                        SeeScreen.this.startService(intent);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    break;
            }
            Toast.makeText(
                    SeeScreen.this,dir + " swipe Action triggered on " + mAdapter.getItem(position),
                    Toast.LENGTH_SHORT
            ).show();
            mAdapter.notifyDataSetChanged();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshPage();
                }
            }, 200);
        }
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

    //controls if an item is swipable or not
    @Override
    public boolean hasActions(int position) {
        Boolean answer;

        if(listOfStatus.get(position).equals("0")){
            answer = false;
        }else {
            answer = true;
        }
        return answer;
    }

    // return true if you want the item to be dismissed, false if you want to keep it
    @Override
    public boolean shouldDismiss(int position, int direction) {
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(pwindow != null){
            pwindow.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        // Unregister since the activity is about to be closed.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    //on start and on stop are used as booleans for intent if statement in GCMNotificationIntent Service
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

    //........ Methods that correlate to buttons on the deny popupwindow ........//
    public void reasonuno(View view){
        Log.v("Buttonone", "Hello There");
        pwindow.getPd().dismiss();
    }

    public void reasondos(View view){
        Log.v("Buttontwo", "Hello There");
        pwindow.getPd().dismiss();
    }

    public void reasontres(View view){
        Log.v("Buttonthree", "Hello There");
        pwindow.getPd().dismiss();
    }

    public void reasoncuatro(View view) {
        Log.v("Buttonfour", "Hello There");
        pwindow.getPd().dismiss();
    }

    //updates the listview when it is called
    public void refreshPage(){
        ArrayList<JSONObject> one = db.getAllOrders();
        ArrayList<String> two = db.getAllNums();
        ArrayList<String> three = db.getAllStatus();
        adapter.updateList(one, two, three);
        adapter.notifyDataSetChanged();
    }
}
