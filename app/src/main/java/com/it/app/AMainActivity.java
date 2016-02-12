package com.it.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Rusty on 7/25/14.
 */
public class AMainActivity extends Activity {
    private static final String TAG = "SMSTRACKER";
    private static final Uri STATUS_URI = Uri.parse("content://sms");
    ImageView btnmess,btnmap,btnset,btnlogout;
    EditText txtcode;
    Boolean bool;
    CircleButton cb,btn;
    private Dialog overlayInfo;
    private SMSSentObserver smsSentObserver = null;
    String holderSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bool = false;
        holderSwitch = "false";
        overlayInfo= new Dialog(AMainActivity.this);
        overlayInfo.requestWindowFeature(Window.FEATURE_NO_TITLE);
        overlayInfo.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        overlayInfo.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        overlayInfo.setContentView(R.layout.overlay_view);
        txtcode = (EditText)overlayInfo.findViewById(R.id.txtcode);
        cb = (CircleButton)overlayInfo.findViewById(R.id.btnoff);
        btn = (CircleButton)findViewById(R.id.btn);
        btnmess = (ImageView)findViewById(R.id.mess);
        btnset = (ImageView)findViewById(R.id.set);
        //Toast.makeText(this,DBAdapter.getMessages().toString(), Toast.LENGTH_SHORT).show();
        holderSwitch = DBAdapter.getValuesData("12");
        if (holderSwitch.equals("false")){
            bool = false;
        }else{
            holderSwitch = "true";
            bool = true;
        }
        showDialog();
        if(smsSentObserver == null){
            smsSentObserver = new SMSSentObserver(new Handler(), this);
            this.getContentResolver().registerContentObserver(STATUS_URI, true, smsSentObserver);
        }
        btnmess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ASortclientmess.class);
                startActivity(i);
            }
        });
        btnset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ASettingsActivity.class);
                startActivity(i);
            }
        });
        overlayInfo.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    return true;
                }
                return false;
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptuser();
            }
        });
        cb.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtcode.getText().toString().equals(DBAdapter.getValuesData("11"))){
                    overlayInfo.cancel();
                    holderSwitch = "false";
                    DBAdapter.updateValues("false",12);
                    bool=false;
                    Toast.makeText(AMainActivity.this, "Code Matched!", Toast.LENGTH_SHORT).show();
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AMainActivity.this);
                    alertDialogBuilder.setTitle("Code Mismatch!");
                    alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialogBuilder.setMessage("Sorry, but you entered a wrong access code. Try again.");
                    alertDialogBuilder.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            holderSwitch = "ON";
                            DBAdapter.updateValues("true",12);
                            bool = !bool;
                            dialog.cancel();
                            showDialog();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();}
                //Toast.makeText(AMainActivity.this, "Clicked!", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.submain, menu);
        mypref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        m=menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_onoff) {
            promptuser();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }*/
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onPause();
        finish();
    }
    private void showDialog(){
        txtcode.setText("");
        if (bool){overlayInfo.show();}
    }
    private void promptuser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AMainActivity.this);
        alertDialogBuilder.setTitle("Proceed?");
        alertDialogBuilder.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialogBuilder.setMessage("Before proceeding make sure you know your access code to stop tracking services(e.g. Messages Monitoring,Acquiring Location). By default, accesscode is set to '1234'. Go to Mobile Tracking Settings to change access code. Now, are you sure you want to proceed?");
        alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                holderSwitch = "ON";
                DBAdapter.updateValues("true",12);
                bool = !bool;
                dialog.cancel();
                showDialog();
            }
        });
        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
                //Toast.makeText(getApplicationContext(), "You chose a negative answer",Toast.LENGTH_LONG).show();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
