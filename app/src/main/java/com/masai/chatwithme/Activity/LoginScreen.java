package com.masai.chatwithme.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.masai.chatwithme.R;

public class LoginScreen extends AppCompatActivity {

    TextView mTvNewUser;
    EditText mEtEmail;
    EditText mPassword;
    Button mBtnLogin;
    FirebaseAuth auth;
    String emailPattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);

        auth=FirebaseAuth.getInstance();
        mTvNewUser=findViewById(R.id.tvNewUser);
        mEtEmail=findViewById(R.id.editTextEmail);
        mPassword=findViewById(R.id.editTextPassword);
        mBtnLogin=findViewById(R.id.LoginButton);
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String email = mEtEmail.getText().toString();
                String password = mPassword.getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginScreen.this, "enter Valid Details", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailPattern)) {
                    progressDialog.dismiss();
                    mEtEmail.setError("invalid email");
                } else if (password.length() < 6) {
                    progressDialog.dismiss();
                    mPassword.setError("Invalid password");
                    Toast.makeText(LoginScreen.this, "pls enter valid password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginScreen.this, Home.class));

                            } else {
                                Toast.makeText(LoginScreen.this, "error in Login", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

            }
        });
        mTvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginScreen.this, Registration.class));
            }
        });
    }





//    public void onLoginClick(View View){
//        startActivity(new Intent(this,Registration.class));
//        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
//    }

    }
