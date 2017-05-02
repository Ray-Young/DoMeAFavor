package com.example.a47668.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;

public class HelpOutActivity extends AppCompatActivity {
    private class myTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            try {
                addComment(postTime,content,senderId,receiverId,postId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (long)0;
        }

        protected void onPostExecute(Long result) {

        }
    }
    private EditText message;
    private TextView txtTime;

    public dataSingleton dsl;
    private String postTime;
    private String content;
    private int senderId;
    private int receiverId;
    private int postId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpout);

        setTitle("Leave Message");

        dsl = new dataSingleton();
        senderId = dsl.getInstance().getMyUserId();
        Bundle bundle = getIntent().getExtras();
        receiverId = bundle.getInt("receiverID");
        postId = bundle.getInt("postID");
        message = (EditText)findViewById(R.id.editTextMsg);
        txtTime = (TextView)findViewById(R.id.textView16);

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        String yearString = Integer.toString(year);
        int month = c.get(Calendar.MONTH)+1;
        String monthString = Integer.toString(month);
        int day = c.get(Calendar.DAY_OF_MONTH);
        String dayString = Integer.toString(day);
        postTime = yearString + "-"+ monthString + "-" + dayString;
        txtTime.setText(postTime);
    }
    public void cancel(View v){
        this.finish();
    }

    public void upLoad(View v){
        content = message.getText().toString();
        myTask mytsk = new myTask();
        try {
            mytsk.execute(new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/addMessage"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Toast.makeText(getBaseContext(),"Message sent.",Toast.LENGTH_SHORT).show();
        this.finish();
    }

    private static void addComment(String date, String content, int senderID, int receiverID, int postID){
        try{
            SyncHttpClient client = new SyncHttpClient();
            RequestParams params = new RequestParams();
            System.out.println(date);
            params.put("date",date);
            params.put("content",content);
            params.put("senderID",senderID);
            params.put("receiverID",receiverID);
            params.put("postID",postID);
            client.post("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/addMessage", params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    System.out.println("Fail");
                    // error handling
                    }

                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    // success
                    System.out.println(responseString);
                    }
                });
            System.out.println("Unknown");
            }catch(Exception e){
            e.printStackTrace();
            }
    }

}
