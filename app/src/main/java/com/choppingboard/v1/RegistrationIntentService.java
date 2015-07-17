/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.choppingboard.v1;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public static final String REG_ID = "regId";
    public static final String EMAIL_ID = "eMailId";
    private String email;
    private String restoID;

    /**
     * Setup process for this class when it is created as an object externally
     *
     */
    public RegistrationIntentService() {
        super(TAG);
    }

    /**
     * Setup process for this class when it is created as an intent externally
     *
     * @param intent Passed in data by an external class
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        email = intent.getStringExtra("email");
        restoID = intent.getStringExtra("restaurant");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                // [END get_token]
                Log.i(TAG, "GCM Registration Token: " + token);

                sendRegistrationToServer(token);

                subscribeTopics(token);

                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                // [END register_for_gcm]
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
            Intent registrationFailure = new Intent(QuickstartPreferences.REGISTRATION_FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationFailure);
        }
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        JSONObject json = new JSONObject();
        String targetUrl = ApplicationConstants.APP_SERVER_URL;
        try {
            json.put("token", token);
            json.put("email", email);
            json.put("restaurant_ID", restoID);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
        }
        if(executePost(targetUrl, json.toString()).equals("OK")) {
            storeRegIdinSharedPref(token);
            Intent registrationComplete = new Intent(QuickstartPreferences.SENT_TOKEN_TO_SERVER);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }
        else {
            Intent registrationFailure = new Intent(QuickstartPreferences.REGISTRATION_FAILED);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationFailure);
        }
    }

    /**
     * Connects to a third-party server to store generated registration info
     *
     * @param targetURL The location of the third-party database
     * @param urlParameters The registration info to be saved
     * @return response The response from the web server
     */
    private static String executePost(String targetURL, String urlParameters) {
        if(urlParameters.equals(null))
            return null;
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(targetURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            InputStream is = url.openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            String response = urlConnection.getResponseMessage();
            while ((line = rd.readLine()) != null) {
                response += line;
                response += "\r";
            }
            rd.close();
            Log.d(TAG, response);
            return response;
        }
        catch(Exception e) {
            e.printStackTrace();
            Log.d(TAG, e.toString());
            return null;
        }
        finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * Saves this user's registration token in their app preferences
     * @param regId
     */
    private void storeRegIdinSharedPref(String regId) {
        SharedPreferences prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(REG_ID, regId);
        editor.putString(EMAIL_ID, email);
        editor.commit();
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}
