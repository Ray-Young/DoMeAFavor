package com.example.a47668.finalproject;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.a47668.finalproject.R.id.imageButton;

/**
 * Changed on May 2nd, 2017 by Lei Yang
 * This method contains the detailed post methods
 */
public class DetailPostActivity extends AppCompatActivity {

    private dataSingleton dsl = new dataSingleton();
    public static String posterName;
    public static String postTheme;
    public static String postDetail;
    public static Bitmap imgBitmap;
    public static Bitmap profileBitmap;
    public static String postLocation;
    public static String postDate;
    public static int postReward;
    public static int postUserId;
    public static int postIdGetInt;
    public static boolean postIsSolved;

    private TextView txtPosterName;
    private TextView txtPostTheme;
    private TextView txtPostDetail;
    private TextView txtPostLocation;
    private TextView txtPostDate;
    private ImageView imgPost;
    private ImageButton imgProfile;
    private TextView txtReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpost);
        setTitle("Post Detail");

        //get the index of the post...
        String postIdGet = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            postIdGet = extras.getString("PostId");
        }
        System.out.println("postID------:" + postIdGet);
        postIdGetInt = Integer.parseInt(postIdGet);

        try {
            getPostDetail(postIdGetInt);
            getUser2(postUserId);
        } catch (Exception e) {
            e.printStackTrace();
        }


        txtPosterName = (TextView) findViewById(R.id.textView11);
        txtPostTheme = (TextView) findViewById(R.id.textView12);
        txtPostDetail = (TextView) findViewById(R.id.textView13);
        txtPostLocation = (TextView) findViewById(R.id.textView14);
        txtPostDate = (TextView) findViewById(R.id.textView15);
        imgPost = (ImageView) findViewById(R.id.imageView2);
        imgProfile = (ImageButton) findViewById(R.id.imageButton);
        txtReward = (TextView) findViewById(R.id.textViewReward);
        //set the textView...
        try {

        } catch (Exception e) {
        }

        txtPosterName.setText(posterName);
        txtPostTheme.setText(postTheme);
        txtPostDetail.setText(postDetail);
        txtPostLocation.setText(postLocation);
        txtPostDate.setText(postDate);
        txtReward.setText("$" + postReward);
        imgPost.setImageBitmap(imgBitmap);
        imgProfile.setImageBitmap(profileBitmap);

        if (imgBitmap != null) {
            imgBitmap = null;
        }
    }

    //get the image of this specific post...
    private static void getPostDetail(int postID) throws Exception {
        URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/getDetail?postID=" + postID);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String tmp = convertInputStreamToString(in);
            JSONObject dataObj = new JSONObject(tmp);
            String base64String = dataObj.getString("attachments");
            if (!base64String.equals("no image")) {
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imgBitmap = decodedByte;
            }
            postTheme = dataObj.getString("title");
            postDetail = dataObj.getString("postContent");
            postLocation = dataObj.getString("location");
            postDate = dataObj.getString("date");
            postReward = dataObj.getInt("reward");
            postUserId = dataObj.getInt("userID");
            postIsSolved = dataObj.getBoolean("isSolved");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }

    // Restful methond, get user from server
    private static void getUser2(int userID) throws Exception {
        URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/user/getUser2" + "?userID=" + userID);
        System.out.println(url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String tmp = convertInputStreamToString(in);
            JSONObject dataObj = new JSONObject(tmp);
            posterName = dataObj.getString("name");
            String base64String = dataObj.getString("fbPhoto");
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            profileBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } finally {
            urlConnection.disconnect();
        }
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }


    public void toMain(View v) {
        Intent intentToMain = new Intent(this, MainActivity.class);
        this.startActivity(intentToMain);
    }

    public void leave(View v) {
        this.finish();
    }

    public void helpOut(View v) {
        Intent intentHelpOut = new Intent(this, HelpOutActivity.class);
        intentHelpOut.putExtra("receiverID", postUserId);
        intentHelpOut.putExtra("postID", postIdGetInt);
        this.startActivity(intentHelpOut);
    }

    public void seeMore(View v) {
        Intent intentSeeMore = new Intent(this, SeeMoreActivity.class);
        this.startActivity(intentSeeMore);
    }

    public void seeProfile(View v) {
        Intent intentProfile = new Intent(this, ProfileActivity.class);
        intentProfile.putExtra("image", profileBitmap);
        intentProfile.putExtra("name", posterName);
        System.out.println(posterName + " name here++++++");
        this.startActivity(intentProfile);
    }
}
