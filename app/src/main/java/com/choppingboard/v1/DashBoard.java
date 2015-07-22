package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Gray on 6/19/2015.
 */
public class DashBoard extends Activity{

    static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        DatabaseHandler db = new DatabaseHandler(this);

//        db.addOrder("{\"provider\":\"EasyChops\",\"id\":\"EC105\",\"reqtime\":\"05:12 PM\",\"custName\":\"Huiyu Zhang\",\"custNum\":\"4344666077\",\"custAdd\":\"217 B2 Hoxton\"}");
//        db.addOrder("{\"provider\":\"EasyChops\",\"id\":\"EC123\",\"reqtime\":\"04:20 PM\",\"custName\":\"Jeffery Cui\",\"custNum\":\"1234567890\",\"custAdd\":\"123 Fake Street\"}");

    }

    public void seeScreen(View view){
        Intent intent = new Intent(DashBoard.this, SeeScreen.class);
        DashBoard.this.startActivity(intent);
    }
    public void buildScreen(View view){
        Intent intent = new Intent(DashBoard.this, BuildScreen.class);
        DashBoard.this.startActivity(intent);
    }
    public void invoiceScreen(View view){
        Intent intent = new Intent(DashBoard.this, Invoice.class);
        DashBoard.this.startActivity(intent);
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
}
