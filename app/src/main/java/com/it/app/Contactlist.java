package com.it.app;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class Contactlist extends ListActivity implements SearchView.OnQueryTextListener {
    ListView list;
    LinearLayout ll;
    //SelectionAdapter mAdapter;
    LoadContactsAyscn lca;
    private SearchView mSearchView;
    int x,y;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactlist);
        x =getIntent().getExtras().getInt("CODE");
        y =getIntent().getExtras().getInt("ID");
        ll = (LinearLayout) findViewById(R.id.LinearLayout1);
        list = (ListView) findViewById(android.R.id.list);
        // TODO Auto-generated method stub
        lca = new LoadContactsAyscn();
        lca.execute();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) (list.getItemAtPosition(position));
                String items[];
                items = item.trim().split(":");
                Toast.makeText(Contactlist.this,  items[0]+":"+items[1] , Toast.LENGTH_SHORT).show();
                if(x==0){
                    DBAdapter.updateSC(items[0],items[1],y);
                }else{
                    DBAdapter.updateRC(items[0],items[1],y);
                }
                finish();
            }
        });
        //setListAdapter(mAdapter);
        /*getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        getListView().setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            private int nr = 0;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                mAdapter.clearSelection();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub

                nr = 0;
                MenuInflater inflater = getMenuInflater();
                inflater.inflate(R.menu.contextual_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                StringBuilder checkedcontacts= new StringBuilder();
                switch (item.getItemId()) {

                    case R.id.action_select:
                        nr = 0;
                        int j=0;
                        for(int i = 0; i < lca.name1.size(); i++){
                        if(mAdapter.isPositionChecked(i)){
                            j++;
                            checkedcontacts.append(lca.name1.get(i).toString());
                            checkedcontacts.append(":");
                            checkedcontacts.append(lca.phno1.get(i).toString());
                            checkedcontacts.append(",");
                            if (x==0){
                                    DBAdapter.updateSC(lca.name1.get(i).toString(), lca.phno1.get(i).toString(),j);
                            }else if (x==1){
                                    DBAdapter.updateRC(lca.name1.get(i).toString(), lca.phno1.get(i).toString(),j);
                            }
                        }
                        }

                        Toast.makeText(Contactlist.this,  checkedcontacts , Toast.LENGTH_SHORT).show();
                        mAdapter.clearSelection();
                        mode.finish();
                        finish();
                }
                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,long id, boolean checked) {
                // TODO Auto-generated method stub
              if (mAdapter.getCurrentCheckedPosition().size()>-1 && mAdapter.getCurrentCheckedPosition().size()<10){
                if (checked) {
                    nr++;
                    mAdapter.setNewSelection(position, checked);
                } else {
                    nr--;
                    mAdapter.removeSelection(position);
                }
                mode.setTitle(nr + "/10 selected");
              }else{
                  Toast.makeText(Contactlist.this,  mAdapter.getCurrentCheckedPosition().size()+"You are only allowed to select 10."  , Toast.LENGTH_SHORT).show();
              }
            }
        });

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                // TODO Auto-generated method stub

                getListView().setItemChecked(position, !mAdapter.isPositionChecked(position));
                return false;
            }
        });*/

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnucontact, menu);
        MenuItem searchItem = menu.findItem(R.id.action_searchcon);
        mSearchView = (SearchView) searchItem.getActionView();
        list = getListView();
        list.setTextFilterEnabled(true);
        setupSearchView(mSearchView);
        return true;
    }

    private void setupSearchView(SearchView mSearchView) {

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setSubmitButtonEnabled(false);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint("Search Contacts");
    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            list.clearTextFilter();
        } else {
            list.setFilterText(newText);
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

    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<String>> {
        ProgressDialog pd;
        public String[] selected;
        public List<String> name1 = new ArrayList<String>();
        public List<String> phno1 = new ArrayList<String>();
        public ArrayList<String> contacts;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = ProgressDialog.show(Contactlist.this, "Loading Contacts","Please Wait");
        }

        @Override
        protected ArrayList<String> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            contacts = new ArrayList<String>();
            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            int i=0;
            while (c.moveToNext()) {

                String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                if (contactName.equals(phNumber)){contactName="no_display_name";}
                contacts.add(contactName + "\n:" + phNumber);

                name1.add(contactName);
                phno1.add(phNumber);
            }
            c.close();

            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<String> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);
            pd.cancel();

           ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                   getApplicationContext(), R.layout.text, contacts);

            list.setAdapter(adapter);
            //mAdapter = new SelectionAdapter(getApplicationContext(),R.layout.text, R.id.textview,contacts);
        }

    }
   /* class SelectionAdapter extends ArrayAdapter<String> {
        Context context;
        private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();

        public SelectionAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
            super(context, resource, textViewResourceId, objects);
            this.context = context;
        }

        public void setNewSelection(int position, boolean value) {
            mSelection.put(position, value);
            notifyDataSetChanged();
        }

        public boolean isPositionChecked(int position) {
            Boolean result = mSelection.get(position);
            return result == null ? false : result;
        }

        public Set<Integer> getCurrentCheckedPosition() {
            return mSelection.keySet();
        }

        public void removeSelection(int position) {
            mSelection.remove(position);
            notifyDataSetChanged();
        }

        public void clearSelection() {
            mSelection = new HashMap<Integer, Boolean>();
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v =super.getView(position, convertView, parent);//let the adapter handle setting up the row views
            v.setBackgroundColor(getResources().getColor(android.R.color.background_light)); //default color
            if (mSelection.get(position) != null) {
                v.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
            }

            return v;
        }
    }*/
}