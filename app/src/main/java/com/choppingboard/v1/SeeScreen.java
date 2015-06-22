package com.choppingboard.v1;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.wdullaer.swipeactionadapter.SwipeActionAdapter;
import com.wdullaer.swipeactionadapter.SwipeDirections;

import java.util.ArrayList;


public class SeeScreen extends ListActivity implements SwipeActionAdapter.SwipeActionListener {

    SwipeActionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_screen);

        ArrayList<String> orders = new ArrayList<>();
        CustomList adapter = new CustomList(SeeScreen.this, orders);
        getListView().setAdapter(adapter);

        mAdapter = new SwipeActionAdapter(adapter);
        mAdapter.setSwipeActionListener(this)
                .setDimBackgrounds(true)
                .setListView(getListView());
        setListAdapter(mAdapter);


        mAdapter
                .addBackground(SwipeDirections.DIRECTION_FAR_LEFT,R.layout.leftswipe)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_LEFT,R.layout.leftswipe)
                .addBackground(SwipeDirections.DIRECTION_FAR_RIGHT,R.layout.rightswipe)
                .addBackground(SwipeDirections.DIRECTION_NORMAL_RIGHT, R.layout.rightswipe);


        adapter.add("thing");
        adapter.notifyDataSetChanged();
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
}
