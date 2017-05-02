package com.example.a47668.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SeeMoreActivity extends AppCompatActivity {
    private
    ListView lvEpisodes;
    ListAdapter lvAdapter;
    private Button lvButton;
    Activity act;
    public static int posterID;
    private dataSingleton dsl;
    private ArrayList<Object> postIDList = new ArrayList<>();
    private ArrayList<Object> titleList = new ArrayList<>();
    private ArrayList<Object> dateList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seemore);
        setTitle("Post List");

        act = this;
        dsl = new dataSingleton();
        try {
            String overview = getOverView();
            JSONArray jArray = new JSONArray(overview);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject dataObj = jArray.getJSONObject(i);
                System.out.println(dataObj);
                postIDList.add(dataObj.get("postID"));
                titleList.add(dataObj.get("title"));
                dateList.add(dataObj.get("date"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        lvEpisodes = (ListView) findViewById(R.id.lvEpisodes);
        lvAdapter = new SeeMoreActivity.MyCustomAdapter(this.getBaseContext());  //instead of passing the boring default string adapter, let's pass our own, see class MyCustomAdapter below!
        lvEpisodes.setAdapter(lvAdapter);

    }
    public void toDetailPost(View v){
        Intent intentToDetailPost = new Intent(this, DetailPostActivity.class);
        this.startActivity(intentToDetailPost);
    }
    public void returnToLast(View v){
        this.finish();
    }
    public void mainPage(View v){
        Intent intentMain = new Intent(this,MainActivity.class);
        this.startActivity(intentMain);
    }
    public String getOverView() throws Exception{
        URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/post/getOverview");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            //System.out.println("---------------"+convertInputStreamToString(in));
            return convertInputStreamToString(in);
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
            String base64String = dataObj.getString("attachments");
            //int userID = dataObj.getInt("userID");
            if(!base64String.equals("no image")) {
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                //imgBitmap = decodedByte;
            }
            //postTheme = dataObj.getString("title");
            //postDetail = dataObj.getString("postContent");
            //postLocation = dataObj.getString("location");
            //postDate = dataObj.getString("date");
            //postReward = dataObj.getInt("reward");
            posterID = dataObj.getInt("userID");
            //postIsSolved = dataObj.getBoolean("isSolved");
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
            title = new String[len];
            date = new String[len];
            for(int i=0;i<titleList.size();i++){
                title[i] = titleList.get(i).toString();
                date[i] = dateList.get(i).toString();
            }


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
            lvButton = (Button)row.findViewById(R.id.LVbutton);
//        tvEpisodeTitle.setText(episodes.get(position));  //puts the predefined titles in the textview.
//
            LVtheme.setText(title[position]);
            LVtime.setText(date[position]);

            try {
                lvButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.out.println("now" + String.valueOf(position) + "is clicked");
                        Intent intentMyDetailPost = new Intent(act, MyDetailPostActivity.class);
                        String realPostId = ""+ postIDList.get(position);//dsl.getInstance().getPostId().get(Integer.valueOf((String)myPostIdList.get(position))).toString();
                        try {
                            getPostDetail(Integer.valueOf(realPostId));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.out.println("realPostId here: " + realPostId);
                        intentMyDetailPost.putExtra("realPostId", realPostId);
                        if(posterID == dsl.getInstance().getMyUserId()){
                            act.startActivity(intentMyDetailPost);
                        }else{
                            Intent intentDetailPost = new Intent(act,DetailPostActivity.class);
                            intentDetailPost.putExtra("PostId",realPostId);
                            act.startActivity(intentDetailPost);
                        }


                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }

            return row;  //once the row is fully constructed, return it.  Hey whatif we had buttons, can we target onClick Events within the rows, yep!
        }

    }

    //Menu stuff...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menupost, menu);
        return true;  //we've handled it!
        //return super.onCreateOptionsMenu(menu);  //what happens if we let this run?
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.postID11) {
            Toast.makeText(getBaseContext(), "sorted by post time.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.postID22) {
            Toast.makeText(getBaseContext(), "sorted by post distance.", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);  //default behavior (fall through)
    }
}
