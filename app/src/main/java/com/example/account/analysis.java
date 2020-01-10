package com.example.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class analysis extends AppCompatActivity {
//hello chart ref : https://github.com/DerrickPikachu/PieChartPrototype
//
    int no,page;
    TextView text3;
    PieChartView pieChart;

    SQLiteDatabase db;
    static final String db_name ="testDB";
    static  final  String tb_name="test";


    private float x1,x2;
    static final int MIN_DISTANCE = 150;
    ConstraintLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analysis);

        page =3;
        no =0;
        Intent it=getIntent();
        no = it.getIntExtra("page",0);

        currentLayout = findViewById(R.id.back);
        currentLayout.setBackgroundColor(Color.rgb(0,0,0));

        db  = openOrCreateDatabase(db_name, Context.MODE_PRIVATE,null);

        final String[] lunch = {"尚未選擇", "色情片", "避孕用品", "口服避用藥", "性病治療費"};
        final Integer[] classnumer= new Integer[100];
        final int[] pieColor ={Color.GRAY,Color.parseColor("#00ff00"),Color.parseColor("#bf00ff"),Color.parseColor("#00ffff"),Color.parseColor("#ff8000")};

        ArrayList<SliceValue> slices = new ArrayList<>();

        pieChart = findViewById(R.id.chart);
        for(int i=0;i<5;i++) {

            Cursor c2 = db.rawQuery("SELECT COUNT(*) FROM " + tb_name + " WHERE classname =" + i, null);
            if (c2.moveToFirst()) {
                classnumer[i] = Integer.valueOf(c2.getString(0));
            }
            slices.add(new SliceValue(classnumer[i], pieColor[i]).setLabel(lunch[i]+" "+classnumer[i]));

        }
        /*
        slices.add(new SliceValue(50, Color.BLUE).setLabel("KMT:50"));
        slices.add(new SliceValue(20, Color.GREEN).setLabel("DDP:20"));
        slices.add(new SliceValue(10, Color.RED).setLabel("Communist :10"));
        slices.add(new SliceValue(20, Color.YELLOW).setLabel("New:20"));
        */
        PieChartData chartData = new PieChartData(slices);
        chartData.setHasLabels(true).setValueLabelTextSize(14);
        chartData.setValueLabelBackgroundColor(Color.argb(255,0,0,0));
        chartData.setHasCenterCircle(true).setCenterText1("費用占比")
                .setCenterText1FontSize(20).setCenterText1Color(Color.parseColor("#f4e1d9"));
        pieChart.setPieChartData(chartData);

    }



    public void threetoone(View view) {
        Intent it = new Intent(this,MainActivity.class);
        it.putExtra("page",3);
        startActivityForResult(it,page);
    }

    public void threetotwo(View view) {
        Intent it = new Intent(this,record.class);
        it.putExtra("page",3);
        startActivityForResult(it,page);
    }

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

                        Log.i("right","right");
                        Intent it = new Intent(this,MainActivity.class);
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
                        Log.i("left","left");

                        Intent it = new Intent(this,record.class);
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
