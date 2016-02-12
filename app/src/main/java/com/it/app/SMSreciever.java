package com.it.app;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Rusty on 8/15/14.
 */
public class SMSreciever extends BroadcastReceiver{
    public String msg_from,msgBody,longMessage1,longMessage2,strt[],address,test[];
    LocationService loc;
    private double latitude;
    private double longitude;
    private String mess,onTracking,chkSMSreceived,recUnknown,recSpecific,serv1Offline,serv2Offline,noti;
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // TODO Auto-generated method stub
        DBAdapter.init(context);
        onTracking = DBAdapter.getValuesData("12");
        chkSMSreceived = DBAdapter.getValuesData("5");
        recUnknown = DBAdapter.getValuesData("6");
        recSpecific = DBAdapter.getValuesData("7");
        noti = DBAdapter.getValuesData("1");
        serv1Offline = DBAdapter.getValuesData("3");
        serv2Offline = DBAdapter.getValuesData("4");

        if(intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs = null;
            String date="",cname="";
            DateFormat df = new SimpleDateFormat("d/MMM/yyyy, HH:mm");
            Long today ;//= df.format(Calendar.getInstance().getTime());
            loc = new LocationService(context);
            if (bundle != null){
                //---retrieve the SMS message received---
                try{
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    msgs = new SmsMessage[pdus.length];
                    for(int i=0; i<msgs.length; i++){
                        msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        msg_from = msgs[i].getOriginatingAddress();
                        msgBody = msgs[i].getMessageBody();
                        strt = msgBody.split(",");
                        today = msgs[i].getTimestampMillis();
                        date = df.format(today);
                        cname= getContactName(context,msg_from);
                        test = msgBody.split(":");
                    }
                if (strt[0].equals("&_LatLng")&&onTracking.equals("false")){
                    this.abortBroadcast();
                    DBAdapter.addMessages(new DBMessages(cname+":"+msg_from,strt[1],strt[2],"",strt[3],strt[4],strt[5],strt[6]));
                    if(noti.equals("true")){notificationSMS(context,"From : "+cname+" "+msg_from);}
                    Toast.makeText(context,  "Success"  , Toast.LENGTH_SHORT).show();
                }
             if(onTracking.equals("true")&&(chkSMSreceived.equals("true"))){
                    getloc(context);
                    Toast.makeText(context,cname + ":"+msgBody  , Toast.LENGTH_SHORT).show();

                    if (test[0].equals("Location Coordinates")){
                        //nothing
                    }else if (strt[0].equals("&_LatLng")){
                        this.abortBroadcast();
                        DBAdapter.addMessages(new DBMessages(cname+":"+msg_from,strt[1],strt[2],"",strt[3],strt[4],strt[5],strt[6]+strt[7]));
                        if(noti.equals("true")){notificationSMS(context,"From : "+cname+" "+msg_from);}
                        Toast.makeText(context,  "Success"  , Toast.LENGTH_SHORT).show();
                    }else{
                        longMessage1 = "&_LatLng," + latitude + "," + longitude+",received,"+cname +":"+msg_from+","+msgBody+","+ date;
                        longMessage2 = "Location Coordinates:"+latitude + "," + longitude+"\n\nThe subject received an sms from " +cname +":"+msg_from+"\n\nSMS content: "+msgBody+"\n\nSMS Timestamp: "+date;
                        if(recSpecific.equals("true")){
                            List<DBReceivedConData> data = DBAdapter.getAllRCData();
                            for (DBReceivedConData dt : data) {
                              if(!dt.getNum().equals("empty")){
                                String test =  dt.getNum().substring(dt.getNum().indexOf("9"));
                                if(test.equals(msg_from.substring(msg_from.indexOf("9")))){
                                    if(!DBAdapter.getServerNum("1").equals("empty")){
                                        if(serv1Offline.equals("true")){mess = longMessage2;}
                                        else{mess = longMessage1;}
                                        SmsManager sms1 = SmsManager.getDefault();
                                        sms1.sendTextMessage(DBAdapter.getServerNum("1"), null, mess, null, null);
                                        Toast.makeText(context, "Sent" , Toast.LENGTH_SHORT).show();}
                                    if(!DBAdapter.getServerNum("2").equals("empty")){
                                        if(serv2Offline.equals("true")){mess = longMessage2;}
                                        else{mess = longMessage1;}
                                        SmsManager sms2 = SmsManager.getDefault();
                                        sms2.sendTextMessage(DBAdapter.getServerNum("2"), null, mess, null, null);
                                    }
                                                                     }
                              }//ifnotempty
                            }
                        }//ifrecspecific
                        if(recUnknown.equals("true")){
                            if(!contactExists(context,msg_from)){
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
                                Toast.makeText(context, "Sent" , Toast.LENGTH_SHORT).show();
                            }//ifExists
                        }//sentunknown
                    }
             }

                }catch(Exception e){
                    Log.d("Exception caught", e.getMessage());
                    Toast.makeText(context, "FAILED!\n" + e.getMessage() , Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void getloc(Context con){
       try{
        Location nwLoc = loc.getLocation(LocationManager.GPS_PROVIDER);
        if (nwLoc!=null){
            latitude = nwLoc.getLatitude();
            longitude = nwLoc.getLongitude();
        }
       }catch (Exception e){
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
            return "unknown";
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
    public void notificationSMS(Context context,String smsSummary){
        NotificationManager notifManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notif = new Notification(
                android.R.drawable.sym_action_chat, smsSummary,
                System.currentTimeMillis());
        notif.defaults |= Notification.DEFAULT_SOUND;
        notif.defaults |= Notification.DEFAULT_VIBRATE;
        notif.defaults |= Notification.DEFAULT_LIGHTS;

        // The notification will be canceled when clicked by the user...

        notif.flags |= Notification.FLAG_AUTO_CANCEL;

        // ...but we still need to provide and intent; an empty one will
        // suffice. Alter for your own app's requirement.

        Intent notificationIntent = new Intent(context,AMainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notif.setLatestEventInfo(context, "Mobile Tracker Message",
                "Message from client.", pi);

        notifManager.notify(0, notif);
    }
}
