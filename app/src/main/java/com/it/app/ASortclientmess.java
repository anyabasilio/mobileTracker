package com.it.app;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rusty on 9/9/14.
 */
public class ASortclientmess extends ListActivity {
    ListView list;
    String[] values;
    LoadContactsAyscn lca;
    @Override
    public void onCreate(Bundle savedInstanceState){
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);
        list = (ListView) findViewById(android.R.id.list);
        lca = new LoadContactsAyscn();
        lca.execute();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                String  itemValue    = (String) list.getItemAtPosition(position);
                finish();
                Intent i = new Intent(getBaseContext(),AMessagesActivity.class);
                i.putExtra("CLIENT",itemValue);
                startActivity(i);
            }

        });
    }
    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {
        ProgressDialog pd;
        public String[] selected;
        public ArrayList<String> mess;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = ProgressDialog.show(ASortclientmess.this, "Loading Messages","Please Wait");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            mess = new ArrayList<String>();
            SQLiteDatabase db= DBAdapter.open();
            Cursor c = db.rawQuery("Select distinct clientnum from tbl_messages",null);
            int i=0;
            while (c.moveToNext()) {
                String phNumber = c.getString(0);
                mess.add(phNumber);
            }
            c.close();
            return mess;
        }

        @Override
        protected void onPostExecute(ArrayList<String> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(mess);
            pd.cancel();

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    getApplicationContext(), R.layout.text, mess);

            list.setAdapter(adapter);
            //mAdapter = new SelectionAdapter(getApplicationContext(),R.layout.text, R.id.textview,contacts);
        }

    }
}
