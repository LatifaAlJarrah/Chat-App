package com.example.chitchat.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.databinding.ItemContainerRecivedMessageBinding;
import com.example.chitchat.databinding.ItemContainerSentMessageBinding;
import com.example.chitchat.models.Chat;

import java.util.ArrayList;

import kotlinx.coroutines.channels.ActorKt;

public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<Chat> chatList;
    private  Bitmap receiverProfileImage;
    private final String senderId;
    private final int VIEW_TYPE_SENT=1;
    private final int VIEW_TYPE_RECEIVED=2;

    public void setReceiverProfileImage(Bitmap bimapFromEncodedString) {

        receiverProfileImage = bimapFromEncodedString;

    }

    public ChatMessageAdapter(ArrayList<Chat> chatList, Bitmap receiverProfileImage, String senderId) {
        this.chatList = chatList;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT) {
            return new ChatMessageAdapter.SentMessageViewHolder(
                    ItemContainerSentMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new ChatMessageAdapter.ReceivdMessageViewHolder(
                    ItemContainerRecivedMessageBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position)==VIEW_TYPE_SENT){
            ((ChatMessageAdapter.SentMessageViewHolder) holder).setData(chatList.get(position));
        }else{
            ((ChatMessageAdapter.ReceivdMessageViewHolder) holder).setData(chatList.get(position),receiverProfileImage);
        }
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if(chatList.get(position).senderId.equals(senderId)){
            return VIEW_TYPE_SENT;
        }
        else {
            return VIEW_TYPE_RECEIVED;
        }

    }



    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSentMessageBinding binding;

        SentMessageViewHolder(ItemContainerSentMessageBinding itemContainerSentMessageBinding) {
            super(itemContainerSentMessageBinding.getRoot());
            binding = itemContainerSentMessageBinding;
        }

        void setData(Chat chat) {
            binding.textmessage.setText(chat.message);
            binding.textDate.setText(chat.dateTime);


        }
    }

    static class ReceivdMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerRecivedMessageBinding binding;

        ReceivdMessageViewHolder(ItemContainerRecivedMessageBinding itemContainerRecivedMessageBinding) {
            super(itemContainerRecivedMessageBinding.getRoot());
            binding = itemContainerRecivedMessageBinding;
        }

        void setData(Chat chat, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chat.message);
            binding.userimage.setImageBitmap(receiverProfileImage);
            binding.textDateTime.setText(chat.dateTime);
        }
    }
}