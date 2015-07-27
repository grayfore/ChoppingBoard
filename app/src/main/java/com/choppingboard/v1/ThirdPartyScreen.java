package com.choppingboard.v1;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class ThirdPartyScreen extends Activity {

    private String[] arraySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_screen);

        final EditText edt = (EditText) findViewById(R.id.priceEdit);

        edt.setText("$");
        Selection.setSelection(edt.getText(), edt.getText().length());


        edt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("$")) {
                    edt.setText("$");
                    Selection.setSelection(edt.getText(), edt.getText().length());

                }

            }
        });

        this.arraySpinner = new String[] {
                "OrderUp", "Grubhub", "Foodio"
        };
        Spinner s = (Spinner) findViewById(R.id.tpSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        s.setAdapter(adapter);
    }

    public void tpRequestButton(View view) {
        // TODO Auto-generated method stub
    }
}
