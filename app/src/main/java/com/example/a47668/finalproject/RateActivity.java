package com.example.a47668.finalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;

public class RateActivity extends AppCompatActivity {
    private RatingBar ratingBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);

        ratingBar1 = (RatingBar)findViewById(R.id.ratingBar1);
        ratingBar1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                Intent intentToMain = new Intent(RateActivity.this, MainActivity.class);

                RateActivity.this.startActivity(intentToMain);

                return false;
            }
        });



    }
}
