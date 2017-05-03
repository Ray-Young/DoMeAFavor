package com.example.a47668.finalproject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Changed on 2017/05/02
 * This class has the methods for Show the personal Activity
 */
public class PersonalActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {

    private GestureDetectorCompat GD;
    private dataSingleton dsl = new dataSingleton();
    TextView userName;
    TextView userEmail;
    ImageView myPhoto;
    Bitmap fbBitMap = dsl.getInstance().getFbPhoto();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        setTitle("My Info");

        myPhoto = (ImageView) findViewById(R.id.imageView);
        userEmail = (TextView) findViewById(R.id.textViewEmail);
        userEmail.setText(dsl.getInstance().getFbEmail());
        userName = (TextView) findViewById(R.id.textView4);
        userName.setText(dsl.getInstance().getMyUserName().toString());
        myPhoto.setImageBitmap(fbBitMap);

        GD = new GestureDetectorCompat(this, this);   //Context, Listener as per Constructor Doc.
        GD.setOnDoubleTapListener(this);   //DoubleTaps implemented a bit differently, must be bound like this.

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.GD.onTouchEvent(event);               //insert this line to consume the touch event locally by our GD,
        return super.onTouchEvent(event);          //if we have a handler for the touch event we will handle before passing on.
    }

    public void toMain(View v) {
        this.finish();
    }

    public void toMyPost(View v) {
        Intent intentMyPost = new Intent(this, MyPostActivity.class);
        this.startActivity(intentMyPost);
    }

    public void toMySolution(View v) {
        Intent intentMySolution = new Intent(this, MySolutionActivity.class);
        this.startActivity(intentMySolution);
    }

    //gesture stuff...
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e2.getX() - e1.getX() < -300) {
            Intent intentMain = new Intent(this, MainActivity.class);
            this.startActivity(intentMain);
            overridePendingTransition(R.anim.tleft, 0);
        }

        return false;
    }
}
