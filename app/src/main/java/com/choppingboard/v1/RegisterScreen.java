package com.choppingboard.v1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterScreen extends Activity {

    // Loading screen for longer tasks
    ProgressDialog prgDialog;

    // Data on the running activity
    Context applicationContext;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    // Name of the preference for registration id
    public static final String REG_ID = "regId";

    // The text field for email
    EditText emailET;

    // The text field for restaurant ID
    EditText idET;

    /**
     * Setup process for this class when it is created as an activity externally
     *
     * @param savedInstanceState Passed in data by an external class
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        applicationContext = getApplicationContext();
        emailET = (EditText) findViewById(R.id.email);
        idET = (EditText) findViewById(R.id.restoId);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Please wait...");
        prgDialog.setCancelable(false);

        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        String registrationId = prefs.getString(REG_ID, "");

        if (!TextUtils.isEmpty(registrationId)) {
            Intent i = new Intent(applicationContext, DashBoard.class);
            i.putExtra("regId", registrationId);
            startActivity(i);
            finish();
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(sMessageReceiver,
                new IntentFilter(QuickstartPreferences.SENT_TOKEN_TO_SERVER));
        LocalBroadcastManager.getInstance(this).registerReceiver(fMessageReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_FAILED));
    }

    /**
     * Receiver for registration success in-app message
     *
     */
    private BroadcastReceiver sMessageReceiver = new BroadcastReceiver() {
        /**
         * Processes run when registration id is successfully sent to a third-party server
         *
         * @param context Passed in data by this class
         * @param intent Passed in data by an external class
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            prgDialog.dismiss();
            Toast.makeText(applicationContext, "Registration successful!",
                    Toast.LENGTH_LONG).show();
            Intent i = new Intent(RegisterScreen.this, DashBoard.class);
            RegisterScreen.this.startActivity(i);
            finish();
        }
    };

    /**
     * Receiver for registration failure in-app message
     */
    private BroadcastReceiver fMessageReceiver = new BroadcastReceiver() {
        /**
         * Processes run when registration id sending fails
         *
         * @param context Passed in data by this class
         * @param intent Passed in data by an external class
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            prgDialog.dismiss();
            Toast.makeText(
                    applicationContext,
                    "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                    , Toast.LENGTH_LONG).show();
        }
    };

    /**
     * Attempts to register user with server if the register button is pressed
     *
     * @param view The register button
     */
    public void RegisterUser(View view) {
        prgDialog.setMessage("Registering you with the EasyChops Database...");
        prgDialog.show();
        String emailID = emailET.getText().toString();
        String restoId = idET.getText().toString();

        if (!TextUtils.isEmpty(emailID) && Utility.validate(emailID)) {

            if (checkPlayServices()) {

                Intent intent = new Intent(this, RegistrationIntentService.class);
                intent.putExtra("email", emailID);
                intent.putExtra("restaurant", restoId);
                startService(intent);

            }
        }
        else {
            prgDialog.dismiss();
            Toast.makeText(applicationContext, "Please enter valid email",
                    Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Statement whether or not Google Play Services are installed and running on current device
     *
     * @return false if not installed, true otherwise
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(
                        applicationContext,
                        "This device doesn't support Play services, App will not work normally",
                        Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Processes run when app is resumed
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }
}