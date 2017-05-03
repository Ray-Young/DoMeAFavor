package com.example.a47668.finalproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.v7.view.ViewPropertyAnimatorCompatSet;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import android.content.Intent;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.Arrays;

/**
 * Commented on May 2nd, 2017
 * This Activity has the codes for facebook login
 */

public class LoginActivity extends AppCompatActivity {
    LoginButton loginButton;
    TextView txtView;
    CallbackManager callbackmgr;
    boolean success = false;
    Activity act;
    com.facebook.login.widget.ProfilePictureView profilePictureView;
    public dataSingleton dsl = new dataSingleton();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        FacebookSdk.sdkInitialize(getApplicationContext());

        act = this;
        loginButton = (LoginButton) findViewById(R.id.btnid);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email", "user_birthday", "user_friends"));

        txtView = (TextView) findViewById(R.id.txtvw);
        callbackmgr = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackmgr, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                final String Token = loginResult.getAccessToken().getToken().toString();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    System.out.println("Here------------" + object == null);
                                    System.out.println(object);
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    String userID = object.getString("id");


                                    System.out.println(email);
                                    System.out.println(name);
                                    System.out.println(userID);
                                    System.out.println("haaaaaaaaaaaaaaaaaa" + Token);
                                    URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
                                    Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                                    dsl.getInstance().setMyUserName(name);
                                    System.out.println("after set userName:" + dsl.getInstance().getMyUserName().toString());
                                    dsl.getInstance().setFbEmail(email);
                                    dsl.getInstance().setFbToken(userID);
                                    dsl.getInstance().setFbPhoto(bitmap);
                                    System.out.println(bitmap);
                                    addUser(name, email, userID, bitmap);
                                    getUser(userID);
                                    System.out.println("HERE!!!" + dsl.getInstance().getMyUserId());

                                    Intent intentToMain = new Intent(act, MainActivity.class);
                                    act.startActivity(intentToMain);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,link");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

                txtView.setText("Login cancelled :-( ");

            }

            @Override
            public void onError(FacebookException error) {

                txtView.setText("Login ERROR FROM API  :-| ");

            }

        });

    }

    public void getUser(String token) throws Exception {

        URL url = new URL("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/user/getUser" + "?fbToken=" + token);
        System.out.println(url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            String tmp = convertInputStreamToString(in);
            System.out.println(tmp);
            JSONArray jArray = new JSONArray(tmp);
            JSONObject dataObj = jArray.getJSONObject(0);
            dsl.getInstance().setMyUserId(dataObj.getInt("id"));

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


    private static void addUser(String name, String email, String token, Bitmap bm) throws IOException {
        SyncHttpClient client = new SyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name", name);
        params.put("email", email);
        params.put("fbToken", token);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        params.put("fbPhoto", encodedImage);

        client.post("http://sample-env.mebx8vgf3h.us-west-2.elasticbeanstalk.com/rest/user/addUser", params, new TextHttpResponseHandler() {
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

    }

    public void toMain(View v) {
        if (success == true) {
            Intent intentMain = new Intent(this, MainActivity.class);
            this.startActivity(intentMain);
        } else {
            Toast.makeText(getBaseContext(), "login first.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackmgr.onActivityResult(requestCode, resultCode, data);
    }
}
