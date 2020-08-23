package com.example.krruiz.twitterclonelite;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krruiz.twitterclonelite.Model.Tweet;
import com.example.krruiz.twitterclonelite.Model.Users;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ShowingContacts extends AppCompatActivity {

    private RecyclerView recyclerViewContacts;
    private TextView ShowingPutExtra;
    private DatabaseReference MyDB;
    private String QUERY_TO;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_contacts);

        setTitle("Buscar Tweets");

        MyDB = FirebaseDatabase.getInstance().getReference().child("Users");
        String aux = getIntent().getStringExtra(HomeActivity.QUERY_FIREBASE);

        recyclerViewContacts = findViewById(R.id.show_contact_recyclerview);
        recyclerViewContacts.setHasFixedSize(true);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));

        QUERY_TO = getIntent().getStringExtra(HomeActivity.QUERY_FIREBASE);
        Log.d("RESULT_OK", QUERY_TO);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query firebaseSearchQuery = MyDB.orderByChild("name").startAt(QUERY_TO).endAt(QUERY_TO + "\uf8ff");

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(firebaseSearchQuery, Users.class).build();

        FirebaseRecyclerAdapter<Users, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                options
        ) {

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model) {

                Log.d("RESULT_OK", "Entre a onBindViewHolder de RecyclerAdapter");
                holder.bindUser(getApplicationContext(), model);

                isFollowing(model.getId(), holder.buttonFollow);
            }

            @NonNull
            @Override
            public UserViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                //return null;
                Log.d("RESULT_OK", "Entre a onCreateViewHolder de RecyclerAdapter");

                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.show_contacts, viewGroup, false);
                return new UserViewHolder(view);
            }
        };

        recyclerViewContacts.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

    }

    private void isFollowing(final String id, final Button btn) {

        MyDB = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getUid()).child("following");

        MyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child(id).getKey().equals(firebaseUser.getUid())) {

                    btn.setVisibility(View.GONE);
                } else {

                    if (dataSnapshot.child(id).exists()) {
                        btn.setText("following");
                        btn.setBackgroundResource(R.drawable.borde_round_follow);
                    } else {
                        btn.setText("follow");
                        btn.setBackgroundResource(R.drawable.borde_round_unfollow);
                        btn.setTextColor(getColor(R.color.colorPrimaryDark));
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {

        View v;
        Button buttonFollow;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            buttonFollow = (Button) v.findViewById(R.id.button_follow);
        }

        public void bindUser(Context ctx, final Users user) {

            Log.d("RESULT_OK", "Entre a bindUser from UserViewHolderClass");

            TextView nameUser = (TextView) v.findViewById(R.id.show_contact_name);
            TextView idUser = (TextView) v.findViewById(R.id.show_contact_id);
            ImageView imageUser = (ImageView) v.findViewById(R.id.show_contact_image);
            TextView bioUser = (TextView) v.findViewById(R.id.show_bio);

            nameUser.setText(user.getName());
            idUser.setText(user.getIdUser());
            bioUser.setText(user.getBio());

            Picasso.get().load(user.getImage())
                    .into(imageUser);

            buttonFollow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                if (buttonFollow.getText().toString().equals("follow")) {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(user.getId()).setValue(user);

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("followers")
                         .child(firebaseUser.getUid()).setValue(HomeActivity.actualUser);

                }else {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getUid()).child("following")
                            .child(user.getId()).removeValue();

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(user.getId()).child("followers")
                            .child(firebaseUser.getUid()).removeValue();

                    }
                }

            });
        }
    }
}

