package com.example.krruiz.twitterclonelite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.krruiz.twitterclonelite.Model.LikeFragment;
import com.example.krruiz.twitterclonelite.Model.MediaFragment;
import com.example.krruiz.twitterclonelite.Model.RepliesFragment;
import com.example.krruiz.twitterclonelite.Model.Tweet;
import com.example.krruiz.twitterclonelite.Model.TweetsFragment;
import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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

    private ViewPager viewPager;
    private TabLayout tabLayout;

    private TweetsFragment tweetsFragment;
    private LikeFragment likeFragment;
    private RepliesFragment repliesFragment;
    private MediaFragment mediaFragment;

    private TabItem tabTweets;
    private TabItem tabReplies;
    private TabItem tabLikes;
    private TabItem tabMedia;

    private PageAdapter pageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_feed);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // getting user


        // init variables
        followingButton = findViewById(R.id.follwing_or_not);
        profileButton = findViewById(R.id.button_to_profile);
        textName = findViewById(R.id.name_user_profile);
        textid = findViewById(R.id.ud_user_profile);
        imageUser = findViewById(R.id.profile_img_profile);
        numberFollowings = findViewById(R.id.number_following_profile);
        numberFollowers = findViewById(R.id.number_followers_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            idUser = bundle.getString("idUser");
            gettingFollowOrFollowing();
            profileButton.setVisibility(View.GONE);
        }else{
            idUser = firebaseUser.getUid();
            followingButton.setVisibility(View.GONE);
        }

        // Viewpager
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.view_pager);
        tabTweets = findViewById(R.id.tab_tweets);
        tabReplies = findViewById(R.id.tab_replies);
        tabLikes =findViewById(R.id.tab_likes);
        tabMedia = findViewById(R.id.tab_media);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), idUser);
        viewPager.setAdapter(pageAdapter);

        // Fragment
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setText("Tweets");
        tabLayout.getTabAt(1).setText("Replies");
        tabLayout.getTabAt(2).setText("Media");
        tabLayout.getTabAt(3).setText("Likes");
        /*tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 1){

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));*/

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
        //getTweetsFeed();


    }

    private void gettingFollowOrFollowing() {

        DatabaseReference userFollowRef = FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("Following");
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

    private void getQtyFollowers() {

        DatabaseReference followingN = FirebaseDatabase.getInstance().getReference().child("Follow").child(idUser).child("Followers");

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
        DatabaseReference followingN = FirebaseDatabase.getInstance().getReference().child("Follow").child(idUser).child("Following");

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
        intentFollowers.putExtra("idforcheck", idUser);
        //intentFollowers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentFollowers);
    }

    public void onProfileFollowers(View view) {

        Intent intentFollowers = new Intent(PersonalFeedActivity.this, ListFollowers.class);
        intentFollowers.putExtra("idforcheck", idUser);
        //intentFollowers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentFollowers);
    }
}