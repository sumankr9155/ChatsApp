package com.sumankumar.chatapp.login;

import android.app.Activity;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.sumankumar.chatapp.HomePageActivity;
import com.sumankumar.chatapp.localdatabase.LocalDatabase;
import com.sumankumar.chatapp.ProgressBar;
import com.sumankumar.chatapp.R;

public class JLoginFirebaseAuth {

    private final FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference ref;
    private Activity context;
    private TextView error_msg_id;

    public JLoginFirebaseAuth(){
        mAuth=FirebaseAuth.getInstance();

    }
    public JLoginFirebaseAuth(Activity context) {
        this.context=context;
        error_msg_id=(TextView)context.findViewById(R.id.error_msg);
        mAuth=FirebaseAuth.getInstance();

    }



    public void userVerification(final String email, final String password, final boolean bRemember, final LocalDatabase localDb){
        ProgressBar pb=new ProgressBar(context);
       error_msg_id.setText(null);
       pb.startDialog();
           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                       pb.dismissDialog();

                       //save data in the local database;
                        if(bRemember){
                            localDb.saveEmailAndPassword(email,password);
                        }
                       Toast.makeText(context,"login Successful",Toast.LENGTH_LONG).show();
                       user=mAuth.getCurrentUser();
                       context.finish();
                       updateUI(user);
                    }
                    else{
                        pb.dismissDialog();
                        error_msg_id.setText("Invalid user name or password!");
                        Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            });
        }


        public void update(FirebaseUser u){
        context.finish();
          updateUI(u);
        }
        private void updateUI(FirebaseUser u){
            context.startActivity(new Intent(context,HomePageActivity.class));
        }
}
