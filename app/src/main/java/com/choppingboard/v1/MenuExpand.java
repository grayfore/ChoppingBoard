package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MenuExpand extends Activity {

    private String thing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_expand);

        Intent intent = getIntent();
        thing = intent.getStringExtra("hello");

        TextView testing = (TextView)findViewById(R.id.bob);
        testing.setText(thing);
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
