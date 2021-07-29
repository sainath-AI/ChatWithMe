package com.masai.chatwithme.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.masai.chatwithme.MessagesAdapter;
import com.masai.chatwithme.Messages;
import com.masai.chatwithme.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    String ReciverImage,ReciverUid,ReciverName, SenderUid;
    CircleImageView profileimage;
    TextView reciverName;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    public static  String  sImage;
    public static  String  rImage;

    CardView SendBtn;
    EditText mEtMessage;

    String senderROOM,reciverROOM;

    RecyclerView recyclerView;

    ArrayList<Messages> messagesArrayList;

    MessagesAdapter messagesAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();


        profileimage=findViewById(R.id.ProfileImage);
        reciverName=findViewById(R.id.Recivername);

        ReciverName=getIntent().getStringExtra("name");
        ReciverImage=getIntent().getStringExtra("ReciverImage");
        ReciverUid=getIntent().getStringExtra("Uid");

        messagesArrayList=new ArrayList<>();

        SendBtn=findViewById(R.id.sendMessageBtn);
        mEtMessage=findViewById(R.id.editTextMessage);

        recyclerView=findViewById(R.id.messageRecyclerView);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(this,messagesArrayList);
        recyclerView.setAdapter(messagesAdapter);


        Picasso.get().load(ReciverImage).into(profileimage);
        reciverName.setText("" + ReciverName);

        SenderUid=firebaseAuth.getUid();

        senderROOM=SenderUid+ReciverUid;
        reciverROOM=ReciverUid+SenderUid;





       DatabaseReference reference = firebaseDatabase.getReference().child("user").child(firebaseAuth.getUid());
        DatabaseReference chatreference = firebaseDatabase.getReference().child("chats").child(senderROOM).child("messages");


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Messages messages=dataSnapshot.getValue(Messages.class);
                    messagesArrayList.add(messages);
                }
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
               sImage= snapshot.child("imageUri").getValue().toString();
               rImage=ReciverImage;

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });

        SendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=mEtMessage.getText().toString();
                if (message.isEmpty()){
                    Toast.makeText(ChatActivity.this, "enter message", Toast.LENGTH_SHORT).show();
                    return;
                }
                mEtMessage.setText("");
                Date date=new Date();

                Messages messages=new Messages(message,SenderUid,date.getTime());

                firebaseDatabase=FirebaseDatabase.getInstance();
                firebaseDatabase.getReference().child("chats")
                        .child(senderROOM)
                        .child("messages")
                        .push()
                        .setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseDatabase.getReference().child("chats")
                                .child(reciverROOM)
                                .child("messages")
                                .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull  Task<Void> task) {

                            }
                        });
                    }
                });
            }
        });
    }
}