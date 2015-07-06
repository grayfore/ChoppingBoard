package com.choppingboard.v1;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


public class BuildScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_screen);
        EditText phoneText = (EditText) findViewById(R.id.buildPhoneEdit);
        phoneText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    Intent i = new Intent(BuildScreen.this, CusCreateScreen.class);
//                    i.putExtra("phoneNumber", v.getText().toString());
//                    BuildScreen.this.startActivity(i);
                    handled = true;
                }
                return handled;
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(this,DashBoard.class);
//        BuildScreen.this.startActivity(intent);
//        BuildScreen.this.finish();
//    }

}
