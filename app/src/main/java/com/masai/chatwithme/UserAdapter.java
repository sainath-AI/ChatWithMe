package com.masai.chatwithme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masai.chatwithme.Activity.ChatActivity;
import com.masai.chatwithme.Activity.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.Viewholder> {

    Context home;
    ArrayList<Users> usersArrayList;

    public UserAdapter(Home home, ArrayList<Users> usersArrayList) {
        this.home=home;
        this.usersArrayList=usersArrayList;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
     View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userrow,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  UserAdapter.Viewholder holder, int position) {
        Users users=usersArrayList.get(position);
        holder.setdata(users);
    }

    @Override
    public int getItemCount() {
        return usersArrayList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder {
        CircleImageView userProfile;
        TextView userName;
        TextView userStatus;
        RelativeLayout relativeLayout;

        public Viewholder(@NonNull  View itemView) {
            super(itemView);
            userProfile=itemView.findViewById(R.id.UserImage);
            userName=itemView.findViewById(R.id.UserName);
            userStatus=itemView.findViewById(R.id.UserStatus);
            relativeLayout=itemView.findViewById(R.id.RelativeLayout);
        }

        public void setdata(Users users) {
            userName.setText(users.getName());
            userStatus.setText(users.getStatus());
            Picasso.get().load(users.imageUri).into(userProfile);
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(home, ChatActivity.class);
                    intent.putExtra("name",users.getName());
                    intent.putExtra("ReciverImage",users.getImageUri());
                    intent.putExtra("Uid",users.getUid());
                    home.startActivity(intent);
                }
            });



        }
    }
}
