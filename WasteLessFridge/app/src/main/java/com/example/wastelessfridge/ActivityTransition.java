package com.example.wastelessfridge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityTransition extends AppCompatActivity {
    String id, title, author, pages;

    bddSQL myDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);
        myDB = new bddSQL(ActivityTransition.this);
        id = getIntent().getStringExtra("id");
        System.out.println(id);
        myDB.deleteOneRow(String.valueOf(id));
        Intent intent=new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}