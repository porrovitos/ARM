package com.example.armmrk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;



public class HomeActivity extends AppCompatActivity {
Button test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);
        test = findViewById(R.id.button_test);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Zapysk();
            }
        });

    }

    private void Zapysk() {
        startActivity(new Intent(HomeActivity.this, DocumentListActivity.class));
        finish();
    }



}