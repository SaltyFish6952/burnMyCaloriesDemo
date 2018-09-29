package com.example.salty_9a312.burnmycalories;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jjoe64.graphview.GraphView;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);



    }

    public void naviToAdd(View view) {
        Intent intent = new Intent(ManageActivity.this,AddDateActivity.class);
        startActivity(intent);


    }
}
