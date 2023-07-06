package com.example.chitchat.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.R;
import com.example.chitchat.models.Chat;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.myViewHolder> {
    ArrayList<Chat> chatList;
    Activity activity;

    Context context;

    public ChatAdapter(ArrayList<Chat> chatList, Activity activity, Context context) {
        this.chatList = chatList;
        this.activity = activity;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_all, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.myViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        Bitmap imageChat = BitmapFactory.decodeByteArray(chatList.get(position).getImage(), 0, chatList.get(position).getImage().length);
        holder.imageChat.setImageBitmap(imageChat);
        holder.nameUser.setText(chatList.get(position).getName());
        holder.lastMessage.setText(chatList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView imageChat;
        TextView nameUser, lastMessage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageChat = itemView.findViewById(R.id.userImg);
            nameUser = itemView.findViewById(R.id.textName);
            lastMessage = itemView.findViewById(R.id.textMessage);

        }
    }
}
