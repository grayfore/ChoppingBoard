package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Gray on 6/19/2015.
 */
public class DashBoard extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

    }

    public void seeScreen(View view){
        Intent intent = new Intent(DashBoard.this, SeeScreen.class);
        DashBoard.this.startActivity(intent);
        DashBoard.this.finish();
    }
    public void buildScreen(View view){

    }
    public void invoiceScreen(View view){

    }
}
