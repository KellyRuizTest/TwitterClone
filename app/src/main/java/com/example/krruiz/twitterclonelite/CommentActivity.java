package com.example.krruiz.twitterclonelite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krruiz.twitterclonelite.Model.Comments;
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
import java.util.HashMap;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    private String idUser;
    private String idTweet;

    private ImageView imageTweet;
    private ImageView imageUser;
    private TextView textTweet;
    private Button comment;
    private EditText addcomment;
    private RecyclerView recyclerView;
    private List<Comments> commentsUploads;
    private CommentAdapter commentAdapter;
    private FirebaseUser firebaseUser;
    DatabaseReference CommentRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        idUser = getIntent().getStringExtra("TweetUser");
        idTweet = getIntent().getStringExtra("TweetId");

        imageTweet = findViewById(R.id.tweet_image_comment);
        imageUser = findViewById(R.id.imageprofile_comment);
        textTweet = findViewById(R.id.textTweet_incomment);
        comment = findViewById(R.id.comment_button);
        addcomment = findViewById(R.id.add_comment);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = findViewById(R.id.recycler_comments);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);
        commentsUploads = new ArrayList<>();

        commentAdapter = new CommentAdapter(getApplicationContext(), commentsUploads);
        recyclerView.setAdapter(commentAdapter);

        userInfo();
        retrieveComments();
        tweetImageInfo();

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addcomment.getText().toString().equals("")){
                    Toast.makeText(CommentActivity.this, "Text is empty", Toast.LENGTH_SHORT).show();
                }else{
                    addComment();
                }
            }
        });

    }

    private void retrieveComments() {

        DatabaseReference commentReftrieve = FirebaseDatabase.getInstance().getReference().child("Comments").child(idTweet);
        commentReftrieve.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    commentsUploads.clear();
                    for (DataSnapshot eachone : snapshot.getChildren()){
                        Comments getInfromFirebaseIterator = eachone.getValue(Comments.class);

                        System.out.println("<------------------------------------------------------------>");
                        System.out.println(getInfromFirebaseIterator.getCommenter());
                        System.out.println(getInfromFirebaseIterator.getComment());
                        System.out.println("<------------------------------------------------------------>");
                        commentsUploads.add(getInfromFirebaseIterator);
                    }

                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void addComment() {

        CommentRef = FirebaseDatabase.getInstance().getReference().child("Comments").child(idTweet);
        HashMap<String, Object> commentMap = new HashMap<>();

        commentMap.put("comment", addcomment.getText().toString());
        commentMap.put("commenter", firebaseUser.getUid().toString());

        CommentRef.push().setValue(commentMap);
        addcomment.setText("");

    }

    private void userInfo() {

        DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(idUser);
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Users aux = snapshot.getValue(Users.class);
                    Picasso.get().load(aux.getImage()).placeholder(R.drawable.usermale).into(imageUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void tweetImageInfo() {

        DatabaseReference TweetInfo = FirebaseDatabase.getInstance().getReference().child("Tweets").child(idTweet);
        TweetInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Tweet aux = snapshot.getValue(Tweet.class);
                    textTweet.setText(aux.getTweet());
                    if (aux.getImageTweet() == null){
                        imageTweet.setVisibility(View.GONE);
                    }else{
                        Picasso.get().load(aux.getImageTweet()).placeholder(R.drawable.usermale).into(imageTweet);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}