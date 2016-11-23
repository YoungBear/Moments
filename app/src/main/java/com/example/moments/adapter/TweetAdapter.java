package com.example.moments.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.moments.R;
import com.example.moments.bean.TweetBean;

import java.util.List;

import static com.example.moments.R.id.item_txt_username;

/**
 * Created by youngbear on 16/11/20.
 */

public class TweetAdapter extends BaseAdapter {

    private static final String TAG = TweetAdapter.class.getSimpleName();

    private Context mContext;
    private List<TweetBean> mTweetBeanList;

    public TweetAdapter(Context context, List<TweetBean> tweetBeanList) {
        this.mContext = context;
        this.mTweetBeanList = tweetBeanList;
    }

    @Override
    public int getCount() {
        return mTweetBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView, position: " + position);
        ViewHolder holder = null;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.layout_tweet_item, null);
            holder.item_img_avatar = (ImageView) convertView.findViewById(R.id.item_img_avatar);
            holder.item_txt_username = (TextView) convertView.findViewById(item_txt_username);
            holder.item_txt_content = (TextView) convertView.findViewById(R.id.item_txt_content);
//            holder.item_img_test = (ImageView) convertView.findViewById(R.id.item_img_test);
//            holder.item_txt_comment_test = (TextView) convertView.findViewById(R.id.item_txt_comment_test);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        TweetBean tweetBean = mTweetBeanList.get(position);
        if (tweetBean.getClass() != null && tweetBean.getImages() != null) {
            //忽略没有内容或者图片的消息
            Glide.with(mContext)
                    .load(tweetBean.getSender().getAvatar())
                    .into(holder.item_img_avatar);
            holder.item_txt_username.setText(tweetBean.getSender().getUsername());
            if (tweetBean.getContent() != null) {
                holder.item_txt_content.setText(tweetBean.getContent());
            }
            if (tweetBean.getImages() != null) {
                // TODO: 16/11/20 temp show only first image of tweet
                Glide.with(mContext)
                        .load(tweetBean.getImages().get(0).getUrl())
                        .into(holder.item_img_test);
            }
            if (tweetBean.getComments() != null && tweetBean.getComments().size() >= 1) {
                // TODO: 16/11/20 temp show first comment
                String username = tweetBean.getComments().get(0).getSender().getUsername();
                String content = tweetBean.getComments().get(0).getContent();
                holder.item_txt_comment_test.setText(username + " : " + content);
            }

        }
        return convertView;
    }

    private static class ViewHolder {
        private ImageView item_img_avatar;
        private TextView item_txt_username;
        private TextView item_txt_content;
        private ImageView item_img_test;
        private TextView item_txt_comment_test;
    }
}
