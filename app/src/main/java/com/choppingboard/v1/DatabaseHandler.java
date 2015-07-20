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

    // Customer information in table name
    static final String CUSTOMER_INFO = "customerInfo";

    //Category info in table name
    static final String CAT = "CategoryInfo";

    //Material info in table name
    static final String MAT = "MaterialInfo";

    // The database object itself
    public SQLiteDatabase DB;

    // Order information fields
    private static final String ORD_KEY_ID = "OrderNumber";
    private static final String CUS_KEY_ID = "OrderNumber";
    private static final String Order_Json = "OrderString";
    private static final String Customer_Json = "OrderString";
    private static final String Status = "Status";

    private static final String CATEGORY = "Category";
    private static final String NAME = "Name";
    private static final String PRICE = "Price";
    private static final String DESC = "Description";
    private static final String NUMBER = "ExtraId";
    private static final String TITLE = "Title";


    /**
     * Setup process for this class when it is created as an object externally
     *
     * @param context Passed in data by an external class
     */
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Setup process for this class when it is created by an external class
     *
     * @param db The database to be filled with information
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ORDER_TABLE = "CREATE TABLE " + ORDER_INFO + "("
                + ORD_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Order_Json + " MEDIUMTEXT, "
                + Status + " TEXT "
                + ");";
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + CUSTOMER_INFO + "("
                + CUS_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + Customer_Json + " MEDIUMTEXT"
                + ")";
        String CREATE_CAT_TABLE = "CREATE TABLE " + CAT + "("
                + NUMBER + " INTEGER PRIMARY KEY, "
                + CATEGORY + " MEDIUMTEXT, "
                + NAME + " MEDIUMTEXT, "
                + PRICE + " TEXT, "
                + DESC + " MEDIUMTEXT "
                + ")";
        String CREATE_MAT_TABLE = "CREATE TABLE " + MAT + "("
                + NUMBER + " INTEGER PRIMARY KEY, "
                + NAME + " MEDIUMTEXT, "
                + PRICE + " TEXT, "
                + DESC + " MEDIUMTEXT, "
                + TITLE + " MEDIUMTEXT "
                + ")";

        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_ORDER_TABLE);
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_MAT_TABLE);

    }

    /**
     * Reset process for this class when the database requires an upgrade
     *
     * @param db The database to be upgraded
     * @param oldVersion Version number of the old database
     * @param newVersion Version number of the new database
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + CUSTOMER_INFO);
        db.execSQL("DROP TABLE IF EXISTS " + CAT);
        db.execSQL("DROP TABLE IF EXISTS " + MAT);

        // Create tables again
        onCreate(db);
    }

    public void createCAT(String str) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Log.v("lambo", str);

        try{
            JSONObject obj = new JSONObject(str);
            values.put(NUMBER, obj.getString("id"));
            values.put(CATEGORY, obj.getString("category"));
            values.put(NAME, obj.getString("name"));
            values.put(PRICE, obj.getString("price"));
            values.put(DESC, obj.getString("description"));

        }catch (JSONException e){
            e.printStackTrace();
        }

        db.replace(CAT, null, values);
        db.close();
    }


    /**
     * Adds a new order to the datatbase
     *
     * @param str The JSON string to be added to the database as an order
     */
    public  void addOrder(String str) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Order_Json, str); // order json
        values.put(Status, "0");
        // Inserting Row
        db.insert(ORDER_INFO, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Adds a new customer to the database
     *
     * @param str The JSON string to be added to the database as a customer
     */
    public  void addCustomer(String str) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Customer_Json, str); // customer json
        // Inserting Row
        db.insert(CUSTOMER_INFO, null, values);
        db.close(); // Closing database connection
    }

    /**
     * Changes the information of one customer in the database
     *
     * @param oldStr The old JSON data to be searched for
     * @param newStr The new JSON data to be applied to the found customer
     */
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

    /**
     * Changes the status of a single order
     *
     * @param str The JSON string of the order to be updated
     * @param newStat The new status value to be set for that order
     */
    public void updateStatus(String str, String newStat){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Status, newStat);
        db.update(ORDER_INFO, values, ORD_KEY_ID + " = '" + str + "'", null);
        db.close();
    }

    /**
     * Obtains the current status of an order
     *
     * @param str The JSON string of the order to be checked
     * @return answer The status of that order
     */
    public String getStatus(String str){
        String answer = "";

        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + ORDER_INFO + " WHERE " + ORD_KEY_ID + " = '" + str + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                answer = cursor.getString(2);
            } while (cursor.moveToNext());
        }

        return answer;

    }

    /**
     * Obtains every order status as a list
     *
     * @return OrderList The list of statuses
     */
    public ArrayList<String> getAllStatus() {
        ArrayList<String> OrderList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + ORDER_INFO + " ORDER BY " +ORD_KEY_ID + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                    String orderinfo = cursor.getString(2);
                    OrderList.add(orderinfo);
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        // return contact list
        return OrderList;
    }

    /**
     * Obtains every order as a list
     *
     * @return OrderList the list of orders
     */
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

    /**
     * Obtains every customer as a list
     *
     * @return CustomerList the list of customers
     */
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

    /**
     * Obtains every order number as a list
     *
     * @return OrderList the list of numbers
     */
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


    /**
     * Gives the number of orders currently in the database
     *
     * @return int number of orders
     */
    public int getOrderCount() {
        String countQuery = "SELECT  * FROM " + ORDER_INFO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();

    }
}