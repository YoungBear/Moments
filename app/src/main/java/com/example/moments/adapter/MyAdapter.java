package com.example.moments.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moments.R;
import com.example.moments.adapter.viewholder.TweetViewHolder;
import com.example.moments.bean.BaseBean;
import com.example.moments.bean.PhotoInfo;
import com.example.moments.bean.TweetBean;
import com.example.moments.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearyang on 2016/11/23.
 */

public class MyAdapter extends RecyclerView.Adapter {

    private static final String TAG = MyAdapter.class.getSimpleName();

    private final static int TYPE_HEAD = 0;
    private final static int TYPE_COMMON = 1;

    //动态
    private List<BaseBean> mDatas;

    private Context mContext;

    public MyAdapter(Context context) {
        mContext = context;
    }

    public List<BaseBean> getDatas() {
        return mDatas;
    }

    public void setDatas(List<BaseBean> datas) {
        mDatas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder, viewType: " + viewType);
        RecyclerView.ViewHolder viewHolder;
        if (viewType == TYPE_HEAD) {
            View headView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tweet_head_item, parent, false);
            viewHolder = new HeaderViewHolder(headView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_tweet_item, parent, false);
            viewHolder = new TweetViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseBean baseBean = mDatas.get(position);
        Log.d(TAG, "onBindViewHolder, position: " + position + ", baseBean: " + baseBean.toString());
        if (getItemViewType(position) == TYPE_HEAD) {
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;

            if (baseBean instanceof UserBean) {
                UserBean userBean = (UserBean) baseBean;
                Glide.with(mContext).load(userBean.getProfile_image()).into(headerViewHolder.imgProfile);
                Glide.with(mContext).load(userBean.getAvatar()).into(headerViewHolder.imgAvatar);
                headerViewHolder.txtUsername.setText(userBean.getUsername());
            }

        } else {
            TweetViewHolder tweetViewHolder = (TweetViewHolder) holder;
            if (baseBean instanceof TweetBean) {
                TweetBean tweetBean = (TweetBean) baseBean;

                // TODO: 16/11/23 暂时使用可访问的图片
//                Glide.with(mContext).load("http://info.thoughtworks.com/rs/thoughtworks2/images/glyph_badge.png").into(tweetViewHolder.imgAvatar);
                Glide.with(mContext).load(tweetBean.getSender().getAvatar()).into(tweetViewHolder.imgAvatar);
                tweetViewHolder.txtUsername.setText(tweetBean.getSender().getUsername());
                if (!TextUtils.isEmpty(tweetBean.getContent())) {
                    tweetViewHolder.txtContent.setVisibility(View.VISIBLE);
                    tweetViewHolder.txtContent.setText(tweetBean.getContent());
                } else {
                    tweetViewHolder.txtContent.setVisibility(View.GONE);
                }

                if (tweetBean.getImages() != null && tweetBean.getImages().size() >= 1) {
                    tweetViewHolder.mMultiImageView.setVisibility(View.VISIBLE);
                    List<PhotoInfo> photoList = new ArrayList<>(tweetBean.getImages().size());
                    // TODO: 2016/11/23 处理MultiImageView的宽和高
                    for (int i = 0; i < tweetBean.getImages().size(); i++) {
                        PhotoInfo photoInfo = new PhotoInfo();
                        photoInfo.url = tweetBean.getImages().get(i).getUrl();
                        photoList.add(photoInfo);
                    }
                    tweetViewHolder.mMultiImageView.setList(photoList);
                } else {
                    tweetViewHolder.mMultiImageView.setVisibility(View.GONE);
                }

                if (tweetBean.getComments() != null && tweetBean.getComments().size() >= 1) {
                    tweetViewHolder.layoutCommentList.setVisibility(View.VISIBLE);
                    tweetViewHolder.mCommentListView.setDatas(tweetBean.getComments());
                } else {
                    tweetViewHolder.layoutCommentList.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return TYPE_HEAD;
        } else {
            return TYPE_COMMON;
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgProfile;
        private ImageView imgAvatar;
        private TextView txtUsername;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            imgProfile = (ImageView) itemView.findViewById(R.id.img_profile_image);
            imgAvatar = (ImageView) itemView.findViewById(R.id.img_avatar);
            txtUsername = (TextView) itemView.findViewById(R.id.txt_username);
        }
    }
}
