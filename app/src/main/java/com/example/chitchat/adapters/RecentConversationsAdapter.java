package com.example.chitchat.adapters;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.databinding.ItemAllBinding;
import com.example.chitchat.listener.ConversionListener;
import com.example.chitchat.models.Chat;
import com.example.chitchat.models.User;

import java.util.List;

public class RecentConversationsAdapter extends RecyclerView.Adapter<RecentConversationsAdapter.ConversionViewHolder> {

    private final  List<Chat> chatLis;
    private final ConversionListener conversionListener;
    public RecentConversationsAdapter(List<Chat> chatLis, ConversionListener conversionListener) {
        this.chatLis = chatLis;
        this.conversionListener =conversionListener;
    }

    @NonNull
    @Override
    public ConversionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new RecentConversationsAdapter.ConversionViewHolder(ItemAllBinding.inflate(
                LayoutInflater.from(parent.getContext())
                ,parent , false
        ));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversionViewHolder holder, int position) {
        holder.setData(chatLis.get(position));
    }

    @Override
    public int getItemCount() {
        return chatLis.size();
    }

    class ConversionViewHolder extends RecyclerView.ViewHolder{
        ItemAllBinding binding;
        ConversionViewHolder(ItemAllBinding itemAllBinding){
            super(itemAllBinding.getRoot());
            binding = itemAllBinding;
        }
        void setData(Chat chat){
            binding.textName.setText(chat.conversionName);
            binding.userImg.setImageBitmap(getConversionImage(chat.conversionImage));
            binding.textMessage.setText(chat.message);
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = chat.conversionId;
                user.name = chat.conversionName;
                user.image = chat.conversionImage;
                conversionListener.onConversionClicked (user);
            });

        }
    }
    private Bitmap getConversionImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
