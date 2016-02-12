package com.it.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rusty on 8/26/14.
 */
public class DBAdapter {

    /******* if debug is set true then it will show all Logcat message ***/
    public static final boolean DEBUG = true;

    /********** Logcat TAG ************/
    public static final String LOG_TAG = "DBAdapter";

    /************ Table Fields ************/
    public static final String KEY_ID = "_id";

    public static final String KEY_USER_NAME = "user_name";
    public static final String KEY_USER_UNAME = "user_uname";
    public static final String KEY_USER_PASS = "user_password";

    public static final String KEY_SIM_NUM = "sim_num";

    public static final String KEY_CONTACT_NAME = "con_name";
    public static final String KEY_CONTACT_NUM = "con_num";

    public static final String KEY_CLIENT = "clientnum";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String KEY_ADDRESS = "address";
    public static final String KEY_TYPE = "type";
    public static final String KEY_SMSCONTACT = "smscontact";
    public static final String KEY_SMSCONTENT = "smscontent";
    public static final String KEY_SMSDATE = "smsdate";

    public static final String KEY_NAME = "name";
    public static final String KEY_VALUE = "cvalue";

    public static final String KEY_SERVER_NUM = "server_num";

    /************* Database Name ************/
    public static final String DATABASE_NAME = "DB_sqllite";

    /**** Database Version (Increase one if want to also upgrade your database) ****/
    public static final int DATABASE_VERSION = 13;// started at 1

    /** Table names */
    public static final String USER_TABLE = "tbl_user";
    public static final String SIMNUM_TABLE = "tbl_simnum";
    public static final String SENTCONTACTS_TABLE = "tbl_sentcon";
    public static final String RECEIVEDONTACTS_TABLE = "tbl_receivedcon";
    public static final String MESSAGES_TABLE = "tbl_messages";
    public static final String SERVERNUM_TABLE = "tbl_servernum";
    public static final String VALUES_TABLE = "tbl_values";
    /**** Set all table with comma seperated like USER_TABLE,ABC_TABLE ******/
    private static final String[ ] ALL_TABLES = { USER_TABLE,SIMNUM_TABLE,SENTCONTACTS_TABLE,RECEIVEDONTACTS_TABLE,MESSAGES_TABLE,SERVERNUM_TABLE,VALUES_TABLE};

    /** Create table syntax */
    private static final String USER_CREATE = "create table tbl_user" +
            "( _id integer primary key autoincrement," +
            "user_name text not null," +
            "user_uname text not null," +
            "user_password text not null);";
    private static final String SIMNUM_CREATE = "create table tbl_simnum" +
            "( _id integer primary key autoincrement," +
            "sim_num text not null);";
    private static final String SENTCONTACTS_CREATE = "create table tbl_sentcon" +
            "( _id integer primary key autoincrement," +
            "con_name text not null," +
            "con_num text not null);";
    private static final String RECEIVEDCONTACTS_CREATE = "create table tbl_receivedcon" +
            "( _id integer primary key autoincrement," +
            "con_name text not null," +
            "con_num text not null);";
    private static final String MESSAGES_CREATE = "create table tbl_messages" +
            "( _id integer primary key autoincrement," +
            "clientnum text not null," +
            "lat text not null," +
            "lon text not null," +
            "address text ," +
            "type text not null," +
            "smscontact text not null," +
            "smscontent text not null," +
            "smsdate text not null);";
    private static final String SERVERNUM_CREATE = "create table tbl_servernum" +
            "( _id integer primary key autoincrement," +
            "server_num text not null);";
    private static final String VALUES_CREATE = "create table tbl_values" +
            "( _id integer primary key autoincrement," +
            "name text not null," +
            "cvalue text not null);";

    /********* Used to open database in syncronized way *********/
    private static DataBaseHelper DBHelper = null;

    protected DBAdapter() {

    }

    /********** Initialize database *********/
    public static void init(Context context) {
        if (DBHelper == null) {
            if (DEBUG)
                Log.i("DBAdapter", context.toString());
            DBHelper = new DataBaseHelper(context);
        }
    }

