package com.sumankumar.chatapp.signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sumankumar.chatapp.HomePageActivity;
import com.sumankumar.chatapp.MainActivity;
import com.sumankumar.chatapp.ProgressBar;
import com.sumankumar.chatapp.login.LoginPage;
import com.sumankumar.chatapp.R;

import java.util.HashMap;
import java.util.Map;

public class SignUpPage extends AppCompatActivity {

    private TextView login_btn_id;
    private static int pass_toggle_count;
    static int num;
    boolean b;
    private static final int correctness=0;
    private MaterialButton signup_btn;

    //text input layout id
    private TextInputLayout first_name_TIL_id;
    private TextInputLayout last_name_TIL_id;
    private TextInputLayout email_TIL_id;
    private TextInputLayout phone_TIL_id;
    private TextInputLayout password_TIL_id;

    //text input edittext id
    private TextInputEditText first_name_TIEL_id;
    private TextInputEditText last_name_TIEL_id;
    private TextInputEditText email_TIET_id;
    private TextInputEditText phone_TIET_id;
    private TextInputEditText password_TIET_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        login_btn_id=(TextView) findViewById(R.id.login_tv);

        pass_toggle_count=0;
        num +=1;
        b = true;

        //first name field
        first_name_TIL_id = (TextInputLayout) findViewById(R.id.first_name_TIL);
        first_name_TIEL_id= (TextInputEditText) findViewById(R.id.first_name_TIET);
        //last name field
        last_name_TIL_id = (TextInputLayout) findViewById(R.id.last_name_TIL);
        last_name_TIEL_id= (TextInputEditText) findViewById(R.id.last_name_TIET);
        //emial field
        email_TIL_id = (TextInputLayout) findViewById(R.id.email_signup_TIL);
        email_TIET_id= (TextInputEditText) findViewById(R.id.email_signup_TIET);
        //phone field
        phone_TIL_id= (TextInputLayout) findViewById(R.id.phone_signup_TIL);
        phone_TIET_id = (TextInputEditText) findViewById(R.id.phone_signup_TIET);
        //password field
        password_TIET_id= (TextInputEditText) findViewById(R.id.password_signup_TIET);
        password_TIL_id = (TextInputLayout) findViewById(R.id.password_signup_TIL);
        //sign up button
        signup_btn=(MaterialButton) findViewById(R.id.signup_btn);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //sign up page to login page transition function called
        signupPageToLoginPage();
        //data verification function called
        dataVerification();
        setSignupMaterialButton();
    }

    // make calls to SignUpDataVerification class member  to verify data
    private void dataVerification(){
        //first name on focus changed listener
        first_name_TIEL_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(first_name_TIEL_id.getText().toString().length()==0) {
                        first_name_TIL_id.setError("enter your first name !");
                    }
                    else{
                        first_name_TIL_id.setErrorEnabled(false);
                        first_name_TIL_id.setError(null);
                    }
                }
            }
        });

        //name text changed listener
        first_name_TIEL_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                //length should not be zero
                if (name.length() == 0) {
                    first_name_TIL_id.setError("enter your first name !");
                }
                else {
                    //length should be maximum of 25 character
                    if (name.length() > 30) {
                        first_name_TIL_id.setError("name too large !");
                    } else {
                        first_name_TIL_id.setErrorEnabled(false);
                        first_name_TIL_id.setError(null);
                    }
                }
            }
        });

        //last name on focus changed listener
        last_name_TIEL_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(last_name_TIEL_id.getText().toString().length()==0) {
                        last_name_TIL_id.setError("enter your last name !");
                    }
                    else{
                        last_name_TIL_id.setErrorEnabled(false);
                        last_name_TIL_id.setError(null);
                    }
                }
            }
        });

        //last name text changed listener
        last_name_TIEL_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String name = editable.toString();
                //length should not be zero
                if (name.length() == 0) {
                    last_name_TIL_id.setError("enter your last name !");
                }
                else {
                    //length should be maximum of 25 character
                    if (name.length() > 30) {
                        last_name_TIL_id.setError("name too large !");
                    } else {
                        last_name_TIL_id.setErrorEnabled(false);
                        last_name_TIL_id.setError(null);
                    }
                }
            }
        });

        //email on focus change listener
        email_TIET_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(email_TIET_id.getText().toString().length()>0) {
                        if (!SignUpDataVerification.final_email_check(email_TIET_id.getText().toString())) {
                            email_TIL_id.setError("Invalid email address !");
                        }
                    }
                    else
                        email_TIL_id.setError("enter your email !");
                }
            }
        });

        //email text changed listener
        email_TIET_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String email = editable.toString().trim();
                if (email.length() > 0) {

                    if (SignUpDataVerification.email(editable.toString())) {
                        email_TIL_id.setError(null);
                        email_TIL_id.setErrorEnabled(false);
                    } else {
                        email_TIL_id.setError("Invalid email !");
                    }
                } else {
                    email_TIL_id.setError("enter your email !");
                }
            }
        });


        //cursor status
        phone_TIET_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    phone_TIL_id.setCounterEnabled(true);
                    phone_TIL_id.setPrefixText("+91");
                } else {
                    phone_TIL_id.setCounterEnabled(false);
                    if(!(phone_TIET_id.getText().length()==10)){
                        phone_TIL_id.setError("Invalid phone number !");
                    }
                    else {
                       phone_TIL_id.setError(null);
                       phone_TIL_id.setErrorEnabled(false);
                    }

                }
            }
        });
        //phone text changed listener
        phone_TIET_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String phone = editable.toString().trim();
                if(phone.length()>0){
                    if (phone.length() < 10 || phone.length() > 10) {
                        phone_TIL_id.setError("Invalid phone number !");
                    } else {
                        phone_TIL_id.setError(null);
                        phone_TIL_id.setErrorEnabled(false);
                    }
                }
                else {
                    phone_TIL_id.setError("enter your phone number !");
                }
            }
        });

        //password on focus changer listener
        password_TIET_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                int len=password_TIET_id.getText().length();

                if(!b){
                    if(len>0) {
                        if (!SignUpDataVerification.password(password_TIET_id.getText().toString())) {
                            password_TIL_id.setError("should contain at least one uppercase,lowercase,number and special character !");
                        } else {
                            password_TIL_id.setError(null);
                            password_TIL_id.setErrorEnabled(false);
                        }
                    }
                    else{
                        password_TIL_id.setError("enter your password !");
                    }
                }
            }
        });
        //password toggle
        password_TIL_id.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              if(pass_toggle_count==1) {
                    password_TIL_id.setEndIconDrawable(R.drawable.ic_baseline_visibility_off_24);
                    password_TIET_id.setTransformationMethod(new PasswordTransformationMethod());
                    password_TIET_id.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    password_TIET_id.setSelection(password_TIET_id.length());
                    pass_toggle_count=0;
                }
                else {
                   password_TIL_id.setEndIconDrawable(R.drawable.ic_baseline_visibility_24);
                   password_TIET_id.setTransformationMethod(null);
                   password_TIET_id.setInputType(InputType.TYPE_CLASS_TEXT);
                   password_TIET_id.setSelection(password_TIET_id.length());
                   pass_toggle_count=1;
                }
            }
        });
        password_TIET_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                String pass=editable.toString();

                if(pass.length()==0){
                    password_TIL_id.setError("enter your password !");
                }
                else {
                    if (pass.length() > 8 && pass.length() < password_TIL_id.getCounterMaxLength()) {
                        password_TIL_id.setError(null);
                        password_TIL_id.setErrorEnabled(false);
                    } else {
                        password_TIL_id.setError("Minimum length should be greater than or equal to 8 !");
                    }
                }
            }
        });
    }


    //Move from sign up page to login page,if have an account.
    public void signupPageToLoginPage(){
        login_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignUpPage.this, LoginPage.class);
                  intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
    }
    //handling sign up button
    private void setSignupMaterialButton(){
        ProgressBar pb=new ProgressBar(this);
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        pb.startDialog();
                        boolean check=false;
                String fname=first_name_TIEL_id.getText().toString().trim();
                if(fname.isEmpty()){
                   first_name_TIL_id.setError("enter your data!");
                   check=true;
                    }
                String lname=last_name_TIEL_id.getText().toString().trim();
                if(lname.isEmpty()){
                    last_name_TIL_id.setError("enter your data!");
                    check=true;}
                String phone= phone_TIET_id.getText().toString().trim();
                if(phone.isEmpty()){
                    phone_TIL_id.setError("enter your data!");
                   check=true;
                }
                String email=email_TIET_id.getText().toString().trim();
                if(email.isEmpty()){
                    email_TIL_id.setError("enter your data!");
                   check=true;
                }
                String pass=password_TIET_id.getText().toString();
                if(pass.isEmpty()){
                    password_TIL_id.setError("enter your data!");
                    check=true;
                }else{
                    if (!SignUpDataVerification.password(password_TIET_id.getText().toString()))
                        password_TIL_id.setError("should contain at least one uppercase,lowercase,number and special character !");
                }
                if(check){
                    return;
                }
                if(first_name_TIL_id.isErrorEnabled())
                    return;
                if(last_name_TIL_id.isErrorEnabled())
                    return;
                if(phone_TIL_id.isErrorEnabled())
                    return;
                if(email_TIL_id.isErrorEnabled())
                    return;
                if(password_TIL_id.isErrorEnabled())
                    return;

                String fullName=fname+" "+lname;


                FirebaseAuth mauth=FirebaseAuth.getInstance();
                //FirebaseUser user=mauth.getCurrentUser();
                mauth.createUserWithEmailAndPassword(email,pass)
                        .addOnCompleteListener(SignUpPage.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    SignUpData userData=new SignUpData(fullName,phone,email,pass);


                                    FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
                                    //firestore
                                    FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                                  // Map<String,Object> user=new HashMap<>();
                                  // user.put("First name","Suman");
                                  // user.put("Last name","kumar");
                                    firebaseFirestore.collection("user")
                                            .add(userData)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                   // Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                    Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                   // Log.w(TAG, "Error adding document", e);
                                                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                    //realtime data base
                                    FirebaseDatabase fd=FirebaseDatabase.getInstance();
                                    DatabaseReference dr=fd.getReference("RDBStore");
                                    dr.child(firebaseUser.getUid())
                                            .child("email").setValue(userData.getEmail());


                                    Intent intent1=new Intent(getApplicationContext(), HomePageActivity.class);

                                    startActivity(intent1);
                                    pb.dismissDialog();
                                }
                                else {
                                  //  Log.w("hell","fail!!",task.getException());
                                    Toast.makeText(getApplicationContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    pb.dismissDialog();
                                }
                            }
                        }).addOnFailureListener(SignUpPage.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                pb.dismissDialog();
                            }
                        });

            }
        });
    }



}