package com.choppingboard.v1;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jeff on 7/10/15.
 */
public class UpdateStatus extends IntentService {

    private static final String TAG = "UpdateStatus";
    private String orderId;
    private String status;
    private String ordKeyId;
    DatabaseHandler db;

    public UpdateStatus() {
        super(TAG);
    }

    /**
     *  This method handles the intent that is created wherever a status needs to be updated (in other classes).
     *  Retrieves intent extras to update remote database and local database
     *
     * @param intent
     */

    @Override
    protected void onHandleIntent(Intent intent) {
        db = new DatabaseHandler(this);
        //Retrieve all the intent extras
        orderId = intent.getStringExtra("orderId");
        status = intent.getStringExtra("status");
        ordKeyId = intent.getStringExtra("ordKeyId");
        //change status locally
        db.updateStatus(ordKeyId, status);

        //change status on database
        String targetUrl = "http://choppingboard.comuf.com/status.php";
        JSONObject json = new JSONObject();
        try {
            json.put("orderId", orderId);
            json.put("status", status);
            updateStatus(targetUrl, json.toString());
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    /**
     * Update the remote database by making a url connection.
     * Returns "OK" and URL response codes
     *
     * @param targetURL
     * @param urlParameters
     * @return
     */

    public static String updateStatus(String targetURL, String urlParameters) {
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
}