package com.example.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    int page,no;
    String product_name;
    int classname,price;
    EditText product_name_edit,price_edit;

    Spinner spinner;
    final String[] item = {"尚未選擇", "色情片", "避孕用品", "口服避用藥", "性病治療費"};

    static final String db_name ="testDB";
    static  final  String tb_name="test";
    SQLiteDatabase db;

    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    ConstraintLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        page =1;
        product_name_edit  = findViewById(R.id.product_name_edit);
        price_edit = findViewById(R.id.price_edit);
        Intent it=getIntent();
        no = it.getIntExtra("page",0);

        currentLayout = findViewById(R.id.back);
        currentLayout.setBackgroundColor(Color.rgb(0,0,0));

        //sql 參考課本
        db  = openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);

        String CreateTable = "CREATE TABLE IF NOT EXISTS "+
                tb_name+
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "name VARCHAR(32)," +
                "price int(32),"+
                "classname int(64) )";
        db.execSQL(CreateTable);

        spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> lunchList = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                item);
        spinner.setAdapter(lunchList);

        //spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("spinner_item",item[position]);
                classname = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void submit(View view){

        product_name = product_name_edit.getText().toString();
        price = Integer.valueOf(price_edit.getText().toString() );
        Log.i("name", product_name);
        Log.i("price", price+"");
        Log.i("classname", item[classname]);

        addData(product_name,price,classname);
    }

    private  void  addData(String name ,int price,int  classname) //參考課本
    {
        ContentValues cv =  new ContentValues(3);
        cv.put("name",name);
        cv.put("price",price);
        cv.put("classname",classname);

        Log.d("name",name);

        db.insert(tb_name,null,cv);
    }



    @Override
    public boolean onTouchEvent(MotionEvent event)
    //ref from:https://stackoverflow.com/questions/6645537/how-to-detect-the-swipe-left-or-right-in-android?fbclid=IwAR2lQ5dvUe3XrpaEcyyJW9HmNfhcagynlLTsB-1OGf1t17aVr3XNIrgdSzU
    {
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE)
                {
                    if (x2 > x1)
                    {
                        //txv.setText("右移");
                        Random x = new Random();
                        int red = x.nextInt(256);
                        int green = x.nextInt(256);
                        int blue = x.nextInt(256);
                        currentLayout.setBackgroundColor(Color.rgb(red,green,blue));
                        Intent it = new Intent(this, record.class);
                        it.putExtra("page",1);
                        startActivityForResult(it,page);
                    }

                    else
                    {
                        //txv.setText("左移");
                        Random x = new Random();
                        int red = x.nextInt(256);
                        int green = x.nextInt(256);
                        int blue = x.nextInt(256);

                        Intent it = new Intent(this, analysis.class);
                        it.putExtra("page",1);
                        startActivityForResult(it,page);
                    }

                }
                else
                {
                    // consider as something else - a screen tap for example
                }
                break;
        }
        return super.onTouchEvent(event);
    }


}
