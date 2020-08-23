package com.example.krruiz.twitterclonelite;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter <TweetAdapter.TweetViewHolder> {

    public Context context;
    public List<Tweet> uploads;
    public DatabaseReference MyDB;
    public FirebaseUser firebaseUser;

    public TweetAdapter(Context context1, List<Tweet> tweetsUploaded) {
        context = context1;
        uploads = tweetsUploaded;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.tweet_view, viewGroup, false);
        return new TweetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final TweetViewHolder tweetViewHolder, int i) {

        final Tweet tweetCurrent = uploads.get(i);
        final String id = firebaseUser.getUid();
        final String pidTweet = tweetCurrent.getUser();
        tweetViewHolder.textTweet.setText(tweetCurrent.getTweet());

        Picasso.get().load(tweetCurrent.getImageUser()).into(tweetViewHolder.imageUser);

        if (!(tweetCurrent.getImageTweet() == null)){
            Picasso.get().load(tweetCurrent.getImageTweet()).into(tweetViewHolder.tweetImage);
        }else{
            tweetViewHolder.tweetImage.setVisibility(View.GONE);
        }

        tweetViewHolder.imageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToProf = new Intent(context, PersonalFeedActivity.class);
                intentToProf.putExtra("idUser", pidTweet);
                intentToProf.setType("text/plain");
                intentToProf.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentToProf);
            }
        });

        userInfo(tweetCurrent.getUser(), tweetViewHolder.imageUser, tweetViewHolder.textName, tweetViewHolder.textViewId);
        isLike(tweetCurrent.getId(), tweetViewHolder.likeTweetbtn);
        countingLikes(pidTweet, tweetViewHolder.likeTweet);
        countingComments(pidTweet, tweetViewHolder.commentTweet);

        tweetViewHolder.likeTweetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tweetViewHolder.likeTweetbtn.getTag() == "Likes"){
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(tweetCurrent.getId()).child(firebaseUser.getUid().toString()).setValue(true);
                /*    Snackbar snackbar = Snackbar.make(tweetViewHolder.likeTweet, "Liked tweet of"+tweetCurrent.getUser()+"", Snackbar.LENGTH_SHORT);
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                    snackbar.show();*/

                }else{
                    FirebaseDatabase.getInstance().getReference().child("Likes").child(tweetCurrent.getId()).child(firebaseUser.getUid().toString()).removeValue();
                }
            }
        });

        tweetViewHolder.contentTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentToDetails = new Intent(context, CommentActivity.class);
                intentToDetails.putExtra("TweetId", tweetCurrent.getId());
                intentToDetails.putExtra("TweetUser", tweetCurrent.getUser());
                intentToDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intentToDetails);

            }
        });

        tweetViewHolder.commentTweetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentToDetails = new Intent(context, CommentActivity.class);
                intentToDetails.putExtra("TweetId", tweetCurrent.getId());
                intentToDetails.putExtra("TweetUser", tweetCurrent.getUser());
                intentToDetails.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intentToDetails);

            }
        });

    }

  /*  private void TweetImage(ImageView tweetImage, String imageUser) {
    }*/

    private void userInfo(String pidTweet, final ImageView imageUser, final TextView textName, final TextView textViewId) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(pidTweet);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Users aux = snapshot.getValue(Users.class);
                    textName.setText(aux.getName());
                    textViewId.setText(aux.getIdUser());
                    Picasso.get().load(aux.getImage()).placeholder(R.drawable.usermale).into(imageUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void isLike(String idTweet, final ImageView likeTweetbtn) {

        DatabaseReference likeRef = FirebaseDatabase.getInstance().getReference().child("Likes").child(idTweet);
        likeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.child(firebaseUser.getUid()).exists()){
                    likeTweetbtn.setImageResource(R.drawable.ic_favorite_likeliked_24dp);
                    likeTweetbtn.setTag("Liked");
                } else {
                    likeTweetbtn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    likeTweetbtn.setTag("Likes");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void countingLikes(final String id, final TextView txtbtn) {

        MyDB = FirebaseDatabase.getInstance().getReference().child("Likes").child(id);
        MyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    txtbtn.setText((int) dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void countingComments(final String id, final TextView txtbtn) {

        MyDB = FirebaseDatabase.getInstance().getReference().child("Comments").child(id);
        MyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    txtbtn.setText((int) dataSnapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewId;
        public TextView textTweet;
        public TextView textName;
        public ImageView imageUser;
        public ImageView tweetImage;
        public TextView likeTweet;
        public TextView commentTweet;
        public TextView reetTweet;
        public ImageView likeTweetbtn;
        public ImageView commentTweetbtn;
        public CardView contentTweet;


        public TweetViewHolder(View itemView) {
            super(itemView);
            textViewId = itemView.findViewById(R.id.iduser_tweet);
            textTweet = itemView.findViewById(R.id.text_tweet);
            textName = itemView.findViewById(R.id.name_user);
            imageUser = itemView.findViewById(R.id.avatar_image);
            tweetImage = itemView.findViewById(R.id.image_tweet_from);
            likeTweet = itemView.findViewById(R.id.count_like);
            commentTweet = itemView.findViewById(R.id.count_comment);

            likeTweetbtn = itemView.findViewById(R.id.like_tweet);
            commentTweetbtn = itemView.findViewById(R.id.comment_tweet);
            contentTweet = itemView.findViewById(R.id.contact_card_view);
        }

    }
}