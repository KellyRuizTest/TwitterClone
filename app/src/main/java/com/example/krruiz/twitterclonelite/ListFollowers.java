package com.example.krruiz.twitterclonelite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListFollowers extends AppCompatActivity {

    private RecyclerView recyclerViewFollowers;
    private DatabaseReference MyDBFollowers;
    private FirebaseAuth firebaseUserFollowers;
    private List<Users> userUploads;
    public List<String> listIduploads = new ArrayList<>();
    private UserAdapter userAdapter;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_followers);

        setTitle("Followers");
        firebaseUserFollowers = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            idUser = bundle.getString("idforcheck");

        }else{
            idUser = firebaseUserFollowers.getUid();
        }


        recyclerViewFollowers = findViewById(R.id.list_followers_recyclerview);
        recyclerViewFollowers.setHasFixedSize(true);
        recyclerViewFollowers.setLayoutManager(new LinearLayoutManager(this));

        userUploads = new ArrayList<>();
        userAdapter = new UserAdapter(ListFollowers.this, userUploads);
        recyclerViewFollowers.setAdapter(userAdapter);
        searchingFollowersById();

    }

    private void searchingFollowersById() {

        MyDBFollowers = FirebaseDatabase.getInstance().getReference().child("Follow").child(idUser).child("Followers");


        MyDBFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Users userIn = postSnapshot.getValue(Users.class);
                    userUploads.add(userIn);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }
}
