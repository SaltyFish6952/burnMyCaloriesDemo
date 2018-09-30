package com.example.salty_9a312.burnmycalories;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog dp;
    private TextView Date;
    private long beginTime;
    private long endTime;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        Date = (TextView) findViewById(whichDate);
        Date.setText(date);

    }

}
