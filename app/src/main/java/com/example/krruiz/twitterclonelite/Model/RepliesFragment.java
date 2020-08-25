package com.example.krruiz.twitterclonelite.Model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.krruiz.twitterclonelite.R;
import com.example.krruiz.twitterclonelite.TweetAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RepliesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RepliesFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewReplies;
    private List<Tweet> tweetsUploads;
    private TweetAdapter tweetAdapter;
    private FirebaseUser firebaseUser;
    private String idUser;

    private List<String> idUsers;

    public RepliesFragment() {
        // Required empty public constructor
    }

    public RepliesFragment(String id){
        idUser = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RepliesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RepliesFragment newInstance(String param1, String param2) {
        RepliesFragment fragment = new RepliesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_replies, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // recycler view
        recyclerViewReplies = v.findViewById(R.id.recycler_view_replies_feed);
        recyclerViewReplies.setHasFixedSize(true);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerViewReplies.setLayoutManager(linearLayout);

        idUsers = new ArrayList<>();
        tweetsUploads = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getContext(), tweetsUploads);
        recyclerViewReplies.setAdapter(tweetAdapter);

        getRepliesFeed();

        return v;
    }

    private void getRepliesFeed() {

        DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference().child("Retweet");
        tweetsUploads.clear();

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                idUsers.clear();
                for (DataSnapshot each: snapshot.getChildren()){
                    for (DataSnapshot again : each.getChildren()){
                        if (again.getKey().equals(idUser)){
                            System.out.println("<=====================================================>");
                            System.out.println(each.getKey());
                            System.out.println(again.getKey());
                            System.out.println(idUser);
                            System.out.println("<=====================================================>");
                            idUsers.add(each.getKey());
                        }
                    }
                }
                retrieveTweets();
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

                    for (String userId : idUsers){
                        if (tweetOne.getId().equals(userId)){
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
}