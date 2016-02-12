package com.it.app;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rusty on 7/31/14.
 */
public class AMapActivity extends Activity{
    GoogleMap mMap;
    ActionBar a;
    Cursor c ;
    Double lat[],lon[];
    private ArrayList<LatLng> arrayPoints = null;
    PolylineOptions polylineOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    try{
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        createMapView();
        a= getActionBar();
        a.setDisplayHomeAsUpEnabled(true);
        a.setTitle("Map");
        SQLiteDatabase db  = DBAdapter.open();
        c = db.rawQuery(getIntent().getExtras().getString("QUE"),null);
        c.moveToFirst();
        for (int j = 1;j<=c.getCount();j++) {
         if(!c.getString(c.getColumnIndex("lat")).equals("0.0")&&!c.getString(c.getColumnIndex("lon")).equals("0.0")){
           addMarker(new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("lon")))),c.getString(c.getColumnIndex("address")),c.getString(c.getColumnIndex("smsdate")));
           //setPoly(new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("lon")))));

                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                         new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("lon")))),14));

         }
             c.moveToNext();
        }
        /*mMap.addPolyline(new PolylineOptions().geodesic(true)
                .width(3)
                .color(Color.RED)
                .add(new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("lon"))))));
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("lon")))))
                        .title("Subject's location")
                        .snippet(c.getString(c.getColumnIndex("address")))
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                        .anchor(0.0f, 1.0f)
                        .draggable(true)
                );
        for (int j = 1;j>c.getCount();j++) {
            //polyline.add(new LatLng(Double.parseDouble(c.getString(c.getColumnIndex("lat"))), Double.parseDouble(c.getString(c.getColumnIndex("lon")))));
        }
        Toast.makeText(this,Integer.toString(c.getCount()), Toast.LENGTH_LONG).show();

        mMap.addMarker(new MarkerOptions()
                .title("Client's location")
                .snippet("point:1")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .position(new LatLng(14.71752772, 121.03874456))
                .anchor(0.0f, 1.0f)
                .draggable(true)
        );*/
    }catch(Exception e){

    }
    }
    public void setPoly(LatLng point) {
        //add marker
        //addMarker(point);
        // settin polyline in the map
        /*polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        arrayPoints.add(point);
        polylineOptions.addAll(arrayPoints);
        mMap.addPolyline(polylineOptions);*/
    }

    private void createMapView(){
        try {
            if(null == mMap){
                mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                if(null == mMap) {
                    Toast.makeText(getApplicationContext(),"Error creating map",Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mapmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }else if (id == R.id.action_refresh) {
            LocationService loc = new LocationService(this);
            Double latitude = 0.0,longitude = 0.0;
            Location nwLoc = loc.getLocation(LocationManager.GPS_PROVIDER);
            if (nwLoc!=null){
                latitude = nwLoc.getLatitude();
                longitude = nwLoc.getLongitude();

            }
            Toast.makeText(this,getMyLocationAddress(14.71752772,121.03874456,this), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Adds a marker to the map
     */
    private void addMarker(LatLng a,String b,String c){

        /** Make sure that the map has been initialised **/
        if(null != mMap){
            mMap.addMarker(new MarkerOptions()
                    .position(a)
                    .title(b)
                    .snippet(c)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .draggable(true)
            );
        }
    }
    public String getMyLocationAddress(Double lat,Double lng,Context con) {
        String retStatement="";
        Geocoder geocoder= new Geocoder(con, Locale.ENGLISH);
        try {
            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(lat,lng, 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();

                for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                    strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                }
                retStatement= strAddress.toString();
                Toast.makeText(con,retStatement, Toast.LENGTH_LONG).show();
            }else{retStatement= "No Location found!" ;}
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("GETADDRESS",e.getMessage());
            Toast.makeText(con,"Could not get address..!"+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return retStatement;
    }
}
