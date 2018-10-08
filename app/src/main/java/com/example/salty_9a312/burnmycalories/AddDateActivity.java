package com.example.salty_9a312.burnmycalories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class AddDateActivity extends AppCompatActivity {

    String date;
    int calories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_date);
    }

    public void addData(View view) {
        DatePicker datePicker = (DatePicker) findViewById(R.id.datepicker);
        EditText editText = (EditText) findViewById(R.id.editText);

        if (editText.getText().toString().equals("")) {
            Toast.makeText(this, "Please input your calorie !", Toast.LENGTH_LONG).show();
            return;
        }


        date = datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth();
        calories = Integer.parseInt(editText.getText().toString());


        Toast.makeText(this, "Save Success!", Toast.LENGTH_LONG).show();

        this.onBackPressed();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if ((date == null || date.length() == 0) && calories == 0){
            setResult(RESULT_CANCELED);
            finish();
        }


        Intent intent = new Intent();
        intent.putExtra("date", date);
        intent.putExtra("calories", calories);
        setResult(RESULT_OK, intent);
        finish();
    }
}
