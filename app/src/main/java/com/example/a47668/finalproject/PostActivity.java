package com.example.a47668.finalproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.*;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.Calendar;


public class PostActivity extends AppCompatActivity {
    private class myTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            try {
                addPost2(stringTheme, stringDetail, stringLocation, stringDate, intReward, intUserId, stringUserName, PhotoBitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (long)0;
        }

        protected void onPostExecute(Long result) {

        }
    }

    public dataSingleton dsl;
    public static Bitmap PhotoBitmap;
    private ImageView imgPhotoTaken;
    private Button takePhoto;
    private TextView postTime;
    private EditText postTheme;
    private EditText postDetail;
    private TextView postLocation;
    private EditText postReward;

    private String stringTheme;
    private String stringDetail;
    private String stringLocation;
    private String stringDate;
    private int intReward;
    private int intUserId;
    private String stringUserName;
    private String stringImgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        setTitle("Post A Task");

        dsl = new dataSingleton();
        postTheme = (EditText)findViewById(R.id.EditText9);
        postDetail = (EditText)findViewById(R.id.EditText);
        postLocation = (TextView)findViewById(R.id.textView3);
        postReward = (EditText)findViewById(R.id.editTextMoney);
        postTime = (TextView)findViewById(R.id.postTime);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String yearString = Integer.toString(year);
        int month = c.get(Calendar.MONTH)+1;
        String monthString = Integer.toString(month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dayString = Integer.toString(day);
        String time = yearString + "-"+ monthString + "-" + dayString;
        stringDate = time;

        postTime.setText(time);
        imgPhotoTaken = (ImageView)findViewById(R.id.imgView);
        takePhoto = (Button)findViewById(R.id.takePhoto);

        //take photo...
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {  //before proceeding with taking a photo, we should ensure we have a camera. :)
                    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);  //intending to use camera.
                    startActivityForResult(i, 999);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 999 && resultCode == RESULT_OK)
        {
            //The image is stored in a Bundle named data.  Retrieve and render it as follows.
            Bundle PhotoBundle = data.getExtras();
            PhotoBitmap = (Bitmap) PhotoBundle.get("data");
            imgPhotoTaken.setImageBitmap(PhotoBitmap);
        }

    }

    //upload the post...
    private static void addPost2(String title, String postContent, String location, String date, int reward, int userID, String name, Bitmap bm) throws Exception{
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("title", title);
        params.put("postContent",postContent);
        params.put("location",location);
        params.put("date",date);
        params.put("reward",reward);
        params.put("userID",userID);
        params.put("name",name);
        System.out.println(title);
        System.out.println(postContent);
        System.out.println(location);
        System.out.println(date);
        System.out.println(reward);
        System.out.println(userID);
        System.out.println(name);
        if(bm!=null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            params.put("image", encodedImage);
            //params.put("image", new File(imagePath));
            System.out.println("File found! Getiing to post!!!");
            }else{
            params.put("image","");
            }
        client.post("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/addPost", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                System.out.println("Fail");
                // error handling
                }
            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
               // success
                System.out.println("Success!");
                }
            });
        System.out.println("Unknown");
    }


    public void toMain(View v){
        this.finish();
    }
    public void toMyPost(View v){

        //add new post the server...
        stringTheme = postTheme.getText().toString();
        stringDetail = postDetail.getText().toString();
        stringLocation = "42.349876, -71.099486";
        try {  
            intReward = Integer.parseInt(postReward.getText().toString());
        }catch (NumberFormatException nfe){
            System.out.println("Could not parse reward");
        }
        intUserId = dsl.getInstance().getMyUserId();
        stringUserName = dsl.getInstance().getMyUserName();
        try {
            myTask mytsk = new myTask();
            mytsk.execute(new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/addPost"));

        }catch (Exception e) {
            e.printStackTrace();
        }

        //add new post to dataSingleton...
        /**
        dsl.getInstance().addPostId();
        dsl.getInstance().addTitle(stringTheme);
        dsl.getInstance().addPostContent(stringDetail);
        dsl.getInstance().addLocation(stringLocation);
        dsl.getInstance().addDate(stringDate);
        dsl.getInstance().addReward(intReward);
        dsl.getInstance().addUserId(intUserId);
        dsl.getInstance().addIsSolved();
        dsl.getInstance().addName(stringUserName);
         **/

        System.out.println(dsl.getInstance().getPostId().size());
        //intent...
        Toast toast = Toast.makeText(getApplicationContext(),"Post Success!!!",Toast.LENGTH_LONG);
        toast.show();
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intentMain = new Intent(this,MainActivity.class);
        this.startActivity(intentMain);
    }
}

