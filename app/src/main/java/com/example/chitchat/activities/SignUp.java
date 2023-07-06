package com.example.chitchat.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivitySignUpBinding;
import com.example.chitchat.utilities.Constants;
import com.example.chitchat.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    private ActivitySignUpBinding signUpBinding;
    private String encodedImage;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        signUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(signUpBinding.getRoot());

        preferenceManager = new PreferenceManager(getApplicationContext());

        setListeners();

    }
    private void setListeners() {
        signUpBinding.textViewLogIn.setOnClickListener(view -> onBackPressed());
        signUpBinding.btnSignUp.setOnClickListener(view -> {
            if(isValidSignUpDetails()) {
                signUp();
            }
        });

        signUpBinding.imageAddPhoto.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            pickImage.launch(intent);
        });
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void signUp(){
        loading(true);
        FirebaseFirestore database  = FirebaseFirestore.getInstance();
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, signUpBinding.usernameEditText.getText().toString());
        user.put(Constants.KEY_EMAIL, signUpBinding.editTextEmail.getText().toString());
        user.put(Constants.KEY_PHONE, signUpBinding.editTextPhone.getText().toString());
        user.put(Constants.KEY_PASSWORD, signUpBinding.editTextPassword.getText().toString());
        user.put(Constants.KEY_IMAGE, encodedImage);
        database.collection(Constants.KEY_COLLECTION_USERS)
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    loading(false);
                    preferenceManager.putBoolean(Constants.KEY_IS_LOG_IN, true);
                    preferenceManager.putString(Constants.KEY_USER_ID, documentReference.getId());
                    preferenceManager.putString(Constants.KEY_NAME, signUpBinding.usernameEditText.getText().toString());
                    preferenceManager.putString(Constants.KEY_IMAGE, encodedImage);
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(exception -> {
                    loading(false);
                    showToast(exception.getMessage());
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
                            signUpBinding.profileImage.setImageBitmap(bitmap);
                            encodedImage = encodedImage(bitmap);
                        } catch (FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );
    private Boolean isValidSignUpDetails() {
       if(encodedImage == null) {
           showToast("Select Profile Image");
           return false;
       } else if (signUpBinding.usernameEditText.getText().toString().trim().isEmpty()) {
           showToast("Enter Your Name");
           return false;
       } else if (signUpBinding.editTextEmail.getText().toString().trim().isEmpty()) {
           showToast("Enter Your Email");
           return false;
       } else if (!Patterns.EMAIL_ADDRESS.matcher(signUpBinding.editTextEmail.getText().toString()).matches()) {
           showToast("Enter Valid Email");
           return false;
       } else if (signUpBinding.editTextPhone.getText().toString().trim().isEmpty()) {
           showToast("Enter Your Phone");
           return false;
       } else if (signUpBinding.editTextPassword.getText().toString().trim().isEmpty()) {
           showToast("Enter Your Password");
           return false;
       } else {
           return true;
       }
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            signUpBinding.btnSignUp.setVisibility(View.INVISIBLE);
            signUpBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            signUpBinding.btnSignUp.setVisibility(View.VISIBLE);
            signUpBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
}