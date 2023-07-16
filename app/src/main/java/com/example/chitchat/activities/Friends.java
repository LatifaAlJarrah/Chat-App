package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.chitchat.R;
import com.example.chitchat.adapters.FriendesAdapter;
import com.example.chitchat.adapters.UserAdapter;
import com.example.chitchat.databinding.ActivityFriendsBinding;
import com.example.chitchat.listener.UserListener;
import com.example.chitchat.models.User;
import com.example.chitchat.utilities.Constants;
import com.example.chitchat.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//public class Friends extends BaseActivity  implements UserListener {
//    private ActivityFriendsBinding binding;
//
//    private PreferenceManager preferenceManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityFriendsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        getUser();
//        setListener();
//
//    }
//    private void setListener() {
//        binding.button6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), Home.class);
//                startActivity(intent);
//            }
//        });
//    }
//    private void getUser() {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .get()
//                .addOnCompleteListener(task -> {
//                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        List<User> users = new ArrayList<>();
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
//                                continue;
//                            }
//                            User user = new User();
//                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
//                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
//                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
//                            user.token = queryDocumentSnapshot.getString(Constants.KEY_TOKEN);
//                            user.id =queryDocumentSnapshot.getId();
//                            users.add(user);
//                        }
//                        if (users.size() > 0) {
//                            FriendesAdapter friendesAdapter = new FriendesAdapter(users, this);
//                            binding.recycler.setAdapter(friendesAdapter);
//                            binding.recycler.setVisibility(View.VISIBLE);
//                        } else {
//                            showErrorMessage();
//                        }
//                    } else {
//                        showErrorMessage();
//                    }
//                });
//    }
//    private void showErrorMessage() {
//        binding.textErrorMessage.setText(String.format("%s", "No User Available"));
//    }
//
//    @Override
//    public void onUserClicked(User user) {
//        Intent intent = new Intent(getApplicationContext(),ChatMessaging.class);
//        intent.putExtra(Constants.KEY_USER,user);
//        startActivity(intent);
//        finish();
//
//    }
//}
public class Friends extends BaseActivity  implements UserListener {
    private ActivityFriendsBinding binding;
    private PreferenceManager preferenceManager;
    private List<User> users;
    private List<User> filteredUsers;
    private FriendesAdapter friendesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        filteredUsers = new ArrayList<>();
        users = new ArrayList<>();

        getUser();
        setListener();

        friendesAdapter = new FriendesAdapter(filteredUsers, this);
        binding.recycler.setAdapter(friendesAdapter);

        binding.editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterFriends(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
    private void filterFriends(String searchText) {
        filteredUsers.clear();

        for (User user : users) {
            if (user.name.toLowerCase().contains(searchText.toLowerCase())) {
                filteredUsers.add(user);
            }
        }

        friendesAdapter.notifyDataSetChanged();

        if (filteredUsers.size() > 0) {
            binding.recycler.setVisibility(View.VISIBLE);
            binding.textErrorMessage.setVisibility(View.GONE);
        } else {
            showErrorMessage();
        }
}


    private void setListener() {
        binding.button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        binding.bottomNavigationView.findViewById(R.id.search).setOnClickListener(view -> {
            binding.container.setVisibility(View.VISIBLE);
            binding.button6.setVisibility(View.INVISIBLE);
            binding.button5.setVisibility(View.INVISIBLE);
        });
    }
    private void getUser() {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_TOKEN);
                            user.id =queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                        friendesAdapter.notifyDataSetChanged();

                        if (users.size() > 0) {
                            FriendesAdapter friendesAdapter = new FriendesAdapter(users, this);
                            binding.recycler.setAdapter(friendesAdapter);
                            binding.recycler.setVisibility(View.VISIBLE);
                        } else {
                            showErrorMessage();
                        }
                    } else {
                        showErrorMessage();
                    }
                });
    }
    private void showErrorMessage() {
        binding.recycler.setVisibility(View.GONE);
        binding.textErrorMessage.setText("no user available");
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onUserClicked(User user) {
        Intent intent = new Intent(getApplicationContext(),ChatMessaging.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
        finish();

    }
}