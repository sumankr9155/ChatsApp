package com.sumankumar.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.widget.Toolbar;

import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
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
import java.util.HashMap;
import java.util.Map;

public class HomePageActivity extends AppCompatActivity {

    private ListView listViewId;
    private Toolbar toolbar_id;
    private FloatingActionButton addNewUser_fab_id;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        listViewId=(ListView)findViewById(R.id.contact_listView);
        toolbar_id=(Toolbar) findViewById(R.id.chat_room_toolbar);
        setSupportActionBar(toolbar_id);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        addNewUser_fab_id=(FloatingActionButton) findViewById(R.id.add_new_user);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_page_menu,menu);
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.homePageSearch:
                Toast.makeText(getApplicationContext(),"search",Toast.LENGTH_SHORT).show();

                return true;
            case R.id.homePageLogout:
                FirebaseAuth.getInstance().signOut();
                Intent in=new Intent(this, LoginPage.class);
                startActivity(in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

       setDataInTheList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        addNewUser_fab_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               Intent in=new Intent(HomePageActivity.this,AddNewUser_ACT.class);
               startActivity(in);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private void setDataInTheList(){
       final ArrayList<Current_List_data> arrayList=new ArrayList<>();

       final ProfileDb db=new ProfileDb(this);

        ArrayList<ClientData> sqlData=db.getData();
        ArrayList<ChatMsgData> chatMsgData;
        String lastMsg="";
        String date="";

        if(sqlData.size()>0 && sqlData!=null) {
            for (ClientData cd : sqlData) {
                MessageDb ldb=new MessageDb(this);
                chatMsgData=ldb.getMsgData(cd.getUid());
                if(chatMsgData!=null && chatMsgData.size()>0){
                        lastMsg=chatMsgData.get(chatMsgData.size()-1).getChatMsg();
                        String date_time=chatMsgData.get(chatMsgData.size()-1).getDate_time();
                        date=date_time.substring(0,date_time.indexOf(" ")).replace('-','/');
                }
                arrayList.add(new Current_List_data(cd.getName(),cd.getUid(), R.drawable.ic_baseline_person_24, lastMsg, date));
            }
        }
        ContactArrayAdaptar contactArrayAdaptar=new ContactArrayAdaptar(this,arrayList);
        listViewId.setAdapter(contactArrayAdaptar);
       listViewId.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
             Intent intent=new Intent(getApplicationContext(),ChatRoomActivity.class);
               ArrayAdapter<Current_List_data> aacld= (ArrayAdapter<Current_List_data>) adapterView.getAdapter();
               Current_List_data cld=aacld.getItem(i);
             Bundle bun=new Bundle();
             bun.putString("name",cld.getName());
             bun.putString("uid", cld.getUid());
             intent.putExtras(bun);
             startActivity(intent);

           }
      });
    }

}