package com.example.krruiz.twitterclonelite.Model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;

public class Prevalent {

    public static Users currentUser;
    private FirebaseUser firebaseUser;
    private DatabaseReference Db;


    public Prevalent(){
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Db = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        //currentUser = getCurrentUser();
    }

    public Users getCurrentUser(){

        Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUser = dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return currentUser;

    }
}
