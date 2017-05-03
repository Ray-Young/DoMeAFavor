package com.example.a47668.finalproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 47668 on 2017/5/1.
 * This activity has the methods for Personal profile
 */

public class ProfileActivity extends AppCompatActivity {
    private TextView tv;
    private ImageView iv;
    private static String name;
    private static Bitmap bm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        Bundle extras = getIntent().getExtras();
        iv = (ImageView)findViewById(R.id.iv);
        tv = (TextView)findViewById(R.id.tv);
        bm = (Bitmap) getIntent().getParcelableExtra("image");
        name = extras.getString("name");
        System.out.println("Name second class: "+name);
        tv.setText(name);
        iv.setImageBitmap(bm);
    }

}
