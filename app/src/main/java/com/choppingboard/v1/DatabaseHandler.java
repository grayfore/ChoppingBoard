package com.choppingboard.v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jeff on 7/1/15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "orderManager";

    // Order information table name
    static final String ORDER_INFO = "orderInfo";
    public SQLiteDatabase DB;
    // Order information fields
    private static final String KEY_ID = "OrderNumber";
    private static final String Order_Json = "OrderString";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDER_TABLE = "CREATE TABLE " + ORDER_INFO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Order_Json + " MEDIUMTEXT"
                + ")";
        db.execSQL(CREATE_ORDER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_INFO);

        // Create tables again
        onCreate(db);
    }

    // Adding new order
    public  void addOrder(String str) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order_Json, str); // order json
        // Inserting Row
        db.insert(ORDER_INFO, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Orders

    public ArrayList<JSONObject> getAllOrders() {
        ArrayList<JSONObject> OrderList = new ArrayList<JSONObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ORDER_INFO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try{
                    JSONObject orderinfo = new JSONObject(cursor.getString(1));
                    OrderList.add(orderinfo);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        // return contact list
        return OrderList;
    }
    
    // Getting contacts Count
    public int getOrderCount() {
        String countQuery = "SELECT  * FROM " + ORDER_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();

    }


}