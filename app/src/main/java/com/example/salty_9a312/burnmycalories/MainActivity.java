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
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog dp;
    private TextView dateView;
    private long beginTime;
    private long endTime;
    private static final int INIT_VERSION = 1;
    private static DB_Helper dbHelper;
    private DataPoint[] dataPoints;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DB_Helper(this,INIT_VERSION);
        dbHelper.getWritableDatabase();


        GraphView graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[]{
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });

        graph.addSeries(series);

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


        SQLiteDatabase db = getDB();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        try {
            beginDate = format.parse(beginView.getText().toString());
            endDate = format.parse(endView.getText().toString());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Cursor cursor = db.query("Calories",
                null,
                null,
                null, null, null, null);


        if (cursor.moveToFirst()) {
            do {

                try {
                    tempDate = format.parse(cursor.getString(cursor.getColumnIndex("date")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(tempDate.getTime() >= beginDate.getTime() && tempDate.getTime() <= endDate.getTime())
                {
                    //insert into graph
                }


            } while (cursor.moveToNext());
        }

        cursor.close();


    }
}
