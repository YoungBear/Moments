package com.example.moments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moments.adapter.MyAdapter;
import com.example.moments.bean.TweetBean;
import com.example.moments.bean.UserBean;
import com.example.moments.url.UrlValue;
import com.example.moments.volley.VolleyController;
import com.google.gson.Gson;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MyAdapter mAdapter;

    //个人信息
    private UserBean mUserBean;
    //动态
    private List<TweetBean> mDatas = new ArrayList<>();


    private SuperRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        testRequest();
    }

    private void initViews() {
        recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }
    // TODO: 16/11/20 用户的头像无法访问 

    private void testRequest() {
        JSONObject jsonObject = new JSONObject();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                UrlValue.URL_USER_INFO,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            mUserBean = getUserBean(response);
                            Log.d(TAG, "jsonObjectRequest, onResponse, " + mUserBean.toString());
                            if (mAdapter == null) {
                                mAdapter = new MyAdapter(MainActivity.this);
                                mAdapter.setUserBean(mUserBean);
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                mAdapter.notifyDataSetChanged();

                            }
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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                UrlValue.URL_TWEETS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        Log.d(TAG, "onResponse, response.length(): " + response.length());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Log.d(TAG, "i: " + i + " : " + response.getJSONObject(i).toString());
                                Gson gson = new Gson();
                                TweetBean tweetBean = gson.fromJson(
                                        response.getJSONObject(i).toString(),
                                        TweetBean.class);
                                mDatas.add(tweetBean);


//                                Log.d(TAG, "tweetBean: " + tweetBean.getContent()
//                                        + "\n" + tweetBean.getImages()
//                                        + "\n" + tweetBean.getSender()
//                                        + "\n" + tweetBean.getComments());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(TAG, "mAdapter == null: " + (mAdapter == null) + ", mDatas.size(): " + mDatas.size());
                        if (mAdapter != null) {
                            mAdapter.setDatas(mDatas);

                            mAdapter.notifyDataSetChanged();

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        VolleyController.getInstance(this).addToRequestQueue(jsonArrayRequest, "tweets");
        
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
