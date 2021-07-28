package com.masai.chatwithme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReciverImage,ReciverUid,ReciverName;
    CircleImageView profileimage;
    TextView reciverName;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initview();






    }

    private void initview() {

        profileimage=findViewById(R.id.ProfileImage);
        reciverName=findViewById(R.id.Recivername);
        ReciverName=getIntent().getStringExtra("name");
        ReciverImage=getIntent().getStringExtra("ReciverImage");
        ReciverUid=getIntent().getStringExtra("Uid");


        Picasso.get().load(ReciverImage).into(profileimage);
        reciverName.setText("" + ReciverName);

    }
}