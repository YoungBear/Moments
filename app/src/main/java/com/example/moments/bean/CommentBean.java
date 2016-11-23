package com.example.moments.bean;

/**
 * Created by bearyang on 2016/11/23.
 */

public class CommentBean {
    private String content;
    /**
     * username : outman
     * nick : Super hero
     * avatar : https://encrypted-tbn3.gstatic.com/images?q=tbn:ANd9GcRJm8UXZ0mYtjv1a48RKkFkdyd4kOWLJB0o_l7GuTS8-q8VF64w
     */

    private SenderBean sender;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SenderBean getSender() {
        return sender;
    }

    public void setSender(SenderBean sender) {
        this.sender = sender;
    }
}
