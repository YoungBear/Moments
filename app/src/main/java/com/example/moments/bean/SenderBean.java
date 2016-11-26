package com.example.moments.bean;

/**
 * Created by bearyang on 2016/11/23.
 */

public class SenderBean {
    private String username;
    private String nick;
    private String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "SenderBean{" +
                "username='" + username + '\'' +
                '}';
    }
}
