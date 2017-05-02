package com.example.a47668.finalproject;


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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MySolutionActivity extends AppCompatActivity {
    private
    ListView lvEpisodes;
    ListAdapter lvAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mysolution);
        lvEpisodes = (ListView) findViewById(R.id.lvEpisodes);
        lvAdapter = new MySolutionActivity.MyCustomAdapter(this.getBaseContext());  //instead of passing the boring default string adapter, let's pass our own, see class MyCustomAdapter below!
        lvEpisodes.setAdapter(lvAdapter);

    }
    public void toDetailPost(View v){
        Intent intentToDetailPost = new Intent(this, DetailPostActivity.class);
        this.startActivity(intentToDetailPost);
    }
    public void back(View v){
        this.finish();
    }

    class MyCustomAdapter extends BaseAdapter {

        private
        String episodes[];             //this is the introductory way to store the List data.  The way it's usually done is by creating
        String episodeDescriptions[];  //the "better" way is to encapsulate the list items into an object, then create an arraylist of objects.
//     int episodeImages[];         //this approach is fine for now.

        //    ArrayList<String> episodes;

        //ArrayList<Integer> episodeImages;  //Well, we can use one arrayList too...

        Context context;   //What does refer to?  Context enables access to application specific resources.  Eg, spawning & receiving intents, locating the various managers.

        public MyCustomAdapter(Context aContext) {
//initializing our data in the constructor.
//        episodes = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episodes));  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
//        episodeDescriptions = (ArrayList<String>) Arrays.asList(aContext.getResources().getStringArray(R.array.episode_descriptions));  //Also casting to a friendly ArrayList.
            context = aContext;  //saving the context we'll need it again.

            episodes = aContext.getResources().getStringArray(R.array.episodes);  //retrieving list of episodes predefined in strings-array "episodes" in strings.xml
            episodeDescriptions = aContext.getResources().getStringArray(R.array.episode_descriptions);


        }

        @Override
        public int getCount() {
//        return episodes.size();  //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
            return episodes.length;  //all of the arrays are same length, so return length of any... ick!  But ok for now. :)
        }

        @Override
        public Object getItem(int position) {
//        return episodes.get(position);  //In Case you want to use an ArrayList
            return episodes[position];  //really should be retuning entire set of row data, but it's up to us, and we aren't using.
        }

        @Override
        public long getItemId(int position) {
            return position;  //don't really use this, but have to do something since we had to implement (base is abstract).
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {  //convertView is Row, parent is the layout that has the row Views.
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

//        tvEpisodeTitle.setText(episodes.get(position));  //puts the predefined titles in the textview.
//
            LVtheme.setText(episodes[position]);
            LVtime.setText(episodeDescriptions[position]);


            return row;  //once the row is fully constructed, return it.  Hey whatif we had buttons, can we target onClick Events within the rows, yep!
        }

    }
}
