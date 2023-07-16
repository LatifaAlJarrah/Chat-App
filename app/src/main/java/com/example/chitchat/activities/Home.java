package com.example.chitchat.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.example.chitchat.R;
import com.example.chitchat.adapters.RecentConversationsAdapter;
import com.example.chitchat.adapters.UserAdapter;
import com.example.chitchat.databinding.ActivityHomeBinding;
import com.example.chitchat.listener.ConversionListener;
import com.example.chitchat.models.Chat;
import com.example.chitchat.models.User;
import com.example.chitchat.utilities.Constants;
import com.example.chitchat.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import com.google.firebase.firestore.EventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.HashMap;
public class Home extends BaseActivity implements ConversionListener {
    private ActivityHomeBinding binding;
    private PreferenceManager preferenceManager;

    private List<Chat> conversations;
    private RecentConversationsAdapter conversationsAdapter;
    private FirebaseFirestore database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        init();
        loadingUserDetails();
        getToken();
        setListener();
        listenConversations();
//        getUser();
    }
    private void init(){
        conversations = new ArrayList<>();
        conversationsAdapter= new RecentConversationsAdapter(conversations, (ConversionListener) this);
        binding.recycler.setAdapter(conversationsAdapter);
        database = FirebaseFirestore.getInstance();

    }
    private void listenConversations() {
        database.collection (Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
        database.collection (Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);
    }
    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange  : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    Chat chatMessage = new Chat();
                    chatMessage.senderId = senderId;
                    chatMessage.receiverId = receiverId;
                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    } else {
                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(chatMessage);
                }else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }

            }
            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
            conversationsAdapter.notifyDataSetChanged();
            binding.recycler.smoothScrollToPosition(0);
            binding.recycler.setVisibility (View.VISIBLE);
            // binding..setVisibility (View. GONE);
        }
    };


    @Override
    public void onConversionClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatMessaging.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);

    }
//    private void getUser() {
//        FirebaseFirestore database = FirebaseFirestore.getInstance();
//        database.collection(Constants.KEY_COLLECTION_USERS)
//                .get()
//                .addOnCompleteListener(task -> {
//                    String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
//                    if (task.isSuccessful() && task.getResult() != null) {
//                        List<User>  users = new ArrayList<>();
//                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
//                            if(currentUserId.equals(queryDocumentSnapshot.getId())) {
//                                continue;
//                            }
//                            User user = new User();
//                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
//                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
//                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
//                            user.token = queryDocumentSnapshot.getString(Constants.KEY_TOKEN);
//                            users.add(user);
//                        }
//                        if (users.size() > 0) {
//                            UserAdapter userAdapter = new UserAdapter(users);
//                            binding.recycler.setAdapter(userAdapter);
//                            binding.recycler.setVisibility(View.VISIBLE);
//                        } else {
//                            showErrorMessage();
//                        }
//                    } else {
//                        showErrorMessage();
//                    }
//                });
//    }
    private void showErrorMessage() {
        binding.textErrorMessage.setText(String.format("%s", "No User Available"));
    }
    private void setListener() {
        binding.bottomNavigationView.findViewById(R.id.power).setOnClickListener(view -> signOut());
        binding.bottomNavigationView.findViewById(R.id.profile).setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Profile.class));
        });
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Friends.class);
                startActivity(intent);
            }
        });
        binding.callButton.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Call.class));
        });
    }
    private void loadingUserDetails() {
        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        binding.bottomNavigationView.findViewById(R.id.profile);


    }
    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void updateToken(String token) {
        preferenceManager.putString(Constants.KEY_TOKEN, token);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        documentReference.update(Constants.KEY_TOKEN, token)
//                .addOnSuccessListener(unused -> showToast("Token Updated Successfully"))
                .addOnFailureListener(e ->  showToast("Unable To Update Token"));
    }

    private void signOut() {
        showToast("Signing Out...");
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_USERS).document(
                        preferenceManager.getString(Constants.KEY_USER_ID)
                );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_TOKEN, FieldValue.delete());
        documentReference.update(updates)
                .addOnSuccessListener(unused -> {
                    preferenceManager.clear();
                    startActivity(new Intent(getApplicationContext(), login.class));
                    finish();
                })
                .addOnFailureListener(e -> showToast("Unable o LogIn"));
    }

}