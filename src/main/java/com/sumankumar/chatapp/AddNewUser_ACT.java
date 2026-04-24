package com.sumankumar.chatapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sumankumar.chatapp.localdatabase.MessageDb;
import com.sumankumar.chatapp.localdatabase.ProfileDb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class AddNewUser_ACT extends AppCompatActivity {

    private TextInputLayout new_user_name_TIL_id;
    private TextInputEditText new_user_name_TIET_id;

    private TextInputLayout new_user_email_TIL_id;
    private TextInputEditText new_user_email_TIET_id;
    private Button submit_btn_id;
    private boolean b;
    private String uid;
    ProfileDb dbHandler;
    private DatabaseReference dr;
    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
        //name id
       new_user_name_TIL_id=(TextInputLayout)findViewById(R.id.new_user_name_TIL);
       new_user_name_TIET_id=(TextInputEditText) findViewById(R.id.new_user_name_TIET);
        //email id
        new_user_email_TIL_id=(TextInputLayout)findViewById(R.id.new_user_email_TIL);
        new_user_email_TIET_id=(TextInputEditText) findViewById(R.id.new_user_email_TIET);
        //button
        submit_btn_id=(Button) findViewById(R.id.new_user_btn);
        uid=null;
        b=false;
        dr=  FirebaseDatabase.getInstance().getReference("RDBStore");
       dbHandler= new ProfileDb(this);
       email=null;
    }


    @Override
    protected void onResume() {
        super.onResume();
        onSubmitButtonClicked();
    }
    final ProgressBar pb=new ProgressBar(this);
    private void onSubmitButtonClicked(){


        submit_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // for checking internet connection
                ConnectivityManager connect=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isConnected=(connect.getNetworkInfo(connect.TYPE_MOBILE).getState()== NetworkInfo.State.CONNECTED||
                        connect.getNetworkInfo(connect.TYPE_WIFI).getState()==NetworkInfo.State.CONNECTED);
                if(!isConnected){
                    Toast.makeText(getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(isEmpty())
                    return;
                pb.startDialog();
                String name=new_user_name_TIET_id.getText().toString().trim();
                email=new_user_email_TIET_id.getText().toString().trim();
                searchUser(email,name);

            }
        });
    }
    private boolean isEmpty(){
        int i=0;
        if(new_user_name_TIET_id.getText().toString().isEmpty()) {
            new_user_name_TIL_id.setErrorEnabled(true);
            new_user_name_TIL_id.setError("enter your name!!");
            i++;
        }
        if(new_user_email_TIET_id.getText().toString().isEmpty()) {
            new_user_email_TIL_id.setErrorEnabled(true);
            new_user_email_TIL_id.setError("enter your email!!");
            i++;
        }
        if(i==1)
            return true;
        else if(i==2)
            return true;
        else
            return false;
    }


    private void searchUser(String email,String name){
        b=false;
        uid=null;

        dr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot snapshot1:snapshot.getChildren()){
                    String temp_email=snapshot1.child("email").getValue().toString().trim();
                    if(temp_email.compareToIgnoreCase(email)==0) {
                        uid=snapshot1.getKey();
                        b = true;
                        updateDatabase(uid,temp_email);
                        return;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                pb.dismissDialog();

                if(b) {
                    //date and time
                    DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a", Locale.getDefault());
                    String date=df.format(new Date());

                    //sqlite database call
                    dbHandler.addData(name,email,uid,date);
                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(AddNewUser_ACT.this,HomePageActivity.class);
                    startActivity(in);
                }
                else
                    Toast.makeText(getApplicationContext(),"User not found!!",Toast.LENGTH_SHORT).show();
            }
        },4000);
    }
    private void updateDatabase(String uid,String clientEmail){
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        String myem=firebaseUser.getEmail();
        dr.child(firebaseUser.getUid()).child(uid).child("email").setValue(clientEmail);
        dr.child(uid).child(firebaseUser.getUid()).child("email").setValue(myem);
        new MessageDb(this,uid);
    }

}