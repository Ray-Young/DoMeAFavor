package com.example.a47668.finalproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.view.GestureDetectorCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.internal.api.StatsImpl;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Marker;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;

import android.location.Location;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.resource.Singleton;

import org.json.JSONArray;
import org.json.JSONObject;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

public class MainActivity extends AppCompatActivity
        implements GestureDetector.OnGestureListener,
        GoogleApiClient.ConnectionCallbacks,
        GestureDetector.OnDoubleTapListener,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    GoogleMap mgoogleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */

    private Marker myMarker;
    private String leo;
    private GestureDetectorCompat GD;
    private Button btnPersonal, btnPost, btnMessage;
    private static String[] bubbleTitle;

    private ArrayList<Object> postIDList = new ArrayList<>();
    private ArrayList<Object> titleList = new ArrayList<>();
    private ArrayList<Object> postContentList = new ArrayList<>();
    private ArrayList<Object> locationList = new ArrayList<>();
    private ArrayList<Object> dateList = new ArrayList<>();
    private ArrayList<Object> rewardList = new ArrayList<>();
    private ArrayList<Object> userIDList = new ArrayList<>();
    private ArrayList<Object> isSolvedList = new ArrayList<>();
    private ArrayList<Object> nameList = new ArrayList<>();

    public dataSingleton dsl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Do Me A Favor");


        GD = new GestureDetectorCompat(this, this);   //Context, Listener as per Constructor Doc.
        GD.setOnDoubleTapListener(this);   //DoubleTaps implemented a bit differently, must be bound like this.

        dsl = new dataSingleton();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        btnPersonal = (Button) findViewById(R.id.button);
        btnPost = (Button) findViewById(R.id.button2);
        btnMessage = (Button) findViewById(R.id.button3);
