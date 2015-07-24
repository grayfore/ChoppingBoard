package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;


public class Invoice extends Activity {

    DatePicker first, second;
    Button submit;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        first = (DatePicker) findViewById(R.id.first);
        second = (DatePicker) findViewById(R.id.second);
        submit = (Button) findViewById(R.id.enter);
        db = new DatabaseHandler(this);


    }

    public void findOrders(View view) {

        String firstMonth = ""+first.getMonth();
        String firstDay = ""+first.getDayOfMonth();
        String firstYear = ""+first.getYear();

        String secondMonth = ""+second.getMonth();
        String secondDay = ""+second.getDayOfMonth();
        String secondYear = ""+second.getYear();


        if(db.getInvoice(Integer.parseInt(firstYear), Integer.parseInt(firstMonth),
                Integer.parseInt(firstDay), Integer.parseInt(secondYear),Integer.parseInt(secondMonth), Integer.parseInt(secondDay)).isEmpty()){
            Toast.makeText(getApplicationContext(), "No available orders.  Please enter new dates", Toast.LENGTH_LONG).show();
            Log.v("made","WE MADE IT");
        }
        else {
            Intent intent = new Intent(Invoice.this, InvoiceScreen.class);
        intent.putExtra("firstYear", firstYear);
        intent.putExtra("firstMonth", firstMonth);
        intent.putExtra("firstDay", firstDay);
        intent.putExtra("secondYear", secondYear);
        intent.putExtra("secondMonth", secondMonth);
        intent.putExtra("secondDay", secondDay);
            startActivity(intent);
        }



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
