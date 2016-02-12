package com.it.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rusty on 9/3/14.
 */
public class BootUpReceiver extends BroadcastReceiver {
    Double latitude,longitude;
    LocationService loc;
    Boolean isReg;
    String longMessage1,longMessage2,chkSim,serv1Offline,serv2Offline,mess;
    @Override
    public void onReceive(Context context, Intent intent) {
        DBAdapter.init(context);
        chkSim = DBAdapter.getValuesData("2");
        serv1Offline = DBAdapter.getValuesData("3");
        serv2Offline = DBAdapter.getValuesData("4");
        loc = new LocationService(context);
        isReg=false;
        DateFormat df = new SimpleDateFormat("d/MMM/yyyy, HH:mm");
        String today=df.format(Calendar.getInstance().getTime());
        //getloc();
        if(chkSim.equals("true")){
            TelephonyManager tMgr=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            String num1=tMgr.getLine1Number().substring(tMgr.getLine1Number().indexOf("9")),num2;
            List<DBSimnumbersData> data = DBAdapter.getAllSimnumData();
            for (DBSimnumbersData dt : data) {
              if(!dt.getNum().equals("empty")){
              num2=dt.getNum().substring(dt.getNum().toString().indexOf("9"));
                if (num1.equals(num2)){
                    isReg=true;
                    break;
                }else{
                    isReg=false;
                }

              }//ifnotempty
             }
             if(isReg){
                 Toast.makeText(context, "registered number :)" , Toast.LENGTH_SHORT).show();
             }else{
                 longMessage1 = "&_LatLng," + latitude + "," + longitude+",alert,subject,The Mobile Tracker Application detected unauthorized changing of SIM CARD number to "+tMgr.getLine1Number().toString()+","+today;
                 longMessage2 = "Location Coordinates:"+latitude + "," + longitude+"\nThe Mobile Tracker Application detected unauthorized changing of SIM CARD number to "+tMgr.getLine1Number().toString()+","+today;
                             if(!DBAdapter.getServerNum("1").equals("empty")){
                                 if(serv1Offline.equals("true")){mess = longMessage2;
                                 }else{mess = longMessage1;}
                                 SmsManager sms1 = SmsManager.getDefault();
                                 sms1.sendTextMessage(DBAdapter.getServerNum("1"), null, mess, null, null);
                                 Toast.makeText(context, "Sent" , Toast.LENGTH_SHORT).show();}
                             if(!DBAdapter.getServerNum("2").equals("empty")){
                                 if(serv2Offline.equals("true")){mess = longMessage2;
                                 }else{mess = longMessage1;}
                                 SmsManager sms2 = SmsManager.getDefault();
                                 sms2.sendTextMessage(DBAdapter.getServerNum("2"), null, mess, null, null);
                                 Toast.makeText(context, "Sent" , Toast.LENGTH_SHORT).show();
                             }
                 Toast.makeText(context, "unregistered number :(" +DBAdapter.getAllServnum().toString(), Toast.LENGTH_SHORT).show();
             }
        }//chksim
    }
    private void getloc(){
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
}
