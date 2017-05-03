package com.example.a47668.finalproject;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Changed by Lei on 2017/05/02
 * This activity is the entrance for leave comment
 */
public class MessageDetailActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagedetail);

    }
    public void back(View v){
        this.finish();
    }
}
