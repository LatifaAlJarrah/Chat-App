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
import com.example.chitchat.models.Call;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.callViewHolder> {
    ArrayList<com.example.chitchat.models.Call> callsList;
    Activity activity;
    Context context;

    public CallAdapter(ArrayList<Call> callsList, Activity activity, Context context) {
        this.callsList = callsList;
        this.activity = activity;
        this.context = context;
    }
    @NonNull
    @Override
    public CallAdapter.callViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.call_list, parent, false);
        return new callViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.callViewHolder holder, int position) {
        com.example.chitchat.models.Call call = callsList.get(position);
        Bitmap imageCall = BitmapFactory.decodeByteArray(callsList.get(position).getImage(), 0, callsList.get(position).getImage().length);
        holder.image.setImageBitmap(imageCall);
        holder.name.setText(callsList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return callsList.size();
    }

    public class callViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        public callViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.userImg);
            name = itemView.findViewById(R.id.userName);
        }
    }
}
