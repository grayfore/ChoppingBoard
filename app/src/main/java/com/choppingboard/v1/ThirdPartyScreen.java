package com.choppingboard.v1;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ThirdPartyScreen extends Activity {

    // The list of third=party providers to be displayed and chosen form
    private String[] arraySpinner;

    /**
     * Setup process for this class when it is created as an activity externally
     *
     * @param savedInstanceState Passed in data by an external class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_party_screen);

        final EditText edt = (EditText) findViewById(R.id.priceEdit);

        edt.setText("$");
        Selection.setSelection(edt.getText(), edt.getText().length());


        edt.addTextChangedListener(new TextWatcher() {

            /**
             * Allows for additional processes while text in the field is being changed
             * @param s The set of characters that have already been input
             * @param start
             * @param before
             * @param count The number of characters already input into the text field
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            /**
             * Allows for additional processes before text in the field is changed
             * @param s The set of characters already input into the text field
             * @param start
             * @param count The number of characters already input into the text field
             * @param after
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            /**
             * Makes sure that the input symbol remains whenever tech in the field is changed
             * @param s Dynamic input from the text field that can be changed externally
             */
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

    /**
     * Opens a request viewer once the view request button is pressed
     *
     * @param view The request button
     */
    public void tpRequestButton(View view) {
        // TODO Auto-generated method stub
    }


}
