package com.example.krruiz.twitterclonelite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krruiz.twitterclonelite.Model.Tweet;
import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PersonalFeedActivity extends AppCompatActivity {

    private String idUser;
    private List<Tweet> tweetsUploads;
    private TweetAdapter tweetAdapter;
    private FirebaseUser firebaseUser;
    private Button followingButton;
    private Button profileButton;

    private TextView textName;
    private TextView textid;
    private ImageView imageUser;
    private TextView numberFollowings;
    private TextView numberFollowers;

    private RecyclerView recyclerViewTweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_feed);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // init variables
        followingButton = findViewById(R.id.follwing_or_not);
        profileButton = findViewById(R.id.button_to_profile);
        textName = findViewById(R.id.name_user_profile);
        textid = findViewById(R.id.ud_user_profile);
        imageUser = findViewById(R.id.profile_img_profile);
        numberFollowings = findViewById(R.id.number_following_profile);
        numberFollowers = findViewById(R.id.number_followers_profile);

        // getting user
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            idUser = bundle.getString("idUser");
            gettingFollowOrFollowing();
            profileButton.setVisibility(View.GONE);
        }else{
            idUser = firebaseUser.getUid();
            followingButton.setVisibility(View.GONE);
        }

        System.out.println("<========================================================================>");
        System.out.println("What is the name user");
        System.out.println(idUser);
        System.out.println("<=======================================================================>");

        // recycler view
        recyclerViewTweets = findViewById(R.id.recycler_view_profile_feed);
        recyclerViewTweets.setHasFixedSize(true);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerViewTweets.setLayoutManager(linearLayout);
        tweetsUploads = new ArrayList<>();

        tweetAdapter = new TweetAdapter(getApplicationContext(), tweetsUploads);
        recyclerViewTweets.setAdapter(tweetAdapter);


        // logic
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intenToProfile = new Intent(PersonalFeedActivity.this, ProfileActivity.class);
                startActivity(intenToProfile);
            }
        });

        followingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        userInfo();
        getQtyFollowings();
        getQtyFollowers();
        getTweetsFeed();


    }

    private void gettingFollowOrFollowing() {

        DatabaseReference userFollowRef = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following");
        userFollowRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(idUser).exists()){
                    followingButton.setText("Following");
                }else{
                    followingButton.setText("Follow");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTweetsFeed() {

        DatabaseReference tweetsRef = FirebaseDatabase.getInstance().getReference().child("Tweets");
        tweetsUploads.clear();

        tweetsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot each: snapshot.getChildren()){
                    Tweet auxEachOne = each.getValue(Tweet.class);
                    if (auxEachOne.getUser().equals(idUser)){
                        tweetsUploads.add(auxEachOne);
                    }
                }
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getQtyFollowers() {

        DatabaseReference followingN = FirebaseDatabase.getInstance().getReference().child("Follow").child(idUser).child("followers");

        followingN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String aux = ""+snapshot.getChildrenCount();
                    numberFollowers.setText(aux);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void getQtyFollowings() {
        DatabaseReference followingN = FirebaseDatabase.getInstance().getReference().child("Follow").child(idUser).child("following");

        followingN.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    String aux = ""+snapshot.getChildrenCount();
                    numberFollowings.setText(aux);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void userInfo() {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(idUser);

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Users aux = snapshot.getValue(Users.class);
                    textName.setText(aux.getName());
                    textid.setText(aux.getIdUser());
                    Picasso.get().load(aux.getImage()).into(imageUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onProfileFollowing(View view) {

        Intent intentFollowers = new Intent(PersonalFeedActivity.this, ListFollowing.class);
        //intentFollowers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentFollowers);
    }

    public void onProfileFollowers(View view) {

        Intent intentFollowers = new Intent(PersonalFeedActivity.this, ListFollowers.class);
        //intentFollowers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentFollowers);
    }
}