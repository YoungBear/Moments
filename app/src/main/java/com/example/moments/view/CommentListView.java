package com.example.moments.view;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moments.R;
import com.example.moments.bean.CommentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bearyang on 2016/11/23.
 */

public class CommentListView extends LinearLayout {

    private List<CommentBean> mDatas;
    private LayoutInflater mLayoutInflater;

    public CommentListView(Context context) {
        this(context, null, 0);
    }

    public CommentListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public List<CommentBean> getDatas() {
        return mDatas;
    }

    public void setDatas(List<CommentBean> datas) {
        if (null == datas) {
            datas = new ArrayList<CommentBean>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {

        removeAllViews();
        if (null == mDatas || mDatas.size() == 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            View view = getView(i);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }
            addView(view, i, layoutParams);
        }
    }

    private View getView(int position) {
        if (null == mLayoutInflater) {
            mLayoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = mLayoutInflater.inflate(R.layout.layout_comment_item, null, false);
        TextView txtComment = (TextView) convertView.findViewById(R.id.txt_comment);
        CommentBean commentBean = mDatas.get(position);
        String username = commentBean.getSender().getUsername();
        String content = commentBean.getContent();

        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(username + ": ");
        sb.append(content);
        sb.setSpan(new ForegroundColorSpan(Color.parseColor("#1e90ff")),
                0, username.length() + 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        txtComment.setText(sb);

        return convertView;

    }
}
