package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Gray on 7/6/2015.
 */
public class CusCreateScreen extends Activity {

    // Private copy of local database orders and customers are saved on
    private DatabaseHandler db;

    // Private copy of the list of customers from the local database
    private ArrayList<JSONObject> customers;

    // A single customer that is either analyzed or edited
    private JSONObject customer;

    // The text field for customer phone number
    private EditText phoneText;

    // The text field for customer address
    private EditText addressText;

    // The text field for customer name
    private EditText nameText;

    // Statement whether or not customer info has been retrieved
    private boolean customerFound;

    // Listener for changes in the address text field
    public ArrayAdapter<String> adapter;

    // The text field for customer address
    public AutoCompleteTextView textView;

    /**
     * Setup process for this class when it is created as an activity externally
     *
     * @param savedInstanceState Passed in data by an external class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_create_screen);
        customerFound = false;

        adapter = new ArrayAdapter<String>(this, R.layout.item_list);
        textView = (AutoCompleteTextView) findViewById(R.id.addressEdit);
        adapter.setNotifyOnChange(true);
        textView.setAdapter(adapter);
        textView.addTextChangedListener(new TextWatcher() {

            /**
             * Executes an asynchronous task to search and display place names every 3 characters
             * @param s The set of characters that have already been input
             * @param start
             * @param before
             * @param count The number of characters already input into the text field
             */
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count % 3 == 1) {
                    adapter.clear();
                    GetPlaces task = new GetPlaces();
                    task.execute(textView.getText().toString());
                }
            }

            /**
             * Allows for additional processes before text in the field is changed
             * @param s The set of characters already input into the text field
             * @param start
             * @param count The number of characters already input intp the text field
             * @param after
             */
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            /**
             * Allows for additional processes after text in the field is changed
             * @param s Dynamic input from the text field that can be changed externally
             */
            public void afterTextChanged(Editable s) {
            }

            class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {

                /**
                 * Creates a list of place names from Google Places to be shown below user input
                 * @param args Current input into the text field
                 * @return predictionsArr The list of place names to be displayed
                 */
                @Override
                protected ArrayList<String> doInBackground(String... args) {

                    Log.d("gottaGo", "doInBackground");

                    ArrayList<String> predictionsArr = new ArrayList<String>();

                    try {

                        Log.v("Searching for", args[0].toString());

                        URL googlePlaces = new URL(
                                // URLEncoder.encode(url,"UTF-8");
                                "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" +
                                        URLEncoder.encode(args[0].toString(), "UTF-8") +
                                        "&types=geocode&language=en&key=AIzaSyAcZWFP_5F2TaktKH9xxKBEuKphlcTt2mg&location=38.0299,78.4790");
                        URLConnection tc = googlePlaces.openConnection();
                        BufferedReader in = new BufferedReader(new InputStreamReader(
                                tc.getInputStream()));

                        String line;
                        StringBuffer sb = new StringBuffer();
                        while ((line = in.readLine()) != null) {
                            sb.append(line);
                        }
                        Log.v("Prediction", sb.toString());
                        JSONObject predictions = new JSONObject(sb.toString());
                        JSONArray ja = new JSONArray(predictions.getString("predictions"));

                        for (int i = 0; i < ja.length(); i++) {
                            JSONObject jo = (JSONObject) ja.get(i);
                            predictionsArr.add(jo.getString("description"));
                        }
                    } catch (IOException e) {

                        Log.e("ChoppingBoard", "GetPlaces : doInBackground", e);

                    } catch (JSONException e) {

                        Log.e("ChoppingBoard", "GetPlaces : doInBackground", e);

                    }

                    return predictionsArr;

                }

                /**
                 * Changes displayed place names after more text is input into the field
                 * @param result The list of place names to update the field with
                 */
                @Override
                protected void onPostExecute(ArrayList<String> result) {

                    Log.d("ChoppingBoard", "onPostExecute : " + result.size());
                    adapter = new ArrayAdapter<String>(getBaseContext(), R.layout.item_list);
                    adapter.setNotifyOnChange(true);
                    textView.setAdapter(adapter);

                    for (String string : result) {

                        Log.d("ChoppingBoard", "onPostExecute : result = " + string);
                        adapter.add(string);
                        adapter.notifyDataSetChanged();

                    }

                    Log.d("ChoppingBoard", "onPostExecute : autoCompleteAdapter" + adapter.getCount());

                }
            }
        });

        db = new DatabaseHandler(this);
        String phone = getIntent().getStringExtra("phoneNumber");
        customers = new ArrayList<>();
        customers = db.getAllCustomers();
        customer = getCusInfo(phone, customers);

        phoneText = (EditText) findViewById(R.id.phoneEdit);
        addressText = (EditText) findViewById(R.id.addressEdit);
        nameText = (EditText) findViewById(R.id.nameEdit);
        if(customer != null) {
            try {
                nameText.setText(customer.get("name").toString());
                addressText.setText(customer.get("address").toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        phoneText.setText(phone);
    }

    /**
     * Retrieves data pertaining to one customer
     *
     * @param phoneNumber The phone number of the customer to be retrieved
     * @param allCustomers The list of customers to be searched
     * @return receivedCustomer The customer's information
     */
    private JSONObject getCusInfo(String phoneNumber, ArrayList<JSONObject> allCustomers) {
        JSONObject receivedCustomer = new JSONObject();
        for(JSONObject c : allCustomers) {
            try {
                if (c.get("phone").equals(phoneNumber)) {
                    receivedCustomer.put("address", c.get("address"));
                    receivedCustomer.put("name", c.get("name"));
                    receivedCustomer.put("phone", c.get("phone"));
                    customerFound = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return receivedCustomer;
    }

    /**
     * Opens the restaurant menu activity if the menu button is pressed
     *
     * @param view The menu button
     */
    public void MenuButton(View view) {
        JSONObject jCustomer = new JSONObject();
        try {
            jCustomer.put("name", nameText.getText());
            jCustomer.put("phone", phoneText.getText());
            jCustomer.put("address", addressText.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.v("Old String", customer.toString());
        Log.v("New String", jCustomer.toString());
        if(checkCustomerEdited(jCustomer)) {
            Log.v("Customer Edited", "true");
            updateCustomer(jCustomer);
        }
        try {
            if(!customerFound || !jCustomer.get("phone").equals(customer.get("phone"))) {
                addNewCustomer(jCustomer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent i = new Intent(CusCreateScreen.this, ResMenuActivity.class);
        CusCreateScreen.this.startActivity(i);
    }

    /**
     * Checks to see if loaded customer's data has been changed externally
     *
     * @param inCustomer The customer being analyzed
     * @return edited Statement whether loaded customer's data has been changed externally
     */
    private boolean checkCustomerEdited(JSONObject inCustomer) {
        boolean edited = false;
        Log.v("Method", "checkCustomerEdited");
        try {
            Log.v("Trying", "checkCustomerEdited");
            Log.v("Checking", "[" + customer.get("phone").toString() + "]" +
                    " against [" +inCustomer.get("phone").toString() + "]");
            if(customer.get("phone").toString().equals(inCustomer.get("phone").toString())) {
                Log.v("Customers Equal", "true");
                if(!customer.get("name").toString().equals(inCustomer.get("name").toString())) {
                    Log.v("Customer Edited", "true");
                    edited = true;
                }
                if(!customer.get("address").toString().equals(inCustomer.get("address").toString())) {
                    Log.v("Customer Edited", "true");
                    edited = true;
                }
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }
        return edited;
    }

    /**
     * Adds a new customer to the local database
     *
     * @param inCustomer
     */
    private void addNewCustomer(JSONObject inCustomer) {
        db.addCustomer(inCustomer.toString());
    }

    /**
     * Updates an existing customer in the database
     *
     * @param inCustomer
     */
    private void updateCustomer(JSONObject inCustomer) {
        Log.v("Old String", customer.toString());
        Log.v("New String", inCustomer.toString());
        db.editCustomer(customer.toString(), inCustomer.toString());
    }


}
