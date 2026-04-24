package com.sumankumar.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ChatRoomActivity extends AppCompatActivity {
    private Toolbar toolbar_id;
    private FloatingActionButton sendMsg_fab_id;
    private TextInputLayout type_TIL_ID;
    private TextInputEditText type_TIET_ID;
    private LinearLayout scrollView_id;
    private ScrollView scrollView_id1;
    private  long  msgCount;
    private Intent intent;
    private Bundle onStartBundle;
    private Bundle bundle;
    private boolean bDbUpdate;
    private SharedPreferences sp,sp1;
    private SharedPreferences.Editor spe,spe1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        toolbar_id=(Toolbar) findViewById(R.id.chat_room_toolbar);
        setSupportActionBar(toolbar_id);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        sendMsg_fab_id=(FloatingActionButton) findViewById(R.id.send);
        type_TIET_ID=(TextInputEditText) findViewById(R.id.type_msg_TIET);
        type_TIL_ID=(TextInputLayout) findViewById(R.id.type_msg_TIL);
        scrollView_id=(LinearLayout) findViewById(R.id.chat_scroll);
        scrollView_id1=(ScrollView) findViewById(R.id.chat_scroll1);
        msgCount=0;
        bDbUpdate=false;
        intent=getIntent();
        onStartBundle=savedInstanceState;
        sp=getSharedPreferences("last_msg_key",Context.MODE_PRIVATE);
        sp1=getSharedPreferences("realTimeMsg_date",Context.MODE_PRIVATE);
        spe=sp.edit();
        spe1=sp1.edit();
        receiveMsgFromFirebaseDb();
    }


    @Override
    protected void onStart() {
        super.onStart();
         bundle=intent.getExtras();
        toolbar_id.setTitle(bundle.getString("name"));
        retrieveMsgFromLocalDb();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onSendButtonPressed();
        inputTextView();
        realTimeUpdate();
    }
    private void retrieveMsgFromLocalDb(){
        bundle=intent.getExtras();
        String u=bundle.getString("uid");
        ArrayList<ChatMsgData> ald=(new MessageDb(this)).getMsgData(u);
        if(ald!=null) {
            for (ChatMsgData d : ald) {
                setMsgHierarchy(d.getChatMsg(), d.getSend_receive());
            }
        }

    }
    private void setMsgHierarchy(String msg,String sr){
        if(msg!=null && sr!=null) {
            TextView tv = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if(sr.trim().compareToIgnoreCase("S")==0){
                params.setMargins(0, 5, 30, 2);
                params.gravity = Gravity.RIGHT;
                tv.setBackground(getDrawable(R.drawable.textview_boarder_right));
            }
            else if(sr.trim().compareToIgnoreCase("R")==0){
                params.setMargins(30, 5, 0, 2);
                params.gravity = Gravity.LEFT;
                tv.setBackground(getDrawable(R.drawable.textview_boarder_left));

            }
            tv.setLayoutParams(params);
            tv.setMaxWidth(550);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setPadding(20, 5, 20, 5);
            tv.setText(msg);
            scrollView_id.addView(tv);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView_id1.smoothScrollTo(0, scrollView_id.getHeight() + scrollView_id.getPaddingBottom());
                }
            }, 1);
        }
        else
            return;
    }
    private void realTimeTextView(String msg,String d){

        if(msg!=null) {
            spe1.putString("dt",d);
            spe1.commit();

            TextView tv = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(30, 5, 0, 2);
            params.gravity = Gravity.LEFT;
            tv.setBackground(getDrawable(R.drawable.textview_boarder_left));
            tv.setLayoutParams(params);
            tv.setMaxWidth(550);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(18);
            tv.setTextColor(getResources().getColor(R.color.black));
            tv.setPadding(20, 5, 20, 5);
            tv.setText(msg);
            scrollView_id.addView(tv);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView_id1.smoothScrollTo(0, scrollView_id.getHeight() + scrollView_id.getPaddingBottom());
                    //playSound();
                }
            }, 2);
        }
    }

    private void inputTextView(){
        type_TIET_ID.setClickable(true);
        type_TIET_ID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (type_TIET_ID.hasFocus()) {
                    if (imm.isActive()) {
                        new Handler().postDelayed(() -> scrollView_id1.smoothScrollTo(0, scrollView_id.getHeight() + scrollView_id.getPaddingBottom()), 1);
                    }
                }
            }
        });

        type_TIET_ID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    type_TIL_ID.setErrorEnabled(false);
                }
            }
        });

        type_TIET_ID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().length()>0){
                    type_TIL_ID.setErrorEnabled(false);
                if(editable.toString().charAt(editable.toString().length()-1)=='\n')
                    scrollView_id1.smoothScrollTo(0,scrollView_id1.getHeight());
                }
            }
        });
    }
    private void onSendButtonPressed(){

        sendMsg_fab_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
                String date=df.format(new Date());
                createTextView();
                DatabaseReference databaseRef=FirebaseDatabase.getInstance().getReference("RDBStore");
                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                if(getMessage()!=null) {
                    String id=bundle.getString("uid");
                    databaseRef.child(id)
                            .child(user.getUid())
                            .child(date).setValue(getMessage());
                    MessageDb db=new MessageDb(getApplicationContext(),id);
                    addMsgToDbQueueS(db,id,"S",getMessage(),date);
                 //playSound();

                    type_TIET_ID.setText(null);
                }
            }
        });

    }
    private String getMessage(){
        String msg=type_TIET_ID.getText().toString();
        if(msg.isEmpty()) {
            type_TIL_ID.setError("enter your message!!");
            return null;
        }
        return msg;
    }
    private void realTimeUpdate(){
        FirebaseUser fUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db_ref=  FirebaseDatabase.getInstance().getReference("RDBStore").child(fUser.getUid());
        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String msg=null;
                String date=null;
                boolean bT=false;
                for (DataSnapshot root:snapshot.getChildren()) {
                    if(root.getKey().trim().compareToIgnoreCase("email")!=0) {
                      if(intent.getExtras().getString("uid").compareTo(root.getKey())==0) {
                          bT=false;
                          for (DataSnapshot child : root.getChildren()) {
                              if (child.getKey().trim().compareToIgnoreCase("email") != 0) {
                                  msg = child.getValue().toString();
                                  date=child.getKey();
                              }
                          }

                              realTimeTextView(msg,date);


                      }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void receiveMsgFromFirebaseDb(){
        FirebaseUser fUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference db_ref=  FirebaseDatabase.getInstance().getReference("RDBStore").child(fUser.getUid());
        MessageDb ldb=new MessageDb(this);

        db_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id=null;
                String msg=null;
                String d=null;
                boolean bTemp=false;
                for (DataSnapshot root:snapshot.getChildren()) {
                    if(root.getKey().trim().compareToIgnoreCase("email")!=0) {
                        id=root.getKey();
                        bTemp=false;
                        for (DataSnapshot child : root.getChildren()) {
                            if (child.getKey().trim().compareToIgnoreCase("email") != 0){
                                msg=child.getValue().toString();
                                d=child.getKey();
                                if(root.getChildrenCount()<=2)
                                    bTemp=true;

                                if(bTemp){
                                    addMsgToDbQueueR(ldb,id,"R",msg,d);
                                }
                                if(sp.getString("datetime","").compareTo(d)==0){
                                    bTemp=true;
                                }
                            }
                        }
                        //addMsgToDbQueueR(ldb,id,"R",msg,d);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }
    private void addMsgToDbQueueR(MessageDb ldb,final String uid,final String sr,final String msg,final String date) {
            ldb.setMsgData(uid, sr, msg, date);
            spe.putString("datetime",date);
            spe.commit();
    }
    private void addMsgToDbQueueS(final MessageDb msgD,final String uid,final String sr,final String msg,final String date) {
        msgD.setMsgData(uid, sr, msg, date);

    }
    private void laySound(){
        try{
            Uri not= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r=RingtoneManager.getRingtone(getApplicationContext(),not);
            r.play();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    private void createTextView(){
        TextView tv=new TextView(getApplicationContext());
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,5,30,2);
        params.gravity=Gravity.RIGHT;
        tv.setLayoutParams(params);
        tv.setMaxWidth(550);
        tv.setGravity(Gravity.CENTER);
        tv.setTextSize(18);
        tv.setBackground(getDrawable(R.drawable.textview_boarder_right));
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setPadding(20,5,20,5);
        if(type_TIET_ID.getText().toString().isEmpty()){
            type_TIL_ID.setError("Enter your message!!");
            return;
        }
        tv.setText(type_TIET_ID.getText().toString());
        scrollView_id.addView(tv);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView_id1.smoothScrollTo(0,scrollView_id.getHeight()+scrollView_id.getPaddingBottom());
            }
        },1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chatroom_menu,menu);
        return true;
    }

}