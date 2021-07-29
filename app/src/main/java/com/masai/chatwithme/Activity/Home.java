package com.masai.chatwithme.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masai.chatwithme.R;
import com.masai.chatwithme.UserAdapter;
import com.masai.chatwithme.Users;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView recyclerView;
    UserAdapter adapter;
    FirebaseDatabase database;
    ArrayList<Users> usersArrayList;
    ImageView mIvLogout;
    Button mBtnYes;
    Button mBtnNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        usersArrayList=new ArrayList<>();

        DatabaseReference reference=database.getReference().child("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Users users=dataSnapshot.getValue(Users.class);
                    usersArrayList.add(users);
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
        mIvLogout=findViewById(R.id.logout);
        recyclerView=findViewById(R.id.RecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new UserAdapter(this,usersArrayList);
        recyclerView.setAdapter(adapter);

        mIvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog  dialog=new Dialog(Home.this,R.style.Dialog);
               dialog.setContentView(R.layout.dialog_layout);

                mBtnYes=dialog.findViewById(R.id.Yes);
                mBtnNo=dialog.findViewById(R.id.No);

                mBtnYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Home.this,Registration.class));
                    }
                });
                mBtnNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });



        if (auth.getCurrentUser()==null){
            startActivity(new Intent(Home.this,Registration.class));
        }
    }
}