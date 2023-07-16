package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;

import com.example.chitchat.R;
import com.example.chitchat.adapters.UserAdapter;
import com.example.chitchat.databinding.ActivityChatMessagingBinding;
import com.example.chitchat.databinding.ActivityUserProfileBinding;
import com.example.chitchat.models.User;
import com.example.chitchat.utilities.Constants;
import com.example.chitchat.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserProfile extends AppCompatActivity {

    private ActivityUserProfileBinding binding;

    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
        getUserData();
    }

    Bitmap bitmap;
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
                            bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                            binding.userProfileImage.setImageBitmap(bitmap);
                            binding.textViewUserName.setText(user.name);
                            binding.textViewUserPhone.setText(user.phoneNumber);

                            binding.userProfileImage.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // Handle the click event
                                    enlargeImage(bitmap);
                                }
                            });

                        }
                    } else {
                        // Handle the failure case
                    }
                });
    }

    private void setListeners() {
        binding.button8.setOnClickListener(v -> onBackPressed());
    }

    private void enlargeImage(Bitmap bitmap) {
        Dialog dialog = new Dialog(UserProfile.this);
        dialog.setContentView(R.layout.dialog_enlarged_image);

        ImageView imageView = dialog.findViewById(R.id.enlargedImage);
        imageView.setImageBitmap(bitmap);

        dialog.show();
    }
}