package com.sumankumar.chatapp;

import android.media.Image;

public class Current_List_data {
    private String name;
    private String uid;
    private int profileImgId;
    private String last_msg;
    private String last_msg_date;


    public Current_List_data(String name,String uid, int profileImgId, String last_msg, String last_msg_date) {
        this.name = name;
        this.profileImgId = profileImgId;
        this.last_msg = last_msg;
        this.last_msg_date = last_msg_date;
        this.uid=uid;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getProfileImg() {
        return profileImgId;
    }

    public void setProfileImg(Image profileImg) {
        this.profileImgId = profileImgId;
    }

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public String getLast_msg_date() {
        return last_msg_date;
    }

    public void setLast_msg_date(String last_msg_date) {
        this.last_msg_date = last_msg_date;
    }


}
