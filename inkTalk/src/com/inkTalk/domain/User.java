package com.inkTalk.domain;

public class User {
    private int id;
    private String nickname;

    public User(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
