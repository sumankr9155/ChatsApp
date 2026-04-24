package com.sumankumar.chatapp;

import android.content.Context;

import java.util.ArrayList;

public class ChatMsgData {

    private String chatMsg;
    private String send_receive;
    private String date_time;



    public ChatMsgData(String chatMsg, String send_receive, String date_time) {
        this.chatMsg = chatMsg;
        this.send_receive = send_receive;
        this.date_time = date_time;
    }

    public String getChatMsg() {
        return chatMsg;
    }

    public void setChatMsg(String chatMsg) {
        this.chatMsg = chatMsg;
    }

    public String getSend_receive() {
        return send_receive;
    }

    public void setSend_receive(String send_receive) {
        this.send_receive = send_receive;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
