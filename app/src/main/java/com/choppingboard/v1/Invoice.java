package com.choppingboard.v1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;


public class Invoice extends Activity {

    DatePicker first, second;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        first = (DatePicker) findViewById(R.id.first);
        second = (DatePicker) findViewById(R.id.second);
        submit = (Button) findViewById(R.id.enter);


    }

    public void findOrders(View view) {

        int firstMonth = first.getMonth();
        int firstDay = first.getDayOfMonth();
        int firstYear = first.getYear();

        int secondMonth = second.getMonth();
        int secondDay = second.getDayOfMonth();
        int secondYear = second.getYear();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_invoice, menu);
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
