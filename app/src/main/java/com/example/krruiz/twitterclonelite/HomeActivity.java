package com.example.krruiz.twitterclonelite;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.krruiz.twitterclonelite.Model.Tweet;
import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String QUERY_FIREBASE = "QUERY_FIREBASE";

    private RecyclerView recyclerView;
    private TweetAdapter tweetAdapter;

    private DatabaseReference dataRef;
    private DatabaseReference MyDB;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;
    private List<Tweet> tweetsUploads;
    private List<String> tweetsFollowings;

    private TextView textNameUser;
    private TextView textIdUser;
    private TextView followins;
    private TextView followers;
    private ImageView imageViewUser;

    private LinearLayout layoutProfile;
    public static Users actualUser;

    private int nfollowing;
    private int nfollowers;

    private ArrayList<String> followingUsers;
    private ArrayList<String> followersUsers;
    private static final String TAG = HomeActivity.class.getSimpleName();

    @Override
    protected void onStart() {
        super.onStart();


        Log.d(TAG, "onStart()");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Inicio");
        Log.d(TAG, "onCreate()");

        layoutProfile = (LinearLayout) findViewById(R.id.layout_profile);

       // FollowList();

        auth = FirebaseAuth.getInstance();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.tweet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentTweet = new Intent(getApplicationContext(), TweetingActivity.class);
                startActivity(intentTweet);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                textNameUser = (TextView) findViewById(R.id.nameuser);
                textIdUser = (TextView) findViewById(R.id.iduser);
                followins = (TextView) findViewById(R.id.numberfollowing);
                followers = (TextView) findViewById(R.id.numberfollowers);
                imageViewUser = (ImageView) findViewById(R.id.profile_image);

                userInfo();
                nFollowList(followins, followers);


            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recycler_view_to_show_feeds);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        tweetsUploads = new ArrayList<>();

        tweetAdapter = new TweetAdapter(getApplicationContext(), tweetsUploads);
        recyclerView.setAdapter(tweetAdapter);

        checkFollowing();

    }

    private void userInfo() {
        DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Users aux = snapshot.getValue(Users.class);
                    textNameUser.setText(aux.getName());
                    textIdUser.setText(aux.getIdUser());
                    Picasso.get().load(aux.getImage()).placeholder(R.drawable.usermale).into(imageViewUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void checkFollowing() {

        tweetsFollowings = new ArrayList<>();

        DatabaseReference tweetsToShow = FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following");
        tweetsToShow.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    tweetsFollowings.clear();
                    for (DataSnapshot eachone: snapshot.getChildren())
                    {
                        tweetsFollowings.add(eachone.getKey());
                    }
                    retrieveTweets();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void retrieveTweets() {

        final DatabaseReference tweetInfo = FirebaseDatabase.getInstance().getReference().child("Tweets");
        tweetInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot tweetsIter : snapshot.getChildren()){
                    Tweet tweetOne = tweetsIter.getValue(Tweet.class);

                    for (String userId : tweetsFollowings){
                        if (tweetOne.getUser().equals(userId)){
                            tweetsUploads.add(tweetOne);
                        }
                        tweetAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


   private void nFollowList(final TextView followingBtn, final TextView followersBtn){

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference Db = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        Db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingBtn.setText(""+dataSnapshot.getChildrenCount()+"");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Db = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("followers");

        Db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followersBtn.setText(""+dataSnapshot.getChildrenCount()+"");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera){

            //Toast.makeText(HomeActivity.this, "You clicked profile", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(HomeActivity.this, PersonalFeedActivity.class);
            startActivity(intent1);

        }
        else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            finish();
            FirebaseAuth.getInstance().signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onProfileChange(View v){

        Intent intent1 = new Intent(HomeActivity.this, PersonalFeedActivity.class);
        startActivity(intent1);

    }

    public void onProfileFollowers(View v){

        Intent intentFollowers = new Intent(HomeActivity.this, ListFollowers.class);
        //intentFollowers.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentFollowers);
        //finish();

    }

    public void onProfileFollowing(View v){

        Intent intentFollowing = new Intent(HomeActivity.this, ListFollowing.class);
        //intentFollowing.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intentFollowing);
        //finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_scrolling, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Log.d("RESULT_OK", "Entre a SearchView");
                Intent ShowingContact = new Intent(HomeActivity.this, ShowingContacts.class);
                ShowingContact.putExtra(QUERY_FIREBASE, query);
                startActivity(ShowingContact);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

}