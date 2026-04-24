package com.sumankumar.chatapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sumankumar.chatapp.localdatabase.LocalDatabase;
import com.sumankumar.chatapp.R;
import com.sumankumar.chatapp.signup.SignUpDataVerification;
import com.sumankumar.chatapp.signup.SignUpPage;

public class LoginPage extends AppCompatActivity {
    private TextView signup_btn_id;
    private TextInputLayout email_TIL_id;
    private TextInputLayout password_TIL_id;
    private static int pass_toggle_count;

    private TextInputEditText password_TIET_id;
    private TextInputEditText email_TIET_id;
    private MaterialButton login_btn;
    private TextView error_msg_id;
    private JLoginFirebaseAuth loginFirebaseAuth;
    private CheckBox rememberMeId;
    private LocalDatabase localDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        loginFirebaseAuth=new JLoginFirebaseAuth(LoginPage.this);
        pass_toggle_count=0;
        signup_btn_id=(TextView) findViewById(R.id.signup_tv);
        email_TIL_id=(TextInputLayout) findViewById(R.id.email_login_TIL);
        email_TIET_id=(TextInputEditText) findViewById(R.id.email_login_TIET);
        password_TIL_id=(TextInputLayout) findViewById(R.id.password_login_TIL);
        password_TIET_id=(TextInputEditText)findViewById(R.id.password_Login_TIET);
        login_btn=(MaterialButton) findViewById(R.id.login_btn);
        error_msg_id=(TextView) findViewById(R.id.error_msg);
        rememberMeId=(CheckBox) findViewById(R.id.remember_me);
        localDatabase=new LocalDatabase(this);



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null) {

            loginFirebaseAuth.update(user);
        }
        if(localDatabase.isSaveRememberMe()){
           String[] data= localDatabase.getEmailAndPassword();
           email_TIET_id.setText(data[0]);
           password_TIET_id.setText(data[1]);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        loginPageToSignupPage();
        textInputEditTextHandling();
        setLoginMaterialButton();


    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
        email_TIET_id.clearComposingText();
        password_TIET_id.clearComposingText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        email_TIET_id=null;
        email_TIL_id=null;
        password_TIET_id=null;
        password_TIL_id=null;


    }

    //move from login page to sign up page,if don't have an account.
    private void loginPageToSignupPage(){
        signup_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(LoginPage.this, SignUpPage.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

    }


    private void textInputEditTextHandling(){

        //email on focus change listener
        email_TIET_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(!b){
                    if(email_TIET_id.getText().toString().length()>0) {
                        if (!SignUpDataVerification.final_email_check(email_TIET_id.getText().toString())) {
                            email_TIL_id.setError("Invalid email address !");
                            error_msg_id.setText(null);
                        }
                    }
                    else {
                        email_TIL_id.setError("enter your email !");
                        error_msg_id.setText(null);
                    }
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
                        error_msg_id.setText(null);
                    } else {
                        email_TIL_id.setError("Invalid email !");
                        error_msg_id.setText(null);
                    }
                } else {
                    email_TIL_id.setError("enter your email !");
                    error_msg_id.setText(null);
                }
            }
        });

        password_TIET_id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    error_msg_id.setText(null);
                else
                    error_msg_id.setText(null);
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

    }

    //handling login button
    private void setLoginMaterialButton(){
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email=email_TIET_id.getText().toString().trim();
                String password=password_TIET_id.getText().toString();
                if(email.isEmpty()){
                    email_TIL_id.setError("enter your email!");
                    return ;
                }
                else if(password.isEmpty()){
                    password_TIL_id.setError("enter your password!");
                    return ;
                }
                else{

                    if(rememberMeId.isChecked())
                       loginFirebaseAuth.userVerification(email,password,true,localDatabase);
                    else
                        loginFirebaseAuth.userVerification(email,password,false,null);
                }
            }
        });
    }

}