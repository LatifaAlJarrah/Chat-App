package com.example.chitchat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.databinding.ItemAllBinding;
import com.example.chitchat.databinding.ItemFriendsBinding;
import com.example.chitchat.listener.UserListener;
import com.example.chitchat.models.User;

import java.util.List;

public class FriendesAdapter extends RecyclerView.Adapter<FriendesAdapter.FriendsViewHolder> {
    private final List<User> userList;
    private final UserListener userListener;

    public FriendesAdapter(List<User> userList,UserListener userListener) {
        this.userList = userList;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public FriendesAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFriendsBinding itemFriendsBinding = ItemFriendsBinding.inflate(
                LayoutInflater.from(parent.getContext())
                ,parent , false
        );
        return new FriendesAdapter.FriendsViewHolder(itemFriendsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendesAdapter.FriendsViewHolder holder, int position) {
        holder.setUserData(userList.get(position));

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    class FriendsViewHolder extends RecyclerView.ViewHolder {

        ItemFriendsBinding binding;
        FriendsViewHolder(ItemFriendsBinding itemFriendsBinding) {
            super(itemFriendsBinding.getRoot());
            binding = itemFriendsBinding;
        }
        void setUserData(User user) {
            binding.textName.setText(user.name);
            binding.userImg.setImageBitmap(getUserImage(user.image));
            binding.getRoot().setOnClickListener(v -> userListener.onUserClicked(user));
        }
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}