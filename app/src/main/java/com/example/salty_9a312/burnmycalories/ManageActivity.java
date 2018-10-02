package com.example.salty_9a312.burnmycalories;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;

import java.util.Vector;

public class ManageActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 2333;
    private ListView listView;
    private Vector<String> listVector;
    private AlertDialog alertDialog;
    private String[] parts;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);


        listView = (ListView) findViewById(R.id.list);
        listVector = new Vector<String>();


        queryData();
        create_list();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String item = parent.getItemAtPosition(position).toString();
                parts = item.split(" ");


                initAlertDialog();
                if (alertDialog != null && !alertDialog.isShowing())
                    alertDialog.show();

            }
        });

    }

    public void naviToAdd(View view) {
        Intent intent = new Intent(ManageActivity.this, AddDateActivity.class);

        //startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case REQUEST_CODE:
                String date = data.getStringExtra("date");
                int calories = data.getIntExtra("calories", 0);


                Toast.makeText(this, date + " " + calories, Toast.LENGTH_LONG).show();

                //insert into array

                listVector.add("Time : " + date + " Calories : " + calories);
                create_list();

                insertData(date, calories);
                break;
        }

    }


    private void queryData() {

        db = MainActivity.getDB();

        Cursor cursor = db.query("Calories",
                null,
                null,
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {

                listVector.add("Time : " + cursor.getString(cursor.getColumnIndex("date"))
                        + " Calories : " + cursor.getString(cursor.getColumnIndex("calories")));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    private void insertData(String date, int calories) {

        db = MainActivity.getDB();

        ContentValues values = new ContentValues();
        values.put("calories", calories);
        values.put("date", date);

        db.insert("Calories", null, values);
        values.clear();


    }

    private void clearVector() {
        listVector.removeAllElements();
    }

    private void create_list() {

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ManageActivity.this,
                android.R.layout.simple_list_item_1, listVector);
        listView.setAdapter(adapter);

    }

    private void initAlertDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ManageActivity.this);
        builder.setTitle("Warning");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you really want to delete this record ?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.delete("Calories", "date = ?" + " and calories = ?", new String[]{parts[2], parts[5]});

                clearVector();
                queryData();
                create_list();
            }
        });

        builder.setNegativeButton("No", null);
        alertDialog = builder.create();

    }
}
