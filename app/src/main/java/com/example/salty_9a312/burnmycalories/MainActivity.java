package com.example.salty_9a312.burnmycalories;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog dp;
    private TextView dateView;
    private long beginTime;
    private long endTime;
    private static final int INIT_VERSION = 1;
    private static DB_Helper dbHelper;

    LineGraphSeries<DataPoint> series;
    GraphView graph;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DB_Helper(this, INIT_VERSION);
        dbHelper.getWritableDatabase();

        initGraph();
    }

    public void naviToManage(View view) {
        Intent intent = new Intent(MainActivity.this, ManageActivity.class);
        startActivity(intent);

        //need to send the data to the next intent.
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void selectDate(View view) {

        Calendar currentDate = Calendar.getInstance();
        final Calendar c = Calendar.getInstance();
        final int id = view.getId();

        dp = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view,
                                  int year,
                                  int month,
                                  int dayOfMonth) {

                c.set(year, month, dayOfMonth);

                if (R.id.date_begin == id) {
                    beginTime = c.getTimeInMillis();
                } else {
                    endTime = c.getTimeInMillis();
                }

                if (endTime != 0 && endTime < beginTime) {
                    Toast.makeText(MainActivity.this, "error! " + beginTime + " and " + endTime, Toast.LENGTH_LONG).show();
                    return;
                }


                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


                //Toast.makeText(MainActivity.this, "select date is " + format.format(c.getTime()), Toast.LENGTH_LONG).show();

                setDate(format.format(c.getTime()), id);

            }
        }, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
        dp.show();


    }


    private void setDate(String date, int whichDate) {

        dateView = (TextView) findViewById(whichDate);
        dateView.setText(date);

    }

    public static SQLiteDatabase getDB() {
        return dbHelper.getWritableDatabase();
    }

    public void queryFromSQL(View view) {

        TextView beginView = findViewById(R.id.date_begin);
        TextView endView = findViewById(R.id.date_end);
        Date beginDate = Calendar.getInstance().getTime();
        Date endDate = Calendar.getInstance().getTime();
        Date tempDate = Calendar.getInstance().getTime();
        Map<Date, Integer> dateMap = new TreeMap<>();
        int maxValue = 0;
        int minValue = 0;
        Date maxDate = Calendar.getInstance().getTime();
        Date minDate = Calendar.getInstance().getTime();


        SQLiteDatabase db = getDB();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            beginDate = format.parse(beginView.getText().toString());
            endDate = format.parse(endView.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //get from SQLite
        Cursor cursor = db.query("Calories",
                null,
                null,
                null, null, null, null);

        cursor.moveToFirst();

        minValue = Integer.parseInt(cursor.getString(cursor.getColumnIndex("calories")));


        //select from the area

        if (cursor.moveToFirst()) {
            do {

                try {
                    tempDate = format.parse(cursor.getString(cursor.getColumnIndex("date")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


                if (tempDate.getTime() >= beginDate.getTime() && tempDate.getTime() <= endDate.getTime()) {
                    //insert into graph
                    int value = Integer.parseInt(cursor.getString(cursor.getColumnIndex("calories")));

                    if (value >= maxValue)
                        maxValue = value;
                    if (value <= minValue)
                        minValue = value;

                    dateMap.put(tempDate, value);

                }


            } while (cursor.moveToNext());
        }

        cursor.close();

        graph.removeAllSeries();

        series = new LineGraphSeries<>();

        Iterator it = dateMap.entrySet().iterator();
        Date date = Calendar.getInstance().getTime();

        ArrayList<DataPoint> dataPointArrayList = new ArrayList<>();


        boolean isFirst = true;


        //move dataMap into Arraylist

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();


            date = (Date) entry.getKey();
            int calories = Integer.parseInt(entry.getValue().toString());

            if (isFirst) {
                minDate = date;
                isFirst = false;
            }

            dataPointArrayList.add(new DataPoint(date, calories));

            if (!it.hasNext())
                maxDate = date;

        }


        //transfer to DataPointArray

        removeGraph();

        addLineGraph(dataPointArrayList, beginDate.getTime(), endDate.getTime());

    }


    private void initGraph() {

        graph = findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);


    }

    public void removeGraph() {
        graph.removeAllSeries();
    }

    public void addLineGraph(ArrayList<DataPoint> dataPointArrayList, long minDate, long maxDate) {

        DataPoint[] dpArray = (DataPoint[]) dataPointArrayList.toArray(new DataPoint[dataPointArrayList.size()]);
        series = new LineGraphSeries<>(dpArray);

        graph.addSeries(series);
        SimpleDateFormat graphFormat = new SimpleDateFormat("yy/MM/dd");

        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this));

        graph.getGridLabelRenderer().setNumHorizontalLabels(dataPointArrayList.size());


        graph.getViewport().setMinX(minDate);
        graph.getViewport().setMaxX(maxDate);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getGridLabelRenderer().setHumanRounding(false);


    }


}
