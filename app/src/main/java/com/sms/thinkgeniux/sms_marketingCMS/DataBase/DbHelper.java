package com.sms.thinkgeniux.sms_marketingCMS.DataBase;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.ContactItem;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.GroupItem;
import com.sms.thinkgeniux.sms_marketingCMS.PojoClass.Sms_Log_Pojo;

import java.util.ArrayList;

/**
 * Created by Kuncoro on 22/12/2016.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;

    static final String DATABASE_NAME = "SMSMarketing.db";

    public static final String TABLE_NAME_Group = "Groups";
    public static final String TABLE_NAME_CONTACTS = "Contacts";
    public static final String GROUP_COLUMN_ID = "id";
    public static final String COLUMN_NAME_Group = "name";

//    public static final String COLUMN_ADDRESS = "address";
////////////////////CONTACT TABLE NAMES///////////////////////
    public static final String COLUMN_NAME_CONTACTS = "number";
    public static final String COLUMN_id_CONTACTS_Group = "Groupid";
    public static final String COLUMN_NAME_CONTACTS_Group = "Groupname";

    public static final String COLUMN_ID = "id";

    /////////////////LOG TABLE COLUME NAME//////////////////////////////

    public static final String TABLE_NAME_LOG = "logSms";
    public static final String COLUMN_LOG_ID = "id";
    public static final String COLUMN_LOG_TO = "smsto";
    public static final String COLUMN_LOG_FROM = "smsfrom";
    public static final String COLUMN_LOG_MESSAGE = "message";
    public static final String COLUMN_LOG_TIME = "time";

////////////////////////////////////
    ////////////////////BRAND TABLE/////////////////////
public static final String TABLE_NAME_BRAND = "brands";
    public static final String COLUMN_BRAND_ID = "brandid";
    public static final String COLUMN_Name_BRAnd = "brandname";
    //////////////////////////////////////////////

    public DbHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_GROUP_TABLE =
                "CREATE TABLE " + TABLE_NAME_Group + " (" +
                        COLUMN_LOG_ID + " INTEGER PRIMARY KEY autoincrement, " +
                        COLUMN_NAME_Group + " TEXT NOT NULL " +
//                COLUMN_ADDRESS + " TEXT NOT NULL" +
                " )";

        final String  SQL_CONTACT_TABLE=
                "CREATE TABLE " + TABLE_NAME_CONTACTS + " (" +
                        COLUMN_ID + " INTEGER PRIMARY KEY autoincrement , " +
                        COLUMN_NAME_CONTACTS + " TEXT NOT NULL, " +
                        COLUMN_NAME_CONTACTS_Group + " TEXT NOT NULL," +
                        COLUMN_id_CONTACTS_Group + " INTEGER NOT NULL" +
                        " )";
        final String SQL_LOG_SMS_TABLE =
                "CREATE TABLE " + TABLE_NAME_LOG + " (" +
                        GROUP_COLUMN_ID + " INTEGER PRIMARY KEY autoincrement, " +
                        COLUMN_LOG_TO + " TEXT NOT NULL, " +
                        COLUMN_LOG_FROM + " TEXT NOT NULL," +
                        COLUMN_LOG_MESSAGE + " INTEGER NOT NULL," +
                        COLUMN_LOG_TIME + " INTEGER NOT NULL" +
//                COLUMN_ADDRESS + " TEXT NOT NULL" +
                        " )";
        final String SQL_BRAND_TABLE =
                "CREATE TABLE " + TABLE_NAME_BRAND + " (" +
                        COLUMN_BRAND_ID + " INTEGER PRIMARY KEY autoincrement, " +
                        COLUMN_Name_BRAnd + " TEXT NOT NULL " +
//                COLUMN_ADDRESS + " TEXT NOT NULL" +
                        " )";
        db.execSQL(SQL_BRAND_TABLE);
        db.execSQL(SQL_LOG_SMS_TABLE);
        db.execSQL(SQL_GROUP_TABLE);
        db.execSQL(SQL_CONTACT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_Group);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_BRAND);
        onCreate(db);
    }

    public ArrayList<GroupItem> getAllDataGroup() {
        ArrayList<GroupItem> grouplist;
        grouplist = new ArrayList<GroupItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_Group;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put(COLUMN_ID, cursor.getString(0));
//                map.put(COLUMN_NAME_Group, cursor.getString(1));
//                map.put(COLUMN_ADDRESS, cursor.getString(2));
//                wordList.add(map);
                grouplist.add(new GroupItem(cursor.getString(0),cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + grouplist);

        database.close();
        return grouplist;
    }
    public ArrayList<GroupItem> getAllDataBrands() {
        ArrayList<GroupItem> grouplist;
        grouplist = new ArrayList<GroupItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_BRAND;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put(COLUMN_ID, cursor.getString(0));
//                map.put(COLUMN_NAME_Group, cursor.getString(1));
//                map.put(COLUMN_ADDRESS, cursor.getString(2));
//                wordList.add(map);
                grouplist.add(new GroupItem(cursor.getString(0),cursor.getString(1)));
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + grouplist);

        database.close();
        return grouplist;
    }
//    public ArrayList<Sms_Log_Pojo> getAllDataSmsLog() {
//        ArrayList<Sms_Log_Pojo> logArrayList;
//        logArrayList = new ArrayList<Sms_Log_Pojo>();
//        String selectQuery = "SELECT * FROM " + TABLE_NAME_LOG;
//        SQLiteDatabase database = this.getWritableDatabase();
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        if (cursor.moveToFirst()) {
//            do {
////                HashMap<String, String> map = new HashMap<String, String>();
////                map.put(COLUMN_ID, cursor.getString(0));
////                map.put(COLUMN_NAME_Group, cursor.getString(1));
////                map.put(COLUMN_ADDRESS, cursor.getString(2));
////                wordList.add(map);
//                logArrayList.add(new Sms_Log_Pojo(cursor.getString(0),cursor.getString(1),
//                        cursor.getString(2),cursor.getString(3),cursor.getString(4)));
//            } while (cursor.moveToNext());
//        }
//
//        Log.e("select sqlite ", "" + logArrayList);
//
//        database.close();
//        return logArrayList;
//    }
    public ArrayList<ContactItem> getAllDataContacts(int GroupId) {
        ArrayList<ContactItem> contactlist;
        contactlist = new ArrayList<ContactItem>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME_CONTACTS+
                " WHERE " + COLUMN_id_CONTACTS_Group + "=" + "'" + GroupId + "'";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
//                HashMap<String, String> map = new HashMap<String, String>();
//                map.put(COLUMN_ID, cursor.getString(0));
//                map.put(COLUMN_NAME_Group, cursor.getString(1));
////                map.put(COLUMN_ADDRESS, cursor.getString(2));
////                wordList.add(map);
//                contactlist.add(new ContactItem(cursor.getString(0),
//                        cursor.getString(1),cursor.getString(2)));
            } while (cursor.moveToNext());
        }

        Log.e("select sqlite ", "" + contactlist);

        database.close();
        return contactlist;
    }
    public void insertGroup(String name) {

        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_NAME_Group + " (name) " +
                "VALUES ('" + name + "')";

        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }
    public void insertBrands(String name) {

        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_NAME_BRAND + " (brandname) " +
                "VALUES ('" + name + "')";

        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }
    public void insertContacts(String number,String GroupName,int Group_id) {

        SQLiteDatabase database = this.getWritableDatabase();
        String queryValues = "INSERT INTO " + TABLE_NAME_CONTACTS + " ( number,Groupname,Groupid) " +
                "VALUES ('" + number + "', '" + GroupName + "','" + Group_id + "')";

        Log.e("insert sqlite ", "" + queryValues);
        database.execSQL(queryValues);
        database.close();
    }
    public void insertSmsLog(String to, String from, String message, String time) {
        String queryValues = "INSERT INTO " + TABLE_NAME_LOG + "(smsto,smsfrom,message,time)" +
                "VALUES ('" + to + "', '" + from + "','" + message + "','" + time + "')";
        SQLiteDatabase database = this.getWritableDatabase();
        database.execSQL(queryValues);
        database.close();
    }

//    public void update(int id, String name, String address) {
//        SQLiteDatabase database = this.getWritableDatabase();
//
//        String updateQuery = "UPDATE " + TABLE_NAME_Group + " SET "
//                + COLUMN_NAME_Group + "='" + name + "', "
////                + COLUMN_ADDRESS + "='" + address + "'"
//                + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";
//        Log.e("update sqlite ", updateQuery);
//        database.execSQL(updateQuery);
//        database.close();
//    }

    public void deleteGroup(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_NAME_Group + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    public void deleteContact(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_NAME_CONTACTS + " WHERE " + COLUMN_ID + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    public void deleteLog(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_NAME_LOG + " WHERE " + GROUP_COLUMN_ID + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }
    public void deleteBrand(int id) {
        SQLiteDatabase database = this.getWritableDatabase();

        String updateQuery = "DELETE FROM " + TABLE_NAME_BRAND + " WHERE " + COLUMN_BRAND_ID + "=" + "'" + id + "'";
        Log.e("update sqlite ", updateQuery);
        database.execSQL(updateQuery);
        database.close();
    }

}
