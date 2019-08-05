package com.example.krruiz.twitterclonelite;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.krruiz.twitterclonelite.Model.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter <TweetAdapter.TweetViewHolder> {

    public Context context;
    public List<Tweet> uploads;

    public TweetAdapter (Context context1, List<Tweet> tweetsUploaded){
        context = context1;
        uploads = tweetsUploaded;
    }

    @NonNull
    @Override
    public TweetViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(context).inflate(R.layout.tweet_view, viewGroup, false);
        return new TweetViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TweetViewHolder tweetViewHolder, int i) {

        Tweet tweetCurrent = uploads.get(i);
        tweetViewHolder.textViewId.setText(tweetCurrent.getUser());
        tweetViewHolder.textTweet.setText(tweetCurrent.getTweet());
        tweetViewHolder.textName.setText(tweetCurrent.getName());

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class TweetViewHolder extends RecyclerView.ViewHolder{
        public TextView textViewId;
        public TextView textTweet;
        public TextView textName;

        public TweetViewHolder(View itemView){
            super(itemView);
            textViewId = itemView.findViewById(R.id.iduser_tweet);
            textTweet = itemView.findViewById(R.id.text_tweet);
            textName = itemView.findViewById(R.id.name_user);
        }

    }

}
