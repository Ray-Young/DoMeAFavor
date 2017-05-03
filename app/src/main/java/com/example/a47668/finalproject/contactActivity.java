package com.example.a47668.finalproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by 47668 on 2017/5/1.
 * This class contains method for payment related methods
 */

public class contactActivity extends AppCompatActivity {

    private Button btn40;
    private ImageView iv8;
    private TextView tv2;
    private TextView tv3;

    private static int senderID;
    private static String senderEmail;
    private static String senderName;
    private static Bitmap senderImage;
    private static int messageID;
    private static int postID;

    @Override
    // Oncreate method for the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        setTitle("Contact Info");

        btn40 = (Button) findViewById(R.id.btn40);
        iv8 = (ImageView) findViewById(R.id.iv8);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);

        Bundle bundle = getIntent().getExtras();
        senderID = bundle.getInt("id");
        postID = bundle.getInt("postID");
        messageID = bundle.getInt("messageID");
        try {
            getUser2(senderID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        iv8.setImageBitmap(senderImage);
        tv2.setText(senderName);
        tv3.setText(senderEmail);

        offer(messageID);

    }

    public void toPayU(View v) {
        Intent intentToPayU = new Intent(this, payU1Activity.class);
        intentToPayU.putExtra("email", senderEmail);
        this.startActivity(intentToPayU);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelOffer(postID);
    }

    // Handle the offer, send restful web request to get the offer list
    private static void offer(int messageID) {
        try {
            URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/offer?messageID=" + messageID);
            System.out.println(url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(convertInputStreamToString(in));
            urlConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Restful webService, send cancel offer option
    private static void cancelOffer(int postID) {
        try {
            URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/cancel?postID=" + postID);
            System.out.println(url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            System.out.println(convertInputStreamToString(in));
            urlConnection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
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
            System.out.println("User ID is: " + userID);
            senderName = dataObj.getString("name");
            senderEmail = dataObj.getString("email");
            String base64String = dataObj.getString("fbPhoto");
            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
            senderImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            System.out.println("name is: " + senderName);
            System.out.println("Email is: '" + senderEmail);


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


}
