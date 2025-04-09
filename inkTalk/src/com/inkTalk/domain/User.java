package com.inkTalk.domain;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable{
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
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(nickname, user.nickname); // username으로 비교
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname); // username을 기준으로 해시값 생성
    }

}
