package com.masai.chatwithme.Activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.masai.chatwithme.R;
import com.masai.chatwithme.Users;

public class Registration extends AppCompatActivity {

    TextView mTvAlreadyHaveAcc;
    ImageView mIvImageLogo;
    EditText mEtName;
    EditText mEtEmail;
    EditText mEtPassword;
    EditText mEtConfirmPass;
    Button mBtnRegister;
    Uri imageuri;
    FirebaseAuth auth;
    String emailpattern="[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseDatabase database;
    FirebaseStorage storage;
    String imageURI;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initView();
        //changeStatusBarColor();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("please wait...");
        progressDialog.setCancelable(false);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        mTvAlreadyHaveAcc=findViewById(R.id.AlreadyHaveAcc);
        mTvAlreadyHaveAcc.setOnClickListener(v -> startActivity(new Intent(Registration.this,LoginScreen.class)));
        mEtName=findViewById(R.id.editTextName);
        mEtEmail=findViewById(R.id.emailRegistration);
        mEtPassword=findViewById(R.id.Password1);
        mEtConfirmPass=findViewById(R.id.ConfirmPassword1);
        mIvImageLogo=findViewById(R.id.ImageLogo);
        mBtnRegister=findViewById(R.id.RegisterButton);

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String name = mEtName.getText().toString();
                String email = mEtEmail.getText().toString();
                String password = mEtPassword.getText().toString();
                String confirmPassword = mEtConfirmPass.getText().toString();
                String status="Hey there Chat with me" ;


                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "Enter valid Details ", Toast.LENGTH_SHORT).show();
                } else if (!email.matches(emailpattern)) {
                    mEtEmail.setError("enter correct email");
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "enter valid email", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    progressDialog.dismiss();
                    Toast.makeText(Registration.this, "password doesNot match", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(Registration.this, "Enter large password", Toast.LENGTH_SHORT).show();
                }
                else
                    {
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull  Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DatabaseReference databaseReference = database.getReference().child("user").child(auth.getUid());
                                StorageReference storageReference = storage.getReference().child("upload").child(auth.getUid());
                                if (imageuri != null) {
                                    storageReference.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageURI = uri.toString();
                                                        Users users = new Users(auth.getUid(), name, email, imageURI,status);
                                                        databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    progressDialog.dismiss();
                                                                    startActivity(new Intent(Registration.this, Home.class));
                                                                    finish();
                                                                } else {
                                                                    Toast.makeText(Registration.this, "", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    imageURI = "https://firebasestorage.googleapis.com/v0/b/chatwithme-2e7ae.appspot.com/o/man.png?alt=media&token=c2ba1604-33da-47f2-aa3c-8bd7c26a4df3";
                                    Users users = new Users(auth.getUid(), name, email, imageURI,status);
                                    databaseReference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete( Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                startActivity(new Intent(Registration.this, Home.class));
                                                finish();
                                            } else {
                                                Toast.makeText(Registration.this, "Error in creating new User", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else
                            {
                                progressDialog.dismiss();
                                Toast.makeText(Registration.this, "something went wrong ", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }
        });

        mIvImageLogo.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,   Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==10){
            if (data!=null){
                imageuri=data.getData();
                mIvImageLogo.setImageURI(imageuri);
            }
        }
    }






//    private void changeStatusBarColor() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            ((Window) window).addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            window.setStatusBarColor(Color.TRANSPARENT);
//            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
//        }
//    }
//    public void onLoginClick(View view){
//        startActivity(new Intent(this,LoginScreen.class));
//        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
//
//    }



}
// imageURI = uri.toString()