    /********** Main Database creation INNER class ********/
    private static class DataBaseHelper extends SQLiteOpenHelper {
        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            if (DEBUG)
                Log.i(LOG_TAG, "new create");
            try {
                db.execSQL(USER_CREATE);
                db.execSQL(SIMNUM_CREATE);
                db.execSQL(SENTCONTACTS_CREATE);
                db.execSQL(RECEIVEDCONTACTS_CREATE);
                db.execSQL(MESSAGES_CREATE);
                db.execSQL(SERVERNUM_CREATE);
                db.execSQL(VALUES_CREATE);
                for (int i = 1;i<=10;i++){
                    addSimNum("empty",db);
                    addSentConData("empty","empty",db);
                    addReceivedConData("empty","empty",db);
                    Log.i(LOG_TAG, "Exception onCreate() exception done :"+i);
                }
                addVals("notification","false",db);
                addVals("changesim","false",db);
                addVals("server1offline","false",db);
                addVals("server2offline","false",db);
                addVals("smsreceived","false",db);
                addVals("recunknown","false",db);
                addVals("recspecific","false",db);
                addVals("smssent","false",db);
                addVals("senunknown","false",db);
                addVals("senspecific","false",db);
                addVals("accesscode","1234",db);
                addVals("tracking","false",db);
                addServerNum("empty",db);
                addServerNum("empty",db);
                //db.close();
            } catch (Exception exception) {
                if (DEBUG)
                    Log.i(LOG_TAG, "Exception onCreate() exception"+exception.getMessage());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (DEBUG)
                Log.w(LOG_TAG, "Upgrading database from version" + oldVersion
                        + "to" + newVersion + "...");

            for (String table : ALL_TABLES) {
                db.execSQL("DROP TABLE IF EXISTS " + table);
            }
            onCreate(db);
        }

    } // Inner class closed


    /***** Open database for insert,update,delete in syncronized manner ****/
    public static synchronized SQLiteDatabase open() throws SQLException {
        return DBHelper.getWritableDatabase();
    }


    /************* General functions*************/


