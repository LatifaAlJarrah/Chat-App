package com.example.chitchat.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import com.example.chitchat.databinding.ActivityProfileBinding;
import com.example.chitchat.models.User;
import com.example.chitchat.utilities.Constants;
import com.example.chitchat.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity {
    private ActivityProfileBinding binding;
    private PreferenceManager preferenceManager;

    private String encodedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        getUserData();
        setListeners();
    }

    private void getUserData() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                User user = new User();
                                user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                                user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                                user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                                user.token = queryDocumentSnapshot.getString(Constants.KEY_TOKEN);
                                user.phoneNumber = queryDocumentSnapshot.getString(Constants.KEY_PHONE);

                                byte[] imageBytes = Base64.decode(user.image, Base64.DEFAULT);
                                // Convert the byte array to a Bitmap
                                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);

                                binding.profileImage.setImageBitmap(bitmap);
                                binding.textView18.setText(user.name);
                                binding.textView21.setText(user.email);
                                binding.textView24.setText(user.phoneNumber);
                            }
                        }
                    } else {
                    }
                });
    }

    private void setListeners() {

        binding.imageAddPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
        binding.button7.setOnClickListener(view -> {
            updatePhoto();
            startActivity(new Intent(getApplicationContext(), Profile.class));
        });
    }

    private String encodedImage(Bitmap bitmap) {
        int previewWidth = 150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    private final ActivityResultLauncher<Intent> pickImage = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    if (result.getData() != null){
                        Uri imageUri = result.getData().getData();
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            binding.profileImage.setImageBitmap(bitmap);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

//    private void updatePhoto() {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .get()
//                .addOnCompleteListener(task -> {
//                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                            if (currentUserId.equals(queryDocumentSnapshot.getId())) {
//                                // Create a map with the updated photo data
//                                HashMap<String, Object> user = new HashMap<>();
//                                user.put(Constants.KEY_IMAGE, encodedImage);
//
//                            }
//                        }
//                    }
//                            }).addOnSuccessListener(documentReference -> {
//                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
//                });
//    }
        private void updatePhoto() {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.KEY_COLLECTION_USERS)
                    .get()
                    .addOnCompleteListener(task -> {
                        String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                if (currentUserId.equals(queryDocumentSnapshot.getId())) {
                                    // Create a map with the updated photo data
                                    HashMap<String, Object> user = new HashMap<>();
                                    user.put(Constants.KEY_IMAGE, encodedImage);

                                    // Update the document in Firestore
                                    database.collection(Constants.KEY_COLLECTION_USERS)
                                            .document(queryDocumentSnapshot.getId())
                                            .update(user)
                                            .addOnSuccessListener(aVoid -> {
                                                preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                                                startActivity(new Intent(getApplicationContext(), Profile.class));
                                            })
                                            .addOnFailureListener(e -> {
                                                // Handle update failure
                                            });
                                }
                            }
                        }
                    });

        }
}