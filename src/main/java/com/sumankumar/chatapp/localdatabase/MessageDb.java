package com.sumankumar.chatapp.localdatabase;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.sumankumar.chatapp.ChatMsgData;
import com.sumankumar.chatapp.ClientData;

import java.util.ArrayList;

public class MessageDb extends SQLiteOpenHelper {

    private final static String DBNAME="message_Database.db";
    private final static int DB_VERSION=1;

    private Context context;
    private String tableName="";
    private final static String ID="id";
    private final static String MSG_QUEUE="msg_queue";
    private final static String DATE_TIME="data_time";
    private final static String SEND_RECEIVE="send_receive";



    public MessageDb(@Nullable Context context,String table_name) {
        super(context, DBNAME, null, DB_VERSION);
        this.context=context;
        tableName=table_name;
        onCreate(getWritableDatabase());
    }
    public MessageDb(Context context){
        super(context,DBNAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query="CREATE TABLE IF NOT EXISTS "+ tableName + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SEND_RECEIVE + " TEXT, "
                + MSG_QUEUE + " TEXT, "
                + DATE_TIME + " TEXT);";

        db.execSQL(query);
    }
    public void setMsgData(String table_uid,String sr,String msg,String dt){

            try {
                if (table_uid != null && sr != null && msg != null && dt != null) {
                    SQLiteDatabase db = getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(SEND_RECEIVE, sr);
                    values.put(MSG_QUEUE, msg);
                    values.put(DATE_TIME, dt);
                    db.insert(table_uid, null, values);
                    db.close();
                }
            } catch (SQLiteException e) {
                Log.d("insert error", "insert error :" + e.getMessage());
            }

    }

    public ArrayList<ChatMsgData> getMsgData(String tableN){
        ArrayList<ChatMsgData> msgData=null;
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + tableN, null);

            if (cursor.moveToFirst()) {
                msgData = new ArrayList<>();
                do {
                    msgData.add(new ChatMsgData(
                            cursor.getString(2),
                            cursor.getString(1),
                            cursor.getString(3)));
                } while (cursor.moveToNext());
            } else
                msgData = null;
        }catch (SQLiteException e){
            Log.e("sql error" ,e.getMessage());
        }


        return msgData;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
