package com.example.krruiz.twitterclonelite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.core.content.ContextCompat.getColor;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserInViewHolder> {

    public Context context;
    public List<Users> uploads;
    private FirebaseAuth firebaseUser = FirebaseAuth.getInstance();

    public UserAdapter(Context context1, List<Users> usersUploads){

        context = context1;
        uploads = usersUploads;
    }

    @NonNull
    @Override
    public UserInViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.show_contacts, parent, false);
        return new UserInViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final UserInViewHolder holder, int position) {

        Users userCurrent = uploads.get(position);
        holder.userName.setText(userCurrent.getName());
        holder.userId.setText(userCurrent.getIdUser());
        holder.userBio.setText(userCurrent.getBio());
        Picasso.get().load(userCurrent.getImage()).into(holder.userImage);

        final String idUser = userCurrent.getId();

        isFollowing(userCurrent.getId(), holder.buttonFollow);
        unFollow(holder.buttonFollow, userCurrent);

        holder.cardViewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToProfile = new Intent(context, PersonalFeedActivity.class);
                intentToProfile.putExtra("idUser", idUser);
                intentToProfile.setType("text/plain");
                context.startActivity(intentToProfile);
            }
        });

    }

    private void unFollow(final Button auxiliar, final Users userin){

    auxiliar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (auxiliar.getText().toString().equals("follow")) {

                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getCurrentUser().getUid().toString()).child("following")
                        .child(userin.getId()).setValue(userin);

                FirebaseDatabase.getInstance().getReference().child("Follow").child(userin.getId()).child("followers")
                        .child(firebaseUser.getUid().toString()).setValue(userin);

            }else {

                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getCurrentUser().getUid()).child("following")
                        .child(userin.getId()).removeValue();

                FirebaseDatabase.getInstance().getReference().child("Follow").child(userin.getId()).child("followers")
                        .child(firebaseUser.getCurrentUser().getUid()).removeValue();

            }
        }

    });


    }

    private void isFollowing(final String id, final Button btn) {

        DatabaseReference MyDB = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getCurrentUser().getUid()).child("following");

        MyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(id).exists()) {
                        btn.setText("following");
                        btn.setBackgroundResource(R.drawable.borde_round_follow);
                    } else {
                        btn.setText("follow");
                        btn.setBackgroundResource(R.drawable.borde_round_unfollow);
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

    public class UserInViewHolder extends RecyclerView.ViewHolder {

        public ImageView userImage;
        public TextView userName;
        public TextView userId;
        public TextView userBio;
        public Button buttonFollow;
        public CardView cardViewUser;

        public UserInViewHolder(@NonNull View itemView) {
            super(itemView);

            userName = (TextView) itemView.findViewById(R.id.show_contact_name);
            userId = (TextView) itemView.findViewById(R.id.show_contact_id);
            userBio = (TextView) itemView.findViewById(R.id.show_bio);
            userImage = (ImageView) itemView.findViewById(R.id.show_contact_image);
            buttonFollow = (Button) itemView.findViewById(R.id.button_follow);
            cardViewUser = (CardView) itemView.findViewById(R.id.cardview_show_contact);


        }
    }
}
