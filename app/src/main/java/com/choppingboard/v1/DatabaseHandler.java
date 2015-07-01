//package com.choppingboard.v1;
//
//import android.content.Context;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
///**
// * Created by Jeff on 7/1/15.
// */
//public class DatabaseHandler extends SQLiteOpenHelper {
//
//    // All Static variables
//    // Database Version
//    private static final int DATABASE_VERSION = 1;
//
//    // Database Name
//    private static final String DATABASE_NAME = "orderManager";
//
//    // Order information table name
//    static final String ORDER_INFO = "orderInfo";
//    public SQLiteDatabase DB;
//    // Order information fields
//    private static final String KEY_ID = "id";
//    private static final String CUS_NAME = "cus_name";
//    private static final String CUS_PH_NO = "cus_phone_number";
//    private static final String CUS_ADD= "cus_add";
//    private static final String RES_NAME = "res_name";
//    private static final String RES_PH_NO = "res_phone_numbere";
//    private static final String RES_ADD = "res_addr";
//
//    public DatabaseHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
