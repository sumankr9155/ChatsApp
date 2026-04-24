package com.sumankumar.chatapp.localdatabase;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sumankumar.chatapp.ClientData;
import com.sumankumar.chatapp.Current_List_data;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ProfileDb extends SQLiteOpenHelper {
    private final static String DBNAME="SQLITE_DATABASE.db";
    private final static int DB_VERSION=1;

//    private final static String TABLE_1="PERSONAL_DETAILS";
//    private final static String PROFILE_IMAGE="IMAGE";
//    private final static String NAME="NAME";
//    private final static String PHONE="PHONE_NUMBER";

    private final static String TABLE_2="CONTACTS_DETAILS";
    private final static String ID_COL="ID";
    private final static String CONTACT_NAME="Name";
    private final static String CONTACT_UID="Uid";
    private final static String CONTACT_EMAIL="Email";
    private final static String JOINING_DATE="Date";
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context context;

    public ProfileDb(@Nullable Context context) {
        super(context, DBNAME, null, DB_VERSION);
        this.context=context;
        pref=context.getSharedPreferences("list_of_clients",Context.MODE_PRIVATE);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS "+ TABLE_2 + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CONTACT_NAME + " TEXT, "
                + CONTACT_UID + " TEXT, "
                + CONTACT_EMAIL + " TEXT, "
                + JOINING_DATE + " TEXT)";
        db.execSQL(query);
    }

    public void addData(String n,String e,String uid,String d){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(CONTACT_NAME,n);
        values.put(CONTACT_UID,uid);
        values.put(CONTACT_EMAIL,e);
        values.put(JOINING_DATE,d);
       db.insert(TABLE_2,null,values);
        db.close();
        Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
    }
  public ArrayList<ClientData> getData(){
        SQLiteDatabase db=getReadableDatabase();
      Cursor cursor=db.rawQuery("SELECT * FROM "+TABLE_2,null);
      ArrayList<ClientData> dataList=new ArrayList<>();
      if(cursor.moveToFirst()){
        do {
              dataList.add(new ClientData(
                      cursor.getString(1),
                      cursor.getString(2),
                      cursor.getString(3),
                      cursor.getString(4)));
        }while(cursor.moveToNext());
      }
      return dataList;
  }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_2);
        onCreate(db);

    }
}
