package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MenuExpand extends Activity {

    private String thing;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_expand);
        db = new DatabaseHandler(this);

        Intent intent = getIntent();
        thing = intent.getStringExtra("hello");

        Log.v("lambo", thing);
        TableLayout table = (TableLayout)findViewById(R.id.optionstable);

        ArrayList<JSONObject> list = db.getMatMenu(thing);

        Log.v("lambo", "outside");
        Log.v("lambo", ""+db.getMatCount());
        for(JSONObject o: list){
            try{
                Log.v("lambo", "IN here");
                TableRow row = (TableRow) LayoutInflater.from(this).inflate(R.layout.attrib_row, null);
                ((TextView) row.findViewById(R.id.attrib_name)).setText(o.getString("name"));
                ((TextView) row.findViewById(R.id.attrib_value)).setText("$" + o.getString("price"));
                table.addView(row);
            }catch (JSONException e){
                e.printStackTrace();
            }
        }

    }

    public void addOrder(View view){
        ResMenuActivity.finalOrder.add(thing);
        Toast.makeText(this, "Added Order",Toast.LENGTH_SHORT).show();
        NavUtils.navigateUpFromSameTask(MenuExpand.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_expand, menu);
        return true;
    }


}
