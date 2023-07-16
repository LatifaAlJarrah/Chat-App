package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;

import com.example.chitchat.R;
import com.example.chitchat.databinding.ActivityCallScreenBinding;
import com.example.chitchat.databinding.ActivityChatMessagingBinding;
import com.example.chitchat.models.User;
import com.example.chitchat.utilities.Constants;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CallScreen extends AppCompatActivity {

    private ActivityCallScreenBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCallScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        getUserData();
    }

    private void getUserData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String receivedData = getIntent().getStringExtra(Constants.KEY_RECEIVER_ID);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .document(receivedData) // Assuming `receivedData` is the user ID received from the previous activity
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            User user = new User();
                            user.name = documentSnapshot.getString(Constants.KEY_NAME);
                            user.image = documentSnapshot.getString(Constants.KEY_IMAGE);
                            user.phoneNumber = documentSnapshot.getString(Constants.KEY_PHONE);

                            byte[] imageBytes = Base64.decode(user.image, Base64.DEFAULT);
                            // Convert the byte array to a Bitmap
                            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            binding.imgUser.setImageBitmap(bitmap);
                            binding.userName.setText(user.name);
                        }
                    } else {
                        // Handle the failure case
                    }
                });
    }
    private void setListeners(){
        binding.callButton.setOnClickListener(view -> onBackPressed());
    }
}