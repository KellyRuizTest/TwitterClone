package com.example.krruiz.twitterclonelite;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListFollowing extends AppCompatActivity {


    private RecyclerView recyclerViewFolloweing;
    private DatabaseReference MyDBFollowers;
    private FirebaseAuth firebaseUserFollowings;
    private List<Users> userUploadsFollowing;
    public List<String> listIduploads = new ArrayList<>();
    private UserAdapter userAdapterFollowing;
    private String idUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_following);

        setTitle("Following");
        firebaseUserFollowings = FirebaseAuth.getInstance();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            idUser = bundle.getString("idforcheck");

        }else{
            idUser = firebaseUserFollowings.getUid();
        }


        recyclerViewFolloweing = findViewById(R.id.list_following_recycler);
        recyclerViewFolloweing.setHasFixedSize(true);
        recyclerViewFolloweing.setLayoutManager(new LinearLayoutManager(this));

        userUploadsFollowing = new ArrayList<>();
        userAdapterFollowing = new UserAdapter(ListFollowing.this, userUploadsFollowing);
        recyclerViewFolloweing.setAdapter(userAdapterFollowing);
        searchingFollowingsById();
    }


    private void searchingFollowingsById() {


        MyDBFollowers = FirebaseDatabase.getInstance().getReference().child("Follow").child(idUser).child("following");


        MyDBFollowers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userUploadsFollowing.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Users userIn = postSnapshot.getValue(Users.class);
                    userUploadsFollowing.add(userIn);
                }

                userAdapterFollowing.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });

    }


}
