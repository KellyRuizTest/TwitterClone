package com.example.krruiz.twitterclonelite.Model;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
 * Use the {@link MediaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerViewMedia;
    private List<Tweet> mediaUploads;
    private TweetAdapter tweetAdapter;
    private FirebaseUser firebaseUser;
    private String idUser;

    public MediaFragment() {
        // Required empty public constructor
    }

    public MediaFragment(String id){
        idUser = id;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MediaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MediaFragment newInstance(String param1, String param2) {
        MediaFragment fragment = new MediaFragment();
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
        View v = inflater.inflate(R.layout.fragment_media, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // recycler view
        recyclerViewMedia = v.findViewById(R.id.recycler_view_media_feed);
        recyclerViewMedia.setHasFixedSize(true);

        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setReverseLayout(true);
        linearLayout.setStackFromEnd(true);
        recyclerViewMedia.setLayoutManager(linearLayout);
        mediaUploads = new ArrayList<>();
        tweetAdapter = new TweetAdapter(getContext(), mediaUploads);
        recyclerViewMedia.setAdapter(tweetAdapter);

        getTweetsWithMedia();
        // Inflate the layout for this fragment
        return v;
    }

    private void getTweetsWithMedia() {

        DatabaseReference tweetsRef = FirebaseDatabase.getInstance().getReference().child("Tweets");
        mediaUploads.clear();

        tweetsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot each: snapshot.getChildren()){
                    Tweet auxEachOne = each.getValue(Tweet.class);
                    if (auxEachOne.getUser().equals(idUser)){
                        if (auxEachOne.getImageTweet() != null){
                            mediaUploads.add(auxEachOne);
                        }
                    }
                }
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}