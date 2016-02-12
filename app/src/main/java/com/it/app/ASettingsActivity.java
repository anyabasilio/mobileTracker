package com.it.app;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Rusty on 8/9/14.
 */
public class ASettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    public Preference btnchoose,btnsimnum,btneditacc,btnchoose2;
    EditTextPreference etp1,etp2,etp3,s1,s2,s3,s4,s5,s6,s7,s8,s9,s10;
    CheckBoxPreference cnoti,csim,crec,crecun,crecsp,csen,csenun,csensp,serv1Offline,serv2Offline;
    ActionBar a;
    private Dialog dialogReceived,dialogSent;
    SharedPreferences sharedPrefs;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        DBAdapter.init(getBaseContext());
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_generalsettings);
        a = getActionBar();
        a.setDisplayHomeAsUpEnabled(true);
        cnoti = (CheckBoxPreference)findPreference("pref_key_noti");
        csim = (CheckBoxPreference)findPreference("pref_key_simalert");
        crec = (CheckBoxPreference)findPreference("pref_key_received");
        crecun = (CheckBoxPreference)findPreference("pref_key_recunknown");
        crecsp = (CheckBoxPreference)findPreference("pref_key_reccontacts");
        csen = (CheckBoxPreference)findPreference("pref_key_sent");
        csenun = (CheckBoxPreference)findPreference("pref_key_sentunknown");
        csensp = (CheckBoxPreference)findPreference("pref_key_sentcontacts");
        serv1Offline = (CheckBoxPreference)findPreference("isOffline");
        serv2Offline = (CheckBoxPreference)findPreference("isOffline2");
        etp1 = (EditTextPreference)findPreference("pref_key_servnum1");
        etp2 = (EditTextPreference)findPreference("pref_key_servnum2");
        etp3 = (EditTextPreference)findPreference("setcode");
        etp1.setText(DBAdapter.getServerNum("1"));
        etp2.setText(DBAdapter.getServerNum("2"));
        etp3.setText(DBAdapter.getValuesData("11"));

        s1 = (EditTextPreference)findPreference("sim1");
        s2 = (EditTextPreference)findPreference("sim2");
        s3 = (EditTextPreference)findPreference("sim3");
        s4 = (EditTextPreference)findPreference("sim4");
        s5 = (EditTextPreference)findPreference("sim5");
        s6 = (EditTextPreference)findPreference("sim6");
        s7 = (EditTextPreference)findPreference("sim7");
        s8 = (EditTextPreference)findPreference("sim8");
        s9 = (EditTextPreference)findPreference("sim9");
        s10 = (EditTextPreference)findPreference("sim10");

        s1.setSummary(DBAdapter.getSIMData("1"));
        s2.setSummary(DBAdapter.getSIMData("2"));
        s3.setSummary(DBAdapter.getSIMData("3"));
        s4.setSummary(DBAdapter.getSIMData("4"));
        s5.setSummary(DBAdapter.getSIMData("5"));
        s6.setSummary(DBAdapter.getSIMData("6"));
        s7.setSummary(DBAdapter.getSIMData("7"));
        s8.setSummary(DBAdapter.getSIMData("8"));
        s9.setSummary(DBAdapter.getSIMData("9"));
        s10.setSummary(DBAdapter.getSIMData("10"));
        btnchoose = (Preference)findPreference("button_contactsR");
        btnchoose.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getBaseContext(),AddSpecificCon.class);
                i.putExtra("CODE",1);
                startActivity(i);
                return true;
            }
        });
        btnchoose2 = (Preference)findPreference("button_contactsS");
        btnchoose2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent i = new Intent(getBaseContext(),AddSpecificCon.class);
                i.putExtra("CODE",0);
                startActivity(i);
                return true;
            }
        });
        cnoti.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),1);
                    cnoti.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        csim.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),2);
                    csim.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        crec.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),5);
                    crec.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        crecun.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),6);
                    crecun.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        crecsp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),7);
                    crecsp.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        csen.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),8);
                    csen.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        csenun.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),9);
                    csenun.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        csensp.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),10);
                    csensp.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        serv1Offline.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),3);
                    serv1Offline.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        serv2Offline.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=0) {
                    DBAdapter.updateValues(newValue.toString(),4);
                    serv2Offline.setChecked((Boolean)newValue);
                }rtnval = false;
                return rtnval;
            }});
        etp1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        etp2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        etp3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()==0) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ASettingsActivity.this);
                    builder.setTitle("Invalid Input");
                    builder.setMessage("Access code cannot be null!");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.show();
                    rtnval = false;
                }
                return rtnval;
            }});
        s1.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s4.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s5.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s6.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s7.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s8.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s9.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});
        s10.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                Boolean rtnval = true;
                if (newValue.toString().length()!=11) {
                    showErr();
                    rtnval = false;
                }
                return rtnval;
            }});

       sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!sharedPrefs.getString("pref_key_servnum1", "NULL").equals(null)){
            etp1.setSummary(sharedPrefs.getString("pref_key_servnum1", ""));
        }
        if (!sharedPrefs.getString("pref_key_servnum2", "NULL").equals(null)){
            etp2.setSummary(sharedPrefs.getString("pref_key_servnum2", ""));
        }
        if (!sharedPrefs.getString("setcode", "NULL").equals(null)){
            etp3.setText(sharedPrefs.getString("setcode", ""));
        }
        if (!sharedPrefs.getString("sim1", "NULL").equals(null)){
            s1.setSummary(sharedPrefs.getString("sim1", ""));
        }
        if (!sharedPrefs.getString("sim2", "NULL").equals(null)){
            s2.setSummary(sharedPrefs.getString("sim2", ""));
        }
        if (!sharedPrefs.getString("sim3", "NULL").equals(null)){
            s3.setSummary(sharedPrefs.getString("sim3", ""));
        }
        if (!sharedPrefs.getString("sim4", "NULL").equals(null)){
            s4.setSummary(sharedPrefs.getString("sim4", ""));
        }
        if (!sharedPrefs.getString("sim5", "NULL").equals(null)){
            s5.setSummary(sharedPrefs.getString("sim5", ""));
        }
        if (!sharedPrefs.getString("sim6", "NULL").equals(null)){
            s6.setSummary(sharedPrefs.getString("sim6", ""));
        }
        if (!sharedPrefs.getString("sim7", "NULL").equals(null)){
           s7.setSummary(sharedPrefs.getString("sim7", ""));
        }
        if (!sharedPrefs.getString("sim8", "NULL").equals(null)){
            s8.setSummary(sharedPrefs.getString("sim8", ""));
        }
        if (!sharedPrefs.getString("sim9", "NULL").equals(null)){
            s9.setSummary(sharedPrefs.getString("sim9", ""));
        }
        if (!sharedPrefs.getString("sim10", "NULL").equals(null)){
            s10.setSummary(sharedPrefs.getString("sim10", ""));
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //finish();
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

   @Override
  public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference pref = findPreference(key);
        int c =DBAdapter.getSimnumcount();
        if (pref instanceof ListPreference) {
            ListPreference listPref = (ListPreference) pref;
            pref.setSummary(listPref.getEntry());
            return;
        }
      if(key.equals("pref_key_servnum1")){
        if(sharedPrefs.getString("pref_key_servnum1", "").toString().length()==11){
            etp1.setSummary(sharedPrefs.getString("pref_key_servnum1", ""));
              DBAdapter.updateServnum(sharedPrefs.getString("pref_key_servnum1", ""), 1);
        }

      }else if(key.equals("pref_key_servnum2")){
        if(sharedPrefs.getString("pref_key_servnum2", "").length()==11){
              etp2.setSummary(sharedPrefs.getString("pref_key_servnum2", ""));
              DBAdapter.updateServnum(sharedPrefs.getString("pref_key_servnum2", ""),2);
        }
      }else if(key.equals("setcode")){
              etp3.setText(sharedPrefs.getString("setcode", ""));
              DBAdapter.updateValues(sharedPrefs.getString("setcode", ""),11);
      }
      if(key.equals("sim1")){
           DBAdapter.updateSimnumData(sharedPrefs.getString("sim1", ""),1);
           s1.setSummary(sharedPrefs.getString("sim1", ""));
      }else if(key.equals("sim2")){
           DBAdapter.updateSimnumData(sharedPrefs.getString("sim2", ""), 2);
           s2.setSummary(sharedPrefs.getString("sim2", ""));
      }else if(key.equals("sim3")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim3", ""),3);
          s3.setSummary(sharedPrefs.getString("sim3", ""));
      }else if(key.equals("sim4")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim4", ""),4);
          s4.setSummary(sharedPrefs.getString("sim4", ""));
      }else if(key.equals("sim5")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim5", ""),5);
          s5.setSummary(sharedPrefs.getString("sim5", ""));
      }else if(key.equals("sim6")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim6", ""),6);
          s6.setSummary(sharedPrefs.getString("sim6", ""));
      }else if(key.equals("sim7")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim7", ""),7);
          s7.setSummary(sharedPrefs.getString("sim7", ""));
      }else if(key.equals("sim8")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim8", ""),8);
          s8.setSummary(sharedPrefs.getString("sim8", ""));
      }else if(key.equals("sim9")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim9", ""),9);
          s9.setSummary(sharedPrefs.getString("sim9", ""));
      }else if(key.equals("sim10")){
          DBAdapter.updateSimnumData(sharedPrefs.getString("sim10", ""),10);
          s10.setSummary(sharedPrefs.getString("sim10", ""));
      }

  }
    public void showErr(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ASettingsActivity.this);
        builder.setTitle("Invalid Input");
        builder.setMessage("Sorry, please enter a valid 11 digit mobile number.");
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }
}

