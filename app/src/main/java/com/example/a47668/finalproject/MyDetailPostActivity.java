package com.example.a47668.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.*;

import com.loopj.android.http.Base64;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class MyDetailPostActivity extends AppCompatActivity {
    private
    ListView lvReply;
    ListAdapter lvAdapter;
    private TextView txtTheme;
    private TextView txtDetail;
    private ImageView imaBitmap;
    private TextView txtIsSolved;
    private Button btnAccept;
    Activity act;
    private dataSingleton dsl = new dataSingleton();
    public static String postTheme;
    public static String postDetail;
    public static Bitmap postBitmap;
    public static boolean postIsSolved;
    private int postId;
    public static ArrayList<Object> senderIdList = new ArrayList<>();
    public static ArrayList<Object> messageList = new ArrayList<>();
    public static ArrayList<Object> dateList = new ArrayList<>();
    public static ArrayList<Object> senderNameList = new ArrayList<>();
    public static ArrayList<Object> messageIdList = new ArrayList<>();

    /**
    private class myTask1 extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            URL url = urls[0];
            try {
                getComment(postId,url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (long)0;
        }
        protected void onPostExecute(Long result) {
        }
    }

     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mydetailpost);
        setTitle("Post Detail");
        act=this;
        txtTheme = (TextView)findViewById(R.id.textView25);
        txtDetail = (TextView)findViewById(R.id.textView24);
        txtIsSolved = (TextView)findViewById(R.id.textView26);
        imaBitmap = (ImageView)findViewById(R.id.imageView3);
        postId = 1;
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            postId = Integer.parseInt(extras.getString("realPostId"));
            System.out.println("This postID:======="+postId);
        }

        /**myTask1 mytsk = new myTask1();
        try {
            mytsk.execute(new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/getMessage?postID="+postId));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
         **/
        try {
            System.out.println("The Post ID we get is: "+postId);
            getComment(postId,new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/getMessage?postID="+postId));
            getPostDetail(postId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //while(mytsk.getStatus() != AsyncTask.Status.FINISHED){}
        System.out.println(senderIdList.size());
        for(int i=0;i<senderIdList.size();i++){
            try {
                getUser2(Integer.parseInt(senderIdList.get(i).toString()));
                //get personal information of the user
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        txtTheme.setText(postTheme);
        txtDetail.setText(postDetail);
        if(postIsSolved == true){
            txtIsSolved.setText("Solved");
        }else {
            txtIsSolved.setText("Unsolved");
        }
        try {
            imaBitmap.setImageBitmap(postBitmap);
        }catch (NullPointerException e){}
        lvReply = (ListView) findViewById(R.id.lvReply);
        lvAdapter = new MyDetailPostActivity.MyCustomAdapter(this.getBaseContext());  //instead of passing the boring default string adapter, let's pass our own, see class MyCustomAdapter below!
        lvReply.setAdapter(lvAdapter);
    }

    private static void getComment(int postID, URL url){

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String tmp = convertInputStreamToString(in);
            JSONArray jsonArray = new JSONArray(tmp);
            senderIdList.clear();
            messageList.clear();
            dateList.clear();
            senderNameList.clear();
            messageIdList.clear();
            if(jsonArray.length() == 0){
                System.out.println("No comment for this post currently!!!");
                }else {

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int aa = jsonObject.getInt("senderID");
                    System.out.println("content is: " + jsonObject.getString("content"));
                    System.out.println("senderID is: " + aa);
                    System.out.println("receiverID is: " + jsonObject.getInt("receiverID"));
                    System.out.println("postID is: " + jsonObject.getInt("postID"));
                    System.out.println("date is: "+jsonObject.getString("date"));

                    senderIdList.add(aa);
                    messageList.add(jsonObject.getString("content"));
                    dateList.add(jsonObject.getString("date"));
                    messageIdList.add(jsonObject.getInt("messageID"));
                    System.out.println("add success00");
                    }
                //System.out.println(senderIdList.size());
                }
            urlConnection.disconnect();
            }catch (Exception e){
            e.printStackTrace();
            }
    }
    private static void getUser2(int userID) throws Exception{
        URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/user/getUser2"+"?userID="+userID);
        System.out.println(url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String tmp = convertInputStreamToString(in);
            JSONObject dataObj = new JSONObject(tmp);
            System.out.println("User ID is: "+userID);
            String name = dataObj.getString("name");
            senderNameList.add(name);
            System.out.println("name is: "+name);
            System.out.println("Facebook Token is: "+dataObj.getString("fbToken"));
            System.out.println("Email is: '"+dataObj.getString("email"));
            System.out.println("Credit is: "+dataObj.getString("credit"));

            } finally {
            urlConnection.disconnect();
            }
    }
    private static void getPostDetail(int postID) throws Exception{
        URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/getDetail?postID="+postID);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String tmp = convertInputStreamToString(in);
            JSONObject dataObj = new JSONObject(tmp);
            postTheme = dataObj.getString("title");
            System.out.println("The theme of this post: "+postTheme);
            postDetail = dataObj.getString("postContent");
            System.out.println("Detail: "+postDetail);
            postIsSolved = dataObj.getBoolean("isSolved");
            String base64String = dataObj.getString("attachments");
            //int userID = dataObj.getInt("userID");
            if(!base64String.equals("no image")) {
                byte[] decodedString = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                postBitmap = decodedByte;
                System.out.println("post image download success.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            urlConnection.disconnect();
        }
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    public void toMyPost(View v){
        this.finish();
    }

    public void messageDetail(View v){
        Intent intentMessageDetail = new Intent(this,MessageDetailActivity.class);
        this.startActivity(intentMessageDetail);
    }

    class MyCustomAdapter extends BaseAdapter {

        private
        String userName[];             //this is the introductory way to store the List data.  The way it's usually done is by creating
        String replyMessage[];  //the "better" way is to encapsulate the list items into an object, then create an arraylist of objects.
//     int episodeImages[];         //this approach is fine for now.

        //    ArrayList<String> episodes;

        //ArrayList<Integer> episodeImages;  //Well, we can use one arrayList too...

        Context context;   //What does refer to?  Context enables access to application specific resources.  Eg, spawning & receiving intents, locating the various managers.

        public MyCustomAdapter(Context aContext) {
//initializing our data in the constructor.
//        episodes = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episodes));  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
//        episodeDescriptions = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episode_descriptions));  //Also casting to a friendly ArrayList.
            context = aContext;  //saving the context we'll need it again.

            int len = senderNameList.size();
            System.out.println("length is: +++++++"+len);
            userName = new String[len];
            replyMessage = new String[len];
            for(int i=0;i<len;i++){
                userName[i] = senderNameList.get(i).toString();
                replyMessage[i] = messageList.get(i).toString();
            }

            //userName = aContext.getResources().getStringArray(R.array.userName);  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
            //replyMessage = aContext.getResources().getStringArray(R.array.replyMessage);

        }

        @Override
        public int getCount() {
//        return episodes.size();  //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
            return userName.length;  //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
        }

        @Override
        public Object getItem(int position) {
//        return episodes.get(position);  //In Case you want to use an ArrayList
            return userName[position];  //really should be retuning entire set of row data, but it's up to us, and we aren't using.
        }

        @Override
        public long getItemId(int position) {
            return position;  //don't really use this, but have to do something since we had to implement (base is abstract).
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {  //convertView is Row, parent is the layout that has the row Views.
            //THIS IS WHERE THE ACTION HAPPENS.  Let's optimize a bit by checking to see if we need to inflate, or if it's already been inflated...
            View row;  //this will refer to the row to be inflated or displayed if it's already been displayed. (listview_row.xml)
            if (convertView == null) {  //indicates this is the first time we are creating this row.
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  //CRASH
                row = inflater.inflate(R.layout.activity_listview_reply, parent, false);
            } else {
                row = convertView;
            }
            // 2. Now that we have a valid row instance, we need to get references to the views within that row.
            //ImageView imgEpisode = (ImageView) row.findViewById(R.id.imgEpisode);  //notice we prefixed findViewByID with row, why?  row, is the container.
            TextView TVuserName = (TextView) row.findViewById(R.id.LVuserName);
            TextView TVreplyMessage = (TextView) row.findViewById(R.id.LVmessage);

            btnAccept = (Button)row.findViewById(R.id.button23);
//        tvEpisodeTitle.setText(episodes.get(position));  //puts the predefined titles in the textview.
//

            TVuserName.setText(userName[position]);
            TVreplyMessage.setText(replyMessage[position]);
            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent toContactIntent = new Intent(act,contactActivity.class);
                    toContactIntent.putExtra("id",Integer.valueOf(senderIdList.get(position).toString()));
                    toContactIntent.putExtra("messageID",Integer.valueOf(messageIdList.get(position).toString()));
                    toContactIntent.putExtra("postID",postId);
                    act.startActivity(toContactIntent);
                }
            });

            return row;  //once the row is fully constructed, return it.  Hey whatif we had buttons, can we target onClick Events within the rows, yep!
        }

    }
}
