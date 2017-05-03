package com.example.a47668.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by 47668 on 2017/4/2.
 * Changed by Lei on 2017/5/2
 * This class contains the methods for send comments at post
 */

public class MessageActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener,
        GestureDetector.OnDoubleTapListener {
    private GestureDetectorCompat GD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle("Message");
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

    public void toMyDetailPost(View v) {
        Intent intentToMyDetailPost = new Intent(this, MyDetailPostActivity.class);
        this.startActivity(intentToMyDetailPost);
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
        if (e2.getX() - e1.getX() > 300) {
            Intent intentMain = new Intent(this, MainActivity.class);
            this.startActivity(intentMain);
            overridePendingTransition(R.anim.toright, 0);
        }
        return false;
    }
}
