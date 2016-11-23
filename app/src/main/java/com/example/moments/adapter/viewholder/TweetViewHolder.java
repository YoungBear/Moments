package com.example.moments.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moments.R;
import com.example.moments.view.CommentListView;
import com.example.moments.view.MultiImageView;

/**
 * Created by bearyang on 2016/11/23.
 */

public class TweetViewHolder extends RecyclerView.ViewHolder {

    // 头像，用户名
    public ImageView imgAvatar;
    public TextView txtUsername;
    //public TextView txtNick;

    //发表的内容
    public TextView txtContent;

    public MultiImageView mMultiImageView;

    public LinearLayout layoutCommentList;

    public CommentListView mCommentListView;


    public TweetViewHolder(View itemView) {
        super(itemView);

        ViewStub viewStub = (ViewStub) itemView.findViewById(R.id.item_view_stub);

        // TODO: 2016/11/23 将multiImageView布局写到item里边
        initSubView(viewStub);

        imgAvatar = (ImageView) viewStub.findViewById(R.id.item_img_avatar);
        txtUsername = (TextView) viewStub.findViewById(R.id.item_txt_username);
        txtContent = (TextView) viewStub.findViewById(R.id.item_txt_content);

        layoutCommentList = (LinearLayout) viewStub.findViewById(R.id.item_layout_comment_list);
        mCommentListView = (CommentListView) viewStub.findViewById(R.id.item_comment_listview);

    }

    private void initSubView(ViewStub viewStub) {
        viewStub.setLayoutResource(R.layout.view_stub_multi_view);
        View subView = viewStub.inflate();
        this.mMultiImageView = (MultiImageView) subView.findViewById(R.id.item_multi_image_view);
    }
}
