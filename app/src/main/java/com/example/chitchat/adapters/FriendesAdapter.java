package com.example.chitchat.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chitchat.databinding.ItemAllBinding;
import com.example.chitchat.models.User;

import java.util.List;

public class FriendesAdapter extends RecyclerView.Adapter<FriendesAdapter.FriendsViewHolder> {
    private final List<User> userList;

    public FriendesAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public FriendesAdapter.FriendsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAllBinding itemAllBinding = ItemAllBinding.inflate(
                LayoutInflater.from(parent.getContext())
                ,parent , false
        );
        return new FriendesAdapter.FriendsViewHolder(itemAllBinding);
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

        ItemAllBinding binding;
        FriendsViewHolder(ItemAllBinding itemAllBinding) {
            super(itemAllBinding.getRoot());
            binding = itemAllBinding;
        }
        void setUserData(User user) {
            binding.textName.setText(user.name);
            binding.userImg.setImageBitmap(getUserImage(user.image));
        }
    }
    private Bitmap getUserImage(String encodedImage) {
        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

//    public class friendsViewHolder extends RecyclerView.ViewHolder {
//        ItemAllBinding binding;
//        public friendsViewHolder(@NonNull View itemView) {
//            super(itemView);
//            binding = itemAllBinding;
//
//        }
//    }
}