    /*********** Escape string for single quotes (Insert,Update) ********/
    private static String sqlEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            //aReturn = aString.replace(", );
            aReturn = DatabaseUtils.sqlEscapeString(aString);
            // Remove the enclosing single quotes ...
            aReturn = aReturn.substring(1, aReturn.length() - 1);
        }

        return aReturn;
    }

    /********** UnEscape string for single quotes (show data) ************/
    private static String sqlUnEscapeString(String aString) {
        String aReturn = "";

        if (null != aString) {
            aReturn = aString.replace("","");
        }

        return aReturn;
    }

    /********* User data functons *********/
    /********* INSERT *********/
    public static void addSimNum(String a,SQLiteDatabase db) {
        String num = sqlEscapeString(a);
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_SIM_NUM, num);
        db.insert(SIMNUM_TABLE, null, cVal);
    }
    public static void addSentConData(String a,String b,SQLiteDatabase db) {
        String name = sqlEscapeString(a);
        String num = sqlEscapeString(b);
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_CONTACT_NAME, name);
        cVal.put(KEY_CONTACT_NUM, num);
        db.insert(SENTCONTACTS_TABLE, null, cVal);
    }
    public static void addReceivedConData(String a,String b,SQLiteDatabase db) {
        String name = sqlEscapeString(a);
        String num = sqlEscapeString(b);
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_CONTACT_NAME, name);
        cVal.put(KEY_CONTACT_NUM, num);
        db.insert(RECEIVEDONTACTS_TABLE, null, cVal);
    }
    public static void addMessages(DBMessages mData) {
        final SQLiteDatabase db = open();
        String clientnum = sqlEscapeString(mData.getClientnum());
        String lat = sqlEscapeString(mData.getLat());
        String lon = sqlEscapeString(mData.getLon());
        String address = sqlEscapeString(mData.getAddress());
        String type = sqlEscapeString(mData.getType());
        String smscontact = sqlEscapeString(mData.getSMScontact());
        String smscontent = sqlEscapeString(mData.getSMScontent());
        String smsdate = sqlEscapeString(mData.getSMSdate());
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_CLIENT, clientnum);
        cVal.put(KEY_LAT, lat);
        cVal.put(KEY_LON, lon);
        cVal.put(KEY_ADDRESS, address);
        cVal.put(KEY_TYPE, type);
        cVal.put(KEY_SMSCONTACT, smscontact);
        cVal.put(KEY_SMSCONTENT, smscontent);
        cVal.put(KEY_SMSDATE, smsdate);
        // Insert user values in database
        db.insert(MESSAGES_TABLE, null, cVal);
        db.close(); // Closing database connection
    }
    public static void addServerNum(String a,SQLiteDatabase db) {
        String snum = sqlEscapeString(a);
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_SERVER_NUM, snum);
        db.insert(SERVERNUM_TABLE, null, cVal);
    }
    public static void addVals(String a,String b,SQLiteDatabase db) {
        String val1 = sqlEscapeString(a);
        String val2 = sqlEscapeString(b);
        ContentValues cVal = new ContentValues();
        cVal.put(KEY_NAME, val1);
        cVal.put(KEY_VALUE, val2);
        db.insert(VALUES_TABLE, null, cVal);
    }
    /********* INSERT *********/
    /********* UPDATE *********/
    // Updating single data
    public static void updateServnum(String num,int id) {
        final SQLiteDatabase db = open();
        ContentValues values = new ContentValues();
        values.put(KEY_SERVER_NUM,num);
        db.update(SERVERNUM_TABLE, values, "_id "+"="+id, null);
        db.close();

    }
    public static void updateSimnumData(String newnum,int id) {
        final SQLiteDatabase db = open();
        ContentValues values = new ContentValues();
        values.put(KEY_SIM_NUM, newnum);
        db.update(SIMNUM_TABLE, values, "_id "+"="+id, null);
        db.close();
    }
    public static void updateValues(String newnum,int id) {
        final SQLiteDatabase db = open();
        ContentValues values = new ContentValues();
        values.put(KEY_VALUE, newnum);
        db.update(VALUES_TABLE, values, "_id "+"="+id, null);
        db.close();
    }
    public static void updateSC(String name,String num,int id) {
        final SQLiteDatabase db = open();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME,name);
        values.put(KEY_CONTACT_NUM,num);
        db.update(SENTCONTACTS_TABLE, values, "_id "+"="+id, null);
        db.close();

    }
    public static void updateRC(String name,String num,int id) {
        final SQLiteDatabase db = open();
        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_NAME,name);
        values.put(KEY_CONTACT_NUM,num);
        db.update(RECEIVEDONTACTS_TABLE, values, "_id "+"="+id, null);
        db.close();

    }
    public static void updateMessages(String address,int id) {
        final SQLiteDatabase db = open();
        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS, address);
        db.update(MESSAGES_TABLE, values, "_id "+"="+id, null);
        //db.close();
    }
    /********* UPDATE *********/

    // Getting single contact
    public static String getValuesData(String val){
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( "select * from tbl_values where _id = "+val.toString(), null );
        if (cursor != null)
           cursor.moveToFirst();
        String x =cursor.getString(2);
        return x;
    }
    public static String getServerNum(String val){
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( "select * from tbl_servernum where _id = "+val.toString(), null );
        if (cursor != null)
            cursor.moveToFirst();
        String x =cursor.getString(1);
        return x;
    }
    public static String getSIMData(String val){
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( "select * from tbl_simnum where _id = "+val.toString(), null );
        if (cursor != null)
            cursor.moveToFirst();
        String x =cursor.getString(1);
        return x;
    }
    public static String getRC(String val){
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( "select * from tbl_receivedcon where _id = "+val.toString(), null );
        if (cursor != null)
            cursor.moveToFirst();
        String x = cursor.getString(1)+": "+cursor.getString(2);
        return x;
    }
    public static String getSC(String val){
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( "select * from tbl_sentcon where _id = "+val.toString(), null );
        if (cursor != null)
            cursor.moveToFirst();
        String x = cursor.getString(1)+": "+cursor.getString(2);
        return x;
    }
    /********* GET ALL DATA *********/
    // Getting All User data
    public static List<DBUserData> getAllUserData() {

        List<DBUserData> contactList = new ArrayList<DBUserData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USER_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBUserData data = new DBUserData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setUname(cursor.getString(2));
                data.setUpass(cursor.getString(3));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return contactList;
    }

    public static List<DBSimnumbersData> getAllSimnumData() {

        List<DBSimnumbersData> simnumList = new ArrayList<DBSimnumbersData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SIMNUM_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBSimnumbersData data = new DBSimnumbersData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setNum(cursor.getString(1));

                // Adding contact to list
                simnumList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return simnumList;
    }
    public static List<DBServernum> getAllServnum() {

        List<DBServernum> simnumList = new ArrayList<DBServernum>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SERVERNUM_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBServernum data = new DBServernum();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setNum(cursor.getString(1));

                // Adding contact to list
                simnumList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return simnumList;
    }
    public static List<DBSentConData> getAllSCData() {

        List<DBSentConData> contactList = new ArrayList<DBSentConData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SENTCONTACTS_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBSentConData data = new DBSentConData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setNum(cursor.getString(2));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return contactList;
    }
    public static List<DBReceivedConData> getAllRCData() {

        List<DBReceivedConData> contactList = new ArrayList<DBReceivedConData>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + RECEIVEDONTACTS_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBReceivedConData data = new DBReceivedConData();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setName(cursor.getString(1));
                data.setNum(cursor.getString(2));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }
        return contactList;
    }
    public static List<DBMessages> getMessages() {

        List<DBMessages> contactList = new ArrayList<DBMessages>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + MESSAGES_TABLE;


        // Open database for Read / Write
        final SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery ( selectQuery, null );

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DBMessages data = new DBMessages();
                data.setID(Integer.parseInt(cursor.getString(0)));
                data.setClientnum(cursor.getString(1));
                data.setLat(cursor.getString(2));
                data.setLon(cursor.getString(3));
                data.setAddress(cursor.getString(4));
                data.setType(cursor.getString(5));
                data.setSMScontact(cursor.getString(6));
                data.setSMScontent(cursor.getString(7));
                data.setSMSdate(cursor.getString(8));

                // Adding contact to list
                contactList.add(data);
            } while (cursor.moveToNext());
        }

        // return user list
        return contactList;
    }

    /********* DELETE *********/
    // Deleting single contact
    public static void deleteUserData(DBUserData data) {
        final SQLiteDatabase db = open();
        db.delete(USER_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }
    public static void deleteRCData(DBReceivedConData data) {
        final SQLiteDatabase db = open();
        db.delete(RECEIVEDONTACTS_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }
    public static void deleteSCData(DBSentConData data) {
        final SQLiteDatabase db = open();
        db.delete(SENTCONTACTS_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getID()) });
        db.close();
    }
    public static void deleteSimData(int id) {
        final SQLiteDatabase db = open();
        db.delete(SIMNUM_TABLE, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // Getting dataCount

    public static int getSimnumcount() {

        final SQLiteDatabase db = open();
        int cnt;
        String countQuery = "SELECT  * FROM " + SIMNUM_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public static int getServnumcount() {

        final SQLiteDatabase db = open();
        int cnt;
        String countQuery = "SELECT  * FROM " + SERVERNUM_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public static int getRCcount() {

        final SQLiteDatabase db = open();
        int cnt;
        String countQuery = "SELECT  * FROM " + RECEIVEDONTACTS_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public static int getSCcount() {

        final SQLiteDatabase db = open();
        int cnt;
        String countQuery = "SELECT  * FROM " + SENTCONTACTS_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public static int getSMSCount() {

        final SQLiteDatabase db = open();

        String countQuery = "SELECT  * FROM " + MESSAGES_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
    public static int getVAluescount(){
        final SQLiteDatabase db = open();
        String countQuery = "SELECT  * FROM " + VALUES_TABLE;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }
}  // CLASS CLOSED
