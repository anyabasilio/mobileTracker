package com.it.app;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rusty on 8/18/14.
 */
public class SMSListAdapter  extends BaseAdapter
{
    private Context mContext;
    Cursor cursor;
    public SMSListAdapter(Context context,Cursor cur)
    {
        super();
        mContext=context;
        cursor=cur;
        DBAdapter.init(mContext);
    }

    public int getCount()
    {
             // return the number of records in cursor
        return cursor.getCount();
    }

    // getView method is called for each item of ListView
    public View getView(int position,  View view, ViewGroup parent)
    {
        // inflate the layout for each item of listView
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.samp, null);
        cursor.moveToPosition(position);

        // fetch the sender number and sms body from cursor
       // String number=cursor.getString(cursor.getColumnIndex("address"));
        //String body=cursor.getString(cursor.getColumnIndex("body"));
        String num=cursor.getString(cursor.getColumnIndex("smscontact"));
        String bod=cursor.getString(cursor.getColumnIndex("smscontent"));
        String tim=cursor.getString(cursor.getColumnIndex("smsdate"));
        String typ=cursor.getString(cursor.getColumnIndex("type"));
        String lat=cursor.getString(cursor.getColumnIndex("lat"));
        String lon=cursor.getString(cursor.getColumnIndex("lon"));
        String address=cursor.getString(cursor.getColumnIndex("address"));
        String locate=address;
            //String num = "+63" +dt.getSMScontact().indexOf("9");
             //fetch the sender number and sms body from cursor
        if((!lat.equals("0.0")||!lon.equals("0.0"))||address.equals("")){locate = getMyLocationAddress(Double.parseDouble(lat),Double.parseDouble(lon),mContext,Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));}
            String senderNumber=num;
            String smsBody=bod+"\n\nTimeStamp: "+tim+"\nLocation:"+locate;
        //String senderNumber=number+getContactName(mContext,number);
        //String smsBody=body;
        // get the reference of textViews

        //TextView textViewConatctNumber=(TextView)view.findViewById(R.id.textView);
        TextView textViewSMSBody=(TextView)view.findViewById(R.id.textView2);

        // Set the Sender number and smsBody to respective TextViews
        //textViewConatctNumber.setText(senderNumber);
        textViewSMSBody.setText(smsBody);

        return view;

    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public String getMyLocationAddress(Double lat,Double lng,Context con,int id) {
        String retStatement="";
        Geocoder geocoder= new Geocoder(con, Locale.ENGLISH);
        try {
           if(haveNetworkConnection(con)){
            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(lat,lng, 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }
                retStatement= strAddress.toString();
                DBAdapter.updateMessages(strAddress.toString(),id);
            }

           }else{retStatement= "Failed to get exact address. Please connect to the internet and refresh messages." ;}
            return retStatement;
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            retStatement= "(Enable the internet then refresh messages to get the specific location.)"+e.getMessage();
            return retStatement;
        }

    }
    public void updateAdapter(Cursor arrylst) {
        this.cursor= arrylst;
        notifyDataSetChanged();
    }
    private boolean haveNetworkConnection(Context c) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }
}
