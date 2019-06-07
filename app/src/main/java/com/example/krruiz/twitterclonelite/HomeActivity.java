package com.example.krruiz.twitterclonelite;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krruiz.twitterclonelite.Model.Prevalent;
import com.example.krruiz.twitterclonelite.Model.Tweet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView recyclerView;
    private TweetAdapter tweetAdapter;

    private DatabaseReference dataRef;
    private List<Tweet> tweetsUploads;

    private TextView textNameUser;
    private TextView textIdUser;
    private ImageView imageViewUser;

    private LinearLayout layoutProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Inicio");

        layoutProfile = (LinearLayout) findViewById(R.id.layout_profile);

        System.out.println("==============Lets Test======================");
        String userN = Prevalent.currentUser.getName();
        String userId = Prevalent.currentUser.getId();
        System.out.println("Nombre: "+userN);
        System.out.println("ID: "+userId);
        System.out.println("=============================================");

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

                textNameUser.setText(Prevalent.currentUser.getName());
                textIdUser.setText(Prevalent.currentUser.getId());

            }

        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tweetsUploads = new ArrayList<>();

        dataRef = FirebaseDatabase.getInstance().getReference("Tweets");
        dataRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Tweet tweets = postSnapshot.getValue(Tweet.class);
                    tweetsUploads.add(tweets);
                }
                for (int i=0; i<tweetsUploads.size(); i++){
                    System.out.println("================");
                    System.out.println(tweetsUploads.get(i).getTweet());
                    System.out.println("================");

                }
                tweetAdapter = new TweetAdapter(HomeActivity.this, tweetsUploads);
                recyclerView.setAdapter(tweetAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

            Toast.makeText(HomeActivity.this, "You clicked profile", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent1);

        }
        else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout){

            Prevalent.currentUser = null;
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onProfileChange(View v){

        Intent intent1 = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent1);

    }

}
