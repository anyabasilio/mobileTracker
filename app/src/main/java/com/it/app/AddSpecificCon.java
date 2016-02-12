package com.it.app;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Rusty on 9/13/14.
 */
public class AddSpecificCon extends Activity{
    CircleButton c1,c2,c3,c4,c5,c6,c7,c8,c9,c10;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10;
    int _id,code;//sent=0,received=1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBAdapter.init(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addspecificcon);
        code = getIntent().getExtras().getInt("CODE");
        c1 = (CircleButton)findViewById(R.id.btnselect1);
        c2 = (CircleButton)findViewById(R.id.btnselect2);
        c3 = (CircleButton)findViewById(R.id.btnselect3);
        c4 = (CircleButton)findViewById(R.id.btnselect4);
        c5 = (CircleButton)findViewById(R.id.btnselect5);
        c6 = (CircleButton)findViewById(R.id.btnselect6);
        c7 = (CircleButton)findViewById(R.id.btnselect7);
        c8 = (CircleButton)findViewById(R.id.btnselect8);
        c9 = (CircleButton)findViewById(R.id.btnselect9);
        c10 = (CircleButton)findViewById(R.id.btnselect10);

        t1 = (TextView)findViewById(R.id.txtshow1);
        t2 = (TextView)findViewById(R.id.txtshow2);
        t3 = (TextView)findViewById(R.id.txtshow3);
        t4 = (TextView)findViewById(R.id.txtshow4);
        t5 = (TextView)findViewById(R.id.txtshow5);
        t6 = (TextView)findViewById(R.id.txtshow6);
        t7 = (TextView)findViewById(R.id.txtshow7);
        t8 = (TextView)findViewById(R.id.txtshow8);
        t9 = (TextView)findViewById(R.id.txtshow9);
        t10 = (TextView)findViewById(R.id.txtshow10);

        if(code==0){
            if(!DBAdapter.getSC("1").equals("empty: empty")){t1.setText(DBAdapter.getSC("1"));}
            if(!DBAdapter.getSC("2").equals("empty: empty")){t2.setText(DBAdapter.getSC("2"));}
            if(!DBAdapter.getSC("3").equals("empty: empty")){t3.setText(DBAdapter.getSC("3"));}
            if(!DBAdapter.getSC("4").equals("empty: empty")){t4.setText(DBAdapter.getSC("4"));}
            if(!DBAdapter.getSC("5").equals("empty: empty")){t5.setText(DBAdapter.getSC("5"));}
            if(!DBAdapter.getSC("6").equals("empty: empty")){t6.setText(DBAdapter.getSC("6"));}
            if(!DBAdapter.getSC("7").equals("empty: empty")){t7.setText(DBAdapter.getSC("7"));}
            if(!DBAdapter.getSC("8").equals("empty: empty")){t8.setText(DBAdapter.getSC("8"));}
            if(!DBAdapter.getSC("9").equals("empty: empty")){t9.setText(DBAdapter.getSC("9"));}
            if(!DBAdapter.getSC("10").equals("empty: empty")){t10.setText(DBAdapter.getSC("10"));}
        }else{
            if(!DBAdapter.getRC("1").equals("empty: empty")){t1.setText(DBAdapter.getRC("1"));}
            if(!DBAdapter.getRC("2").equals("empty: empty")){t2.setText(DBAdapter.getRC("2"));}
            if(!DBAdapter.getRC("3").equals("empty: empty")){t3.setText(DBAdapter.getRC("3"));}
            if(!DBAdapter.getRC("4").equals("empty: empty")){t4.setText(DBAdapter.getRC("4"));}
            if(!DBAdapter.getRC("5").equals("empty: empty")){t5.setText(DBAdapter.getRC("5"));}
            if(!DBAdapter.getRC("6").equals("empty: empty")){t6.setText(DBAdapter.getRC("6"));}
            if(!DBAdapter.getRC("7").equals("empty: empty")){t7.setText(DBAdapter.getRC("7"));}
            if(!DBAdapter.getRC("8").equals("empty: empty")){t8.setText(DBAdapter.getRC("8"));}
            if(!DBAdapter.getRC("9").equals("empty: empty")){t9.setText(DBAdapter.getRC("9"));}
            if(!DBAdapter.getRC("10").equals("empty: empty")){t10.setText(DBAdapter.getRC("10"));}
        }
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 1;
                choose();
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 2;
                choose();
            }
        });
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 3;
                choose();
            }
        });
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 4;
                choose();
            }
        });
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 5;
                choose();
            }
        });
        c6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 6;
                choose();
            }
        });
        c7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 7;
                choose();
            }
        });
        c8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 8;
                choose();
            }
        });
        c9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 9;
                choose();
            }
        });
        c10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _id = 10;
                choose();
            }
        });
    }
    public void choose(){
        Intent i = new Intent(getBaseContext(),Contactlist.class);
        i.putExtra("ID",_id);
        i.putExtra("CODE", code);
        startActivity(i);
    }
}
