package com.choppingboard.v1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class CusCreateScreen extends Activity {

    private DatabaseHandler db;
    private ArrayList<JSONObject> customers;
    private JSONObject customer;
    private EditText phoneText;
    private EditText addressText;
    private EditText nameText;
    private boolean customerFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_create_screen);
        customerFound = false;

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

    private JSONObject getCusInfo(String phoneNumber, ArrayList<JSONObject> allCustomers) {
        JSONObject recievedCustomer = new JSONObject();
        for(JSONObject c : allCustomers) {
            try {
                if (c.get("phone").equals(phoneNumber)) {
                    recievedCustomer.put("address", c.get("address"));
                    recievedCustomer.put("name", c.get("name"));
                    recievedCustomer.put("phone", c.get("phone"));
                    customerFound = true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recievedCustomer;
    }

    public void MenuButton(View view) {
        checkCustomerEdited(customer, customers);
        if(!customerFound) {
            JSONObject jCustomer = new JSONObject();
            try {
                jCustomer.put("name", nameText.getText());
                jCustomer.put("phone", phoneText.getText());
                jCustomer.put("address", addressText.getText());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            db.addCustomer(jCustomer.toString());
        }
        Intent i = new Intent(CusCreateScreen.this, ResMenuActivity.class);
        CusCreateScreen.this.startActivity(i);
    }

    private void checkCustomerEdited(JSONObject inCustomer, ArrayList<JSONObject> inCustomers) {
        for(JSONObject c : inCustomers) {
            try {
                if(c.get("phone").equals(inCustomer.get("phone"))) {
                    boolean edited = false;
                    if(!c.get("name").equals(nameText.getText())) {
                        edited = true;
                        inCustomer.remove("name");
                        inCustomer.put("name", nameText.getText());
                    }
                    if(!c.get("address").equals(addressText.getText())) {
                        edited = true;
                        inCustomer.remove("address");
                        inCustomer.put("address", addressText.getText());
                    }
                    if(edited)
                        db.editCustomer(c.toString(), inCustomer.toString());
                }
            } catch(JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