//download data from server...
        try {
            String overview = getOverView();
            JSONArray jArray = new JSONArray(overview);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject dataObj = jArray.getJSONObject(i);
                System.out.println(dataObj);
                postIDList.add(dataObj.get("postID"));
                titleList.add(dataObj.get("title"));
                postContentList.add(dataObj.get("postContent"));
                locationList.add(dataObj.get("location"));
                dateList.add(dataObj.get("date"));
                rewardList.add(dataObj.get("reward"));
                userIDList.add(dataObj.get("userID"));
                isSolvedList.add(dataObj.get("isSolved"));
                nameList.add(dataObj.get("name"));
                //System.out.println("@@@@@@@@@@@@@@@@@@@"+dataObj.get("name"));
            }
            //write data to dataSingleton class...
            dsl.getInstance().setPostId(postIDList);
            dsl.getInstance().setTitle(titleList);
            dsl.getInstance().setPostContent(postContentList);
            dsl.getInstance().setLocation(locationList);
            dsl.getInstance().setDate(dateList);
            dsl.getInstance().setReward(rewardList);
            dsl.getInstance().setUserId(userIDList);
            dsl.getInstance().setIsSolved(isSolvedList);
            dsl.getInstance().setName(nameList);
            //int s = dsl.getInstance().getLocation().size();
            //System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        initMap();
        //System.out.println(dsl.getInstance().getDate().get(1));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.GD.onTouchEvent(event);               //insert this line to consume the touch event locally by our GD,
        return super.onTouchEvent(event);          //if we have a handler for the touch event we will handle before passing on.
    }

    public void toPersonal(View v) {
        Intent intentPersonal = new Intent(this, PersonalActivity.class);
        this.startActivity(intentPersonal);
    }

    public void toPost(View v) {
        Intent intentPost = new Intent(this, PostActivity.class);
        this.startActivity(intentPost);
    }

    public void toMessage(View v) {
        Intent intentMessage = new Intent(this, MessageActivity.class);
        this.startActivity(intentMessage);
    }

    public void bubbleClick(View v) throws Exception {
        Intent intentBubbleClick = new Intent(this, DetailPostActivity.class);
        String pass = "6";
        intentBubbleClick.putExtra("PostId", pass);
        this.startActivity(intentBubbleClick);

    }

    public String getOverView() throws Exception {
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


    /**
     * public void getUser(String user)throws Exception{
     * <p>
     * URL url = new URL("http://10.250.2.106:8080/DoMeAFavorServer/rest/user/getUser"+"?name="+user);
     * HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
     * try {
     * InputStream in = new BufferedInputStream(urlConnection.getInputStream());
     * //System.out.println("---------------"+convertInputStreamToString(in));
     * leo = convertInputStreamToString(in);
     * } finally {
     * urlConnection.disconnect();
     * }
     * }
     **/
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }

    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        // Disconnecting the client invalidates it.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    public void onConnected(Bundle dataBundle) {
        // Get last known recent location.
        Location mCurrentLocation = null;
        try {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } catch (SecurityException se) {
            Log.i("DEBUG", "err");
        }
        Log.d("DEBUG", "current: ");
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            Log.d("DEBUG", "current location: " + mCurrentLocation.toString());
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            //LatLng myLocation = new LatLng(42.3601, 71.0589) ;
//private const int MAP_ZOOM = 11 ;
            mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    // Trigger new location updates at interval
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        //mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title("Cur"));
        //Toast.makeText(this, msg,e Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i("Fail", "connection failed");
    }

    //getPostLocation()
    private void initMap() {
        MapFragment mf = (MapFragment) getFragmentManager().findFragmentById(R.id.mapfragment);
        mf.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mgoogleMap = googleMap;
            mgoogleMap.setMyLocationEnabled(true);
            ArrayList<Object> locationList;
            ArrayList<Object> locationListString = new ArrayList<>();
            locationList = dsl.getInstance().getLocation();
            //System.out.println(locationList.get(0).toString());
            int len = locationList.size();
            System.out.println("!!!!!!!!!!!!!!!!!!!number of posts:" + len);
            //String lat = locationList.get(0).toString();
            float locationFloatLat[] = new float[len];
            float locationFloatLog[] = new float[len];
            for (int i = 0; i < len; i++) {
                String[] part = locationList.get(i).toString().split(",");
                locationFloatLat[i] = Float.parseFloat(part[0]);
                locationFloatLog[i] = Float.parseFloat(part[1]);
            }
            //Location location;
            //LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            //mgoogleMap.addMarker(new MarkerOptions().position(loc).title("MyPos"));
            bubbleTitle = new String[len];
            for (int i = 0; i < len; i++) {
                String ii = "" + i;
                bubbleTitle[i] = "Qn" + ii;
            }
            for (int i = 0; i < len; i++) {
                mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(locationFloatLat[i], locationFloatLog[i])).title(bubbleTitle[i]));
            }
            //mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(56.351245, -3.617110)).title("Qn1"));
            myMarker = mgoogleMap.addMarker(new MarkerOptions().position(new LatLng(locationFloatLat[0], locationFloatLog[0])).title(bubbleTitle[0]));

        } catch (Exception e) {
            e.printStackTrace();
        }
        mgoogleMap.setOnMarkerClickListener(this);
        LatLng boston = new LatLng(42.407240, -71.120129);
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(boston));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.getPosition();
        System.out.println("bubble click");
        ArrayList<Object> locationList2;
        locationList2 = dsl.getInstance().getLocation();
        for (int i = 0; i < locationList2.size(); i++) {
            if (marker.getTitle().equals(bubbleTitle[i])) {
                System.out.println("bubble click222222222" + i);
                Intent intentBubbleClick = new Intent(this, DetailPostActivity.class);
                String postIdPass = dsl.getInstance().getPostId().get(i).toString();
                //String pass = "" + i;
                intentBubbleClick.putExtra("PostId", postIdPass);
                this.startActivity(intentBubbleClick);
            }
        }
        return false;
    }


    //Menu stuff...
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;  //we've handled it!
        //return super.onCreateOptionsMenu(menu);  //what happens if we let this run?
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemID1) {
            Toast.makeText(getBaseContext(), "item1.", Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.itemID2) {
            //Toast.makeText(getBaseContext(), "item2.", Toast.LENGTH_LONG).show();
            Intent logOutIntent = new Intent(this, LoginActivity.class);
            this.startActivity(logOutIntent);
            //return true;
        }
        return super.onOptionsItemSelected(item);  //default behavior (fall through)
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
            Intent intentMessage = new Intent(this, MessageActivity.class);
            this.startActivity(intentMessage);
            overridePendingTransition(R.anim.tleft, 0);
        }
        if (e2.getX() - e1.getX() > 300) {
            Intent intentPersonal = new Intent(this, PersonalActivity.class);
            this.startActivity(intentPersonal);
            overridePendingTransition(R.anim.toright, 0);
        }
        return false;
    }

}
