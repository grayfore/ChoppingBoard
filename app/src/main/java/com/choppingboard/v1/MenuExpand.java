package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;


public class MenuExpand extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_expand);

        Intent intent = getIntent();
        String thing = intent.getStringExtra("hello");

        TextView testing = (TextView)findViewById(R.id.bob);
        testing.setText(thing);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_expand, menu);
        return true;
    }


}
