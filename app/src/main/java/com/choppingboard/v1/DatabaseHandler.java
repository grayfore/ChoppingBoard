package com.choppingboard.v1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
    static final String CUSTOMER_INFO = "customerInfo";
    public SQLiteDatabase DB;
    // Order information fields
    private static final String ORD_KEY_ID = "OrderNumber";
    private static final String CUS_KEY_ID = "OrderNumber";
    private static final String Order_Json = "OrderString";
    private static final String Customer_Json = "OrderString";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDER_TABLE = "CREATE TABLE " + ORDER_INFO + "("
                + ORD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Order_Json + " MEDIUMTEXT"
                + ")";
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + CUSTOMER_INFO + "("
                + CUS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Customer_Json + " MEDIUMTEXT"
                + ")";
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_INFO);

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

    public  void addCustomer(String str) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Customer_Json, str); // customer json
        // Inserting Row
        db.insert(CUSTOMER_INFO, null, values);
        db.close(); // Closing database connection
    }


    public void editCustomer(String oldStr, String newStr) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Customer_Json, newStr);
        Log.v("New string:", newStr);
        String whereClause = Customer_Json + " = '" + oldStr + "'";
        for(JSONObject j : getAllCustomers()) {
            Log.v("Old string:", j.toString());
        }
        db.update(CUSTOMER_INFO, values, whereClause, null);
        db.close();
    }

    // Getting All Orders
    public ArrayList<JSONObject> getAllOrders() {
        ArrayList<JSONObject> OrderList = new ArrayList<JSONObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ORDER_INFO + " ORDER BY " +ORD_KEY_ID + " DESC";

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
    public ArrayList<JSONObject> getAllCustomers() {
        ArrayList<JSONObject> CustomerList = new ArrayList<JSONObject>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CUSTOMER_INFO + " ORDER BY " +CUS_KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                try{
                    JSONObject customerinfo = new JSONObject(cursor.getString(1));
                    CustomerList.add(customerinfo);
                }catch (JSONException e){
                    e.printStackTrace();
                }
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        // return contact list
        return CustomerList;
    }

    //Getting all order numbers to use in listview
    public ArrayList<String> getAllNums() {
        ArrayList<String> OrderList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ORDER_INFO + " ORDER BY " +ORD_KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String num = cursor.getString(0);
                OrderList.add(num);
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