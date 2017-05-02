package com.example.a47668.finalproject;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cz.msebera.android.httpclient.Header;
import com.loopj.android.http.*;

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

public class MyPostActivity extends AppCompatActivity {
    private
    ListView lvEpisodes;
    ListAdapter lvAdapter;

    Activity act;
    private dataSingleton dsl = new dataSingleton();
    private int myUserId;
    public static ArrayList<Object> myPostIdList = new ArrayList<>();
    public static ArrayList<Object> dslPostIdList = new ArrayList<>();
    public static ArrayList<Object> titleList = new ArrayList<>();
    public static ArrayList<Object> dateList = new ArrayList<>();
    public static int[] MyPostIDArray;
    /**
    private class myTask extends AsyncTask<URL, Integer, Long> {
        protected Long doInBackground(URL... urls) {
            try {
                getMyPost(myUserId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return (long)0;
        }
        protected void onPostExecute(Long result) {
        }
    }**/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost);
        setTitle("My Post");
        act = this;
        dsl = new dataSingleton();
        myUserId = dsl.getInstance().getMyUserId();
        dslPostIdList = dsl.getInstance().getPostId();
        getMyPost(myUserId);
        System.out.println("myUserId: "+myUserId);
        lvEpisodes = (ListView) findViewById(R.id.lvEpisodes);
        lvAdapter = new MyCustomAdapter(this.getBaseContext());  //instead of passing the boring default string adapter, let's pass our own, see class MyCustomAdapter below!
        lvEpisodes.setAdapter(lvAdapter);
    }
    private static void getMyPost(int userID){
        try{
            URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/getMyPost?userID="+userID);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String tmp = convertInputStreamToString(in);
            JSONArray jsonArray = new JSONArray(tmp);
            if(jsonArray.length() == 0){
                System.out.println("You don't have any post");
                }else {
                System.out.print("json");
                System.out.println(jsonArray.length());
                titleList.clear();
                dateList.clear();
                myPostIdList.clear();
                MyPostIDArray = new int[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //System.out.println("postId is: "+jsonObject.getString("postID"));
                    //System.out.println("title is: " + jsonObject.getString("title"));
                    //System.out.println("postContent is: " + jsonObject.getString("postContent"));
                    //System.out.println("location is: "+jsonObject.getString("location"));
                    //System.out.println("date is: "+jsonObject.getString("date"));
                    //System.out.println("reward is: "+jsonObject.getInt("reward"));
                    //System.out.println("userID is: "+jsonObject.getInt("userID"));
                    //System.out.println("name is: "+jsonObject.getString("name"));
                    //System.out.println(jsonObject.getString("attachments"));

                    String unPostId = jsonObject.getString("postID");
                    titleList.add(jsonObject.getString("title"));
                    dateList.add(jsonObject.getString("date"));
                    myPostIdList.add(jsonObject.getString("postID"));
                    System.out.println(myPostIdList);
                    System.out.println("postId is: "+jsonObject.getInt("postID"));
                    MyPostIDArray[i] = jsonObject.getInt("postID");
                    String base64String = jsonObject.getString("attachments");
                    if(base64String.equals("no image")){
                            System.out.println("No attachments for this post!!!");
                        }else{
                            byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        System.out.println("BitMap is: "+decodedByte);
                        }
                }
                }
            urlConnection.disconnect();
            }catch(Exception e){
            e.printStackTrace();
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

    public void toMain(View v) {
        Intent intentToMain = new Intent(this, MainActivity.class);
        this.startActivity(intentToMain);
    }



    public void toDetailPosttoDetailPost(View v) {
        //String ClickedPostId = lvAdapter.getItemId();


    }

    //adapter staff...
    class MyCustomAdapter extends BaseAdapter {

        private
        String title[];             //this is the introductory way to store the List data.  The way it's usually done is by creating
        String date[];  //the "better" way is to encapsulate the list items into an object, then create an arraylist of objects.
//     int episodeImages[];         //this approach is fine for now.

        //    ArrayList<String> episodes;

        //ArrayList<Integer> episodeImages;  //Well, we can use one arrayList too...

        Context context;   //What does refer to?  Context enables access to application specific resources.  Eg, spawning & receiving intents, locating the various managers.

        public MyCustomAdapter(Context aContext) {
//initializing our data in the constructor.
//        episodes = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episodes));  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
//        episodeDescriptions = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episode_descriptions));  //Also casting to a friendly ArrayList.
            context = aContext;  //saving the context we'll need it again.
            //episodes = aContext.getResources().getStringArray(R.array.episodes);  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
            //episodeDescriptions = aContext.getResources().getStringArray(R.array.episode_descriptions);
            int len = titleList.size();
            System.out.println(len);
            title = new String[len];
            date = new String[len];
            for(int i=len-1;i>=0;i--){
                title[i] = titleList.get(i).toString();
                date[i] = dateList.get(i).toString();
            }
            System.out.println(title);
            System.out.println(date);
        }

        @Override
        public int getCount() {
//        return episodes.size();  //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
            return title.length;  //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
        }

        @Override
        public Object getItem(int position) {
//        return episodes.get(position);  //In Case you want to use an ArrayList
            return title[position];  //really should be retuning entire set of row data, but it's up to us, and we aren't using.
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
                row = inflater.inflate(R.layout.activity_listview, parent, false);
            } else {
                row = convertView;
            }
            // 2. Now that we have a valid row instance, we need to get references to the views within that row.
            //ImageView imgEpisode = (ImageView) row.findViewById(R.id.imgEpisode);  //notice we prefixed findViewByID with row, why?  row, is the container.
            TextView LVtheme = (TextView) row.findViewById(R.id.LVtheme);
            TextView LVtime = (TextView) row.findViewById(R.id.LVtime);
            Button btnEnter = (Button) row.findViewById(R.id.LVbutton);

//        tvEpisodeTitle.setText(episodes.get(position));  //puts the predefined titles in the textview.
//
            LVtheme.setText(title[position]);
            LVtime.setText(date[position]);

            try {
                btnEnter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("now" + String.valueOf(position) + "is clicked");
                        Intent intentToDetailPost = new Intent(act, MyDetailPostActivity.class);
                        String realPostId = Integer.toString(MyPostIDArray[position]);//dsl.getInstance().getPostId().get(Integer.valueOf((String)myPostIdList.get(position))).toString();
                        System.out.println("realPostId here: "+realPostId);
                        intentToDetailPost.putExtra("realPostId",realPostId);
                        act.startActivity(intentToDetailPost);
                    }
                });
            }catch (NullPointerException e){

            }


            return row;  //once the row is fully constructed, return it.  Hey whatif we had buttons, can we target onClick Events within the rows, yep!
        }

    }


}