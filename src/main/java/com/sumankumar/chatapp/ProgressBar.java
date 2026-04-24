package com.sumankumar.chatapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class ProgressBar{
    private final Activity activity;
    private AlertDialog alertDialog;
    AlertDialog.Builder builder;

    public ProgressBar(Activity act){
        activity=act;
    }

   public void  startDialog(){
        builder=new AlertDialog.Builder(activity,R.style.customDialog);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progressbar,null));
        builder.setCancelable(false);
        alertDialog=builder.create();
        alertDialog.show();
    }
    public void dismissDialog(){
        alertDialog.dismiss();
    }
    public void setMessages(String msg){
        builder.setMessage(msg);

    }

}
