package com.sumankumar.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sumankumar.chatapp.localdatabase.MessageDb;
import com.sumankumar.chatapp.localdatabase.ProfileDb;
import com.sumankumar.chatapp.login.LoginPage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private final static int SPLASH_SCREEN_TIME_OUT=2000;
    private String[] name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        name=new String[4];

        new Handler().postDelayed(() -> {
            Intent intent=new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
            finish();
        },SPLASH_SCREEN_TIME_OUT);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent=new Intent(MainActivity.this, LoginPage.class);
//                startActivity(intent);
//                finish();
//            }
//        },SPLASH_SCREEN_TIME_OUT);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //checkNotAddedChild();
    }

    private void checkNotAddedChild(){
        FirebaseUser fUser= FirebaseAuth.getInstance().getCurrentUser();
        if(fUser!=null) {
            DatabaseReference db_ref = FirebaseDatabase.getInstance().getReference("RDBStore").child(fUser.getUid());
            ProfileDb pdb = new ProfileDb(this);
            if (pdb != null) {
                ArrayList<ClientData> arl = pdb.getData();
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String date = df.format(new Date());

                db_ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String id = null;
                        String msg = null;
                        String d = null;
                        for (DataSnapshot root : snapshot.getChildren()) {
                            if (root.getKey().trim().compareToIgnoreCase("email") != 0) {
                                id = root.getKey();
                                for (ClientData c : arl) {
                                    if (!(c.getUid().compareTo(id) == 0)) {
                                        getFireStoreData(id);
                                        MessageDb msgd = new MessageDb(getApplicationContext(), id);
                                        pdb.addData(name[0], name[1], id, date);
                                        for (DataSnapshot child : root.getChildren())
                                            if (child.getKey().trim().compareToIgnoreCase("email") != 0) {
                                                msg = child.getValue().toString();
                                                d = child.getKey();
                                                msgd.setMsgData(id, "R", msg, d);
                                            }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        }
    }

    private void getFireStoreData(String uid){
        FirebaseFirestore fireStore=FirebaseFirestore.getInstance();
        fireStore.collection("user")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                if(uid.compareTo(doc.getId())==0){
                                    name[0]=doc.getString("name");
                                    name[1]=doc.getString("email");
                                }
                                // Log.d("TAG", document.getId() + " => " + document.getString("name"));
                            }
                        } else {

                            Log.w("error", "Error getting documents.", task.getException());
                        }
                    }
                });

    }
}