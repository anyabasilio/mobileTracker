package com.it.app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * Created by Rusty on 7/31/14.
 */
public class AMessagesActivity extends ListActivity implements ActionBar.OnNavigationListener,SearchView.OnQueryTextListener{
    ListView listViewSMS;
    Cursor cursor;
    //SMSListAdapter smsListAdapter;
    Context context;
    ActionBar a;
    String query,passval;
    private ArrayList<SpinnerNav> navSpinner;
    private ArrayList<Integer> IDS;
    private TitleNavigationAdapter adapter;
    SQLiteDatabase db;
    LoadContactsAyscn lca;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        db= DBAdapter.open();
        passval =getIntent().getExtras().getString("CLIENT");
        listViewSMS=(ListView)findViewById(android.R.id.list);
        query = "SELECT * FROM tbl_messages where type = 'received' and clientnum = '"+passval+"' order by _id DESC";
        a= getActionBar();
        a.setDisplayHomeAsUpEnabled(true);
        a.setTitle("");
        a.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        navSpinner = new ArrayList<SpinnerNav>();
        navSpinner.add(new SpinnerNav("Received", android.R.drawable.stat_sys_download));
        navSpinner.add(new SpinnerNav("Sent", android.R.drawable.stat_sys_upload));
        navSpinner.add(new SpinnerNav("Alert", android.R.drawable.stat_sys_warning));
        adapter = new TitleNavigationAdapter(getApplicationContext(), navSpinner);
        a.setListNavigationCallbacks(adapter, this);
        // TODO Auto-generated method stub
        lca = new LoadContactsAyscn();
        lca.execute();
        /*listViewSMS.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View v,int position, long arg3)
            {
                TextView textViewSMSSender=(TextView)v.findViewById(R.id.textView);
                TextView textViewSMSBody=(TextView)v.findViewById(R.id.textView2);
                String smsSender=textViewSMSSender.getText().toString();
                String smsBody=textViewSMSBody.getText().toString();
                AlertDialog dialog = new AlertDialog.Builder(AMessagesActivity.this).create();
                dialog.setTitle("SMS From : "+smsSender);
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                dialog.setMessage(smsBody);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                                return;
                            }
                        });
                dialog.show();
            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mnumess, menu);
        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView ;
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        listViewSMS = getListView();
        listViewSMS.setTextFilterEnabled(true);
        setupSearchView(searchView);

        return true;
    }
    private void setupSearchView(SearchView mSearchView) {

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search Messages");
    }
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            listViewSMS.clearTextFilter();
        } else {
            listViewSMS.setFilterText(newText);
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean onClose() {
        return false;
    }

    protected boolean isAlwaysExpanded() {
        return false;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        else if (id == R.id.action_map){
            finish();
            Intent i = new Intent(getBaseContext(), AMapActivity.class);
            i.putExtra("QUE",query);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed()
    {
        finish();
        super.onBackPressed();  // optional depending on your needs
    }
    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Action to be taken after selecting a spinner item
      switch (itemPosition){
           case 0:
                query = "SELECT  * FROM tbl_messages where type = 'received' and clientnum = '"+passval+"' order by _id DESC";
               break;
            case 1:
                query = "SELECT  * FROM tbl_messages where type = 'sent' and clientnum = '"+passval+"' order by _id DESC";
                break;
            case 2:
                query = "SELECT  * FROM tbl_messages where type = 'alert' and clientnum = '"+passval+"' order by _id DESC";
                break;
        }
        // TODO Auto-generated method stub
        lca = new LoadContactsAyscn();
        lca.execute();
        //smsListAdapter.updateAdapter(c);
        //listViewSMS.setAdapter(smsListAdapter);
        return false;
    }
    public class TitleNavigationAdapter extends BaseAdapter {

        private ImageView imgIcon;
        private TextView txtTitle;
        private ArrayList<SpinnerNav> spinnerNavItem;
        private Context context;

        public TitleNavigationAdapter(Context context,ArrayList<SpinnerNav> spinnerNavItem) {
            this.spinnerNavItem = spinnerNavItem;
            this.context = context;
        }

        @Override
        public int getCount() {
            return spinnerNavItem.size();
        }

        @Override
        public Object getItem(int index) {
            return spinnerNavItem.get(index);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);
            }

            imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

            imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());
            imgIcon.setVisibility(View.GONE);
            txtTitle.setText(spinnerNavItem.get(position).getTitle());
            txtTitle.setTextColor(Color.WHITE);
            return convertView;
        }


        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater mInflater = (LayoutInflater)
                        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);
            }

            imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

            imgIcon.setImageResource(spinnerNavItem.get(position).getIcon());
            txtTitle.setText(spinnerNavItem.get(position).getTitle());
            txtTitle.setTextColor(Color.WHITE);
            return convertView;
        }

    }
    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {
        ProgressDialog pd;
        public String[] selected;
        public ArrayList<String> sms;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = ProgressDialog.show(AMessagesActivity.this, "Loading Messages","This might take a while, because the location is being retrieved. Please Wait");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            sms = new ArrayList<String>();
            cursor = db.rawQuery ( query, null );
            //smsListAdapter=new SMSListAdapter(AMessagesActivity.this,cursor);
            int i=0;
            while (cursor.moveToNext()) {
                String bod=cursor.getString(cursor.getColumnIndex("smscontent"));
                String tim=cursor.getString(cursor.getColumnIndex("smsdate"));
                String typ=cursor.getString(cursor.getColumnIndex("type"));
                String lat=cursor.getString(cursor.getColumnIndex("lat"));
                String lon=cursor.getString(cursor.getColumnIndex("lon"));
                String address=cursor.getString(cursor.getColumnIndex("address"));
                String locate=address;
                if(((!lat.equals("0.0")&&!lat.equals("null"))&&(!lon.equals("0.0")&&!lat.equals("null"))&&address.equals(""))){locate = getMyLocationAddress(Double.parseDouble(lat),Double.parseDouble(lon),getBaseContext(),Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))));}
                String smsBody="Content: "+bod+"\n\nTimeStamp: "+tim+"\nExact Location:"+locate+"Raw Location\nLat:"+lat+"\nLon:"+lon;
                sms.add(smsBody);
            }
            cursor.close();

            return sms;
        }

        @Override
        protected void onPostExecute(ArrayList<String> sms) {
            // TODO Auto-generated method stub
            super.onPostExecute(sms);
            pd.cancel();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.text, sms);
            adapter.notifyDataSetChanged();
            listViewSMS.setAdapter(adapter);
        }

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
            retStatement= "(Enable the internet then refresh messages to get the specific location.)";
            return retStatement;
        }
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

