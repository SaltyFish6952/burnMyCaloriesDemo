package com.example.salty_9a312.burnmycalories;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
    }

    public void addData(View view) {
        DatePicker datePicker = (DatePicker)findViewById(R.id.datepicker);
        EditText editText = (EditText)findViewById(R.id.editText);
        Toast.makeText(this,"year:" + datePicker.getYear() + 1 + " "
                + datePicker.getMonth()  + " " + datePicker.getDayOfMonth()  + " " + editText.getText().toString(),Toast.LENGTH_LONG).show();


    }
}
