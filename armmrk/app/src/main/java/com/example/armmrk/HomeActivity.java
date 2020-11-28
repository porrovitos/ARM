package com.example.armmrk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class HomeActivity extends AppCompatActivity {
Button students,archive,newDoc,uploadDoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        students = findViewById(R.id.button4);
        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, StudentsListActivity.class));
                finish();
            }
        });

        newDoc= findViewById(R.id.button);
        newDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NewDocActivity.class));
                finish();
            }
        });

        uploadDoc= findViewById(R.id.button2);
        uploadDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, DocumentListActivity.class));
                finish();
            }
        });

        archive= findViewById(R.id.button3);
        archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, ArchiveActivity.class));
                finish();
            }
        });

    }




}