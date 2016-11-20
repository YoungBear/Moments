package com.example.moments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.example.moments.bean.UserBean;
import com.example.moments.url.UrlValue;
import com.example.moments.volley.VolleyController;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView imgProfile;
    private ImageView imgAvatar;
    private TextView txtNick;
    private TextView txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        testRequest();
    }

    private void initViews() {
        imgProfile = (ImageView) findViewById(R.id.img_profile_image);
        imgAvatar = (ImageView) findViewById(R.id.img_avatar);
        txtNick = (TextView) findViewById(R.id.txt_nick);
        txtUsername = (TextView) findViewById(R.id.txt_username);
    }

    private void testRequest() {
        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                UrlValue.URL_USER_INFO,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            UserBean userBean = getUserBean(response);
                            Log.d(TAG, "jsonObjectRequest, onResponse, " + userBean.toString());
                            Glide.with(MainActivity.this)
                                    .load(userBean.getProfile_image())
                                    .into(imgProfile);
                            Glide.with(MainActivity.this)
                                    .load(userBean.getAvatar())
                                    .into(imgAvatar);
                            txtNick.setText(userBean.getNick());
                            txtUsername.setText(userBean.getUsername());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        VolleyController.getInstance(this).addToRequestQueue(jsonObjectRequest, "user_info");

//        StringRequest stringRequest = new StringRequest(
//                Request.Method.GET,
//                UrlValue.URL_USER_INFO,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d(TAG, "onResponse, response: " + response);
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "onErrorResponse, error: " + error.getMessage());
//                    }
//                }
//        );
//        VolleyController.getInstance(this).addToRequestQueue(stringRequest, "user_info");

//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
//                UrlValue.URL_TWEETS,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//
//                        Log.d(TAG, "onResponse, response.length(): " + response.length());
//                        for (int i = 0; i < response.length(); i++) {
//                            try {
//                                Log.d(TAG, "i: " + response.getJSONObject(i).toString());
//                                Gson gson = new Gson();
//                                TweetBean tweetBean = gson.fromJson(
//                                        response.getJSONObject(i).toString(),
//                                        TweetBean.class);
//
//                                Log.d(TAG, "tweetBean: " + tweetBean.getContent()
//                                        + "\n" + tweetBean.getImages()
//                                        + "\n" + tweetBean.getSender()
//                                        + "\n" + tweetBean.getComments());
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                    }
//                }
//        );
//        VolleyController.getInstance(this).addToRequestQueue(jsonArrayRequest, "tweets");


    }

    public static UserBean getUserBean(JSONObject jsonObject) throws JSONException {
        UserBean userBean = new UserBean();
        userBean.setProfile_image(jsonObject.getString("profile-image"));//不是下划线，是减号
        userBean.setAvatar(jsonObject.getString("avatar"));
        userBean.setNick(jsonObject.getString("nick"));
        userBean.setUsername(jsonObject.getString("username"));
        return userBean;
    }
}
