package com.sumankumar.chatapp;

public class ClientData {

    private String name;
    private String uid;
    private String email;
    private String joining_date_time;

    public ClientData(String name, String uid, String email, String joining_date_time) {
        this.name = name;
        this.uid = uid;
        this.email = email;
        this.joining_date_time = joining_date_time;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJoining_date_time() {
        return joining_date_time;
    }

    public void setJoining_date_time(String joining_date_time) {
        this.joining_date_time = joining_date_time;
    }
}
