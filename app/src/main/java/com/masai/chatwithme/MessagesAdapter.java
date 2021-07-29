package com.masai.chatwithme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.masai.chatwithme.Activity.ChatActivity.rImage;
import static com.masai.chatwithme.Activity.ChatActivity.sImage;

public class MessagesAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<Messages> messagesArrayList;
    int ITEM_SEND=1;
    int ITEM_RECIVE=2;

    public MessagesAdapter(Context context, ArrayList<Messages> messagesArrayList) {
        this.context = context;
        this.messagesArrayList = messagesArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
       if (viewType==ITEM_SEND){
           View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item,parent,false);
           return new SenderViewholder(view);

       }else {
           View view = LayoutInflater.from(context).inflate(R.layout.reciver_layout_item,parent,false);
           return new ReciverViewholder(view);

       }

    }

    @Override
    public void onBindViewHolder(@NonNull  RecyclerView.ViewHolder holder, int position) {
        Messages messages=messagesArrayList.get(position);
        if (holder.getClass()==SenderViewholder.class){
            SenderViewholder viewholder=(SenderViewholder) holder;
            viewholder.txtmessage.setText(messages.getMessage());
            Picasso.get().load(sImage).into(viewholder.circleImageView);
        }else{
            ReciverViewholder viewholder1=(ReciverViewholder) holder;
            viewholder1.txtmessage.setText(messages.getMessage());
            Picasso.get().load(rImage).into(viewholder1.circleImageView);
        }

    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages=messagesArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderid()))
        {
            return ITEM_SEND;
        }else
        {
            return ITEM_RECIVE;
        }
    }

    class SenderViewholder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtmessage;


        public SenderViewholder(@NonNull  View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.ProfileImage);
            txtmessage=itemView.findViewById(R.id.txtmessageSend);


        }
    }


    class ReciverViewholder extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView txtmessage;
        public ReciverViewholder(@NonNull  View itemView) {
            super(itemView);
            circleImageView=itemView.findViewById(R.id.ProfileImage);
            txtmessage=itemView.findViewById(R.id.txtmessagesRecive);

        }
    }
}
