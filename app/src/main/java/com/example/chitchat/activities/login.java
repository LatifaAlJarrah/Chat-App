package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chitchat.databinding.ActivityLoginBinding;
import com.example.chitchat.utilities.Constants;
import com.example.chitchat.utilities.MySessionManager;
import com.example.chitchat.utilities.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class login extends AppCompatActivity {
    private ActivityLoginBinding loginBinding;
    private PreferenceManager preferenceManager;

    //[START declare_auth]
//    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // [START initialize_auth]
        // Initialize Firebase Auth
//        mAuth = FirebaseAuth.getInstance();

        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_LOG_IN)) {
            Intent intent = new Intent(getApplicationContext(), Home.class);
            startActivity(intent);
            finish();
        }

        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        setListeners();
    }
    private void setListeners(){
        loginBinding.textViewSignUp.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), SignUp.class));
        });
        loginBinding.btnLogIn.setOnClickListener(view -> {
            if (isValidSignInDetails()) {
                signIn();
            }
        });
//        loginBinding.textViewSignUp.setOnClickListener(view -> onBackPressed());
        //loginBinding.btnLogIn.setOnClickListener(view1 ->  addDataToFireStore());
    }

    // [START on_start_check_user]
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//        }
//    }
    private void signIn() {
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_NAME, loginBinding.editTextUserName.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, loginBinding.editTextPersonPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult() != null
                    && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_LOG_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading(false);
                        showToast("Unable To Sign In");
                    }
                });
    }
    private void loading(Boolean isLoading) {
        if (isLoading) {
            loginBinding.btnLogIn.setVisibility(View.INVISIBLE);
            loginBinding.progressBar.setVisibility(View.VISIBLE);
        } else {
            loginBinding.btnLogIn.setVisibility(View.VISIBLE);
            loginBinding.progressBar.setVisibility(View.INVISIBLE);
        }
    }
    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private Boolean isValidSignInDetails(){
        if (loginBinding.editTextUserName.getText().toString().trim().isEmpty()) {
            showToast("Enter Your Name");
            return false;
        } else if (loginBinding.editTextPersonPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else {
            return true;
        }
    }
//    private void addDataToFireStore(){
//        // Access a Cloud FireStore instance from your Activity
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//
//        // add data as key and value
//        HashMap<String, Object> data = new HashMap<>();
//        data.put("first_name", "latifa");
//        data.put("last_name", "latifa");
//        database.collection("users")
//                .add(data)
//                .addOnSuccessListener(documentReference -> {
//                    Toast.makeText(getApplicationContext(), "Data Inserted", Toast.LENGTH_SHORT).show();
//                })
//                .addOnFailureListener(exception -> {
//                    Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//
//    }
}