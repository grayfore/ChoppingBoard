package com.choppingboard.v1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class BuildScreen extends Activity {

    Context applicationContext;

    /**
     * Setup process for this class when it is created as an activity externally
     *
     * @param savedInstanceState Passed in data by an external class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_screen);
        applicationContext = getApplicationContext();
        EditText phoneText = (EditText) findViewById(R.id.buildPhoneEdit);
        phoneText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH && v.getText().length() != 0) {
                    Intent i = new Intent(BuildScreen.this, CusCreateScreen.class);
                    i.putExtra("phoneNumber", v.getText().toString());
                    BuildScreen.this.startActivity(i);
                    handled = true;
                }
                else if(actionId == EditorInfo.IME_ACTION_SEARCH && v.getText().length() == 0) {
                    Toast.makeText(applicationContext, "Please enter valid phone number",
                            Toast.LENGTH_LONG).show();
                    handled = true;
                }
                return handled;
            }
        });
    }

    public void ThirdPartyCheck(View view) {
        Intent i = new Intent(BuildScreen.this, ThirdPartyScreen.class);
        BuildScreen.this.startActivity(i);
    }


}
