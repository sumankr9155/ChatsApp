package com.sumankumar.chatapp.localdatabase;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.internal.ConnectionTelemetryConfiguration;
import com.sumankumar.chatapp.signup.SignUpData;

public class LocalDatabase {
   private Context context;
   private SharedPreferences loginPref;
   private SharedPreferences.Editor loginPrefEditor;
   private boolean  bsave;
   private final static String  LOGIN_SAVE="SAVE_LOGIN";

   //profile details db
    private SharedPreferences profileSP;
    private SharedPreferences.Editor profileSPE;
    private final static String PROFILE_DB="PROFILE_DB";
    private final static String PROFILE_IMAGE="IMAGE";
    private final static String NAME="NAME";
    private final static String PHONE="PHONE_NUMBER";
    private final static String EMAIL="EMAIL";

    public LocalDatabase(){}

   public LocalDatabase(final Context context){
       this.context=context;
       loginPref=context.getSharedPreferences(LOGIN_SAVE,Context.MODE_PRIVATE);
       loginPrefEditor=loginPref.edit();
       //my profile db
       profileSP=context.getSharedPreferences(PROFILE_DB,Context.MODE_PRIVATE);
       profileSPE=profileSP.edit();
   }
   public void saveProfileDetails(final SignUpData data){


   }
   public void getProfileDetails(){

   }




   public boolean isSaveRememberMe(){
       return loginPref.getBoolean("bSaveLogin", false);
   }
   public void saveEmailAndPassword(final String email,final String password){
       loginPrefEditor.putBoolean("bSaveLogin",true);
       loginPrefEditor.putString("Email",email);
       loginPrefEditor.putString("Password",password);
       loginPrefEditor.commit();
   }

   public final String[] getEmailAndPassword(){
       String[] data={loginPref.getString("Email",""),loginPref.getString("Password","")};
       return data;
   }


}
