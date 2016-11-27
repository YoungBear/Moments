package com.example.moments;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.moments.adapter.MyAdapter;
import com.example.moments.bean.BaseBean;
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

    private List<BaseBean> mDatas = new ArrayList<>();
    private List<TweetBean> tweetBeanList = new ArrayList<>();
    private int mIndex;

    private SuperRecyclerView recyclerView;
    private LinearLayoutManager layoutManager;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        mHandler = new Handler();

        loadData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(loadMoreRunnable);
    }

    private void initViews() {
        recyclerView = (SuperRecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.hideProgress();

        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh...");
                loadData();

            }
        });

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    int position = layoutManager.findLastCompletelyVisibleItemPosition();
                    loadMore(position);
                    Log.d(TAG, "onScrollStateChanged, position: " + position);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void loadData() {
        mDatas.clear();
        tweetBeanList.clear();
        profileRequest();
    }

    private void loadMore(int position) {
        Log.d(TAG, "loadMore, position: " + position + ", mIndex: " + mIndex);
        if (position >= mIndex && position < tweetBeanList.size()) {

            Log.d(TAG, "loadMore, position: " + position);
            recyclerView.showMoreProgress();
            mHandler.removeCallbacks(loadMoreRunnable);
            mHandler.postDelayed(loadMoreRunnable, 1000);
        }

    }

    private Runnable loadMoreRunnable = new Runnable() {
        @Override
        public void run() {
            recyclerView.hideMoreProgress();

            if (mAdapter != null) {
                int i = 0;
                while (i + mIndex < tweetBeanList.size() && i < 5) {
                    mDatas.add(tweetBeanList.get(i + mIndex));
                    i++;
                }
                mIndex += i;
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    // TODO: 16/11/20 用户的头像无法访问

    private void profileRequest() {
        recyclerView.setRefreshing(true);

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
                            if (mAdapter == null) {
                                mAdapter = new MyAdapter(MainActivity.this);
                                mDatas.add(userBean);
                                mAdapter.setDatas(mDatas);
                                recyclerView.setAdapter(mAdapter);
                            } else {
                                mDatas.add(userBean);
                                mAdapter.notifyDataSetChanged();
                            }
                            tweetsRequest();
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
    }

    private void tweetsRequest() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                UrlValue.URL_TWEETS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse, response.length(): " + response.length());
                        tweetBeanList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                Log.d(TAG, "i: " + i + " : " + response.getJSONObject(i).toString());
                                Gson gson = new Gson();
                                TweetBean tweetBean = gson.fromJson(
                                        response.getJSONObject(i).toString(),
                                        TweetBean.class);
                                //忽略空信息
                                if (tweetBean != null) {
                                    if (tweetBean.getContent() == null && tweetBean.getImages() == null) {
                                        continue;
                                    } else {
                                        Log.d(TAG, "i: " + i + ", added...");
                                        tweetBeanList.add(tweetBean);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        Log.d(TAG, "mAdapter == null: " + (mAdapter == null) + ", tweetBeanList.size(): " + tweetBeanList.size());
                        if (mAdapter != null) {
                            int i = 0;
                            while (i < tweetBeanList.size() && i < 5) {
                                mDatas.add(tweetBeanList.get(i));
                                i++;
                            }
                            mIndex = i;
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
