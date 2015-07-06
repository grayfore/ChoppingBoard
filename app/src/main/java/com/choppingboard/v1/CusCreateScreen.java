package com.choppingboard.v1;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class CusCreateScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String phone = getIntent().getStringExtra("phoneNumber");
        setContentView(R.layout.activity_cus_create_screen);
        EditText phoneText = (EditText) findViewById(R.id.phoneEdit);
        phoneText.setText(phone);
    }
}
