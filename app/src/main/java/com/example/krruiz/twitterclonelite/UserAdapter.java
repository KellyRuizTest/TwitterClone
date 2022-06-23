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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

        final Users userCurrent = uploads.get(position);
        holder.userName.setText(userCurrent.getName());
        holder.userId.setText(userCurrent.getIdUser());
        holder.userBio.setText(userCurrent.getBio());
        Picasso.get().load(userCurrent.getImage()).into(holder.userImage);

        final String idUser = userCurrent.getId();

        isFollowing(userCurrent.getId(), holder.buttonFollow);
        holder.buttonFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.buttonFollow.getText().toString().trim().equals("Follow")) {
                    System.out.println("=======================================");
                    System.out.println("USER:");
                    System.out.println(idUser);
                    System.out.println("=======================================");
                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getCurrentUser().getUid().toString()).child("Following")
                            .child(userCurrent.getId()).setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        System.out.println("=======================================");
                                        System.out.println(idUser);
                                        System.out.println("=======================================");
                                        FirebaseDatabase.getInstance().getReference().child("Follow").child(idUser).child("Followers")
                                                .child(firebaseUser.getUid().toString()).setValue(true);
                                    }
                                }
                            });

                } else {

                    FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getCurrentUser().getUid()).child("Following")
                            .child(userCurrent.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        FirebaseDatabase.getInstance().getReference().child("Follow").child(userCurrent.getId()).child("Followers")
                                                .child(firebaseUser.getCurrentUser().getUid()).removeValue();
                                    }
                                }
                            });
                }
            }
        });
    }

    /*private void unFollow(final Button auxiliar, final Users userin){

    auxiliar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (auxiliar.getText().toString().trim().equals("Follow")) {

                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getCurrentUser().getUid().toString()).child("Following")
                        .child(userin.getId()).setValue(userin);

                System.out.println("===============================================");
                System.out.println("In Follow");
                System.out.println(auxiliar.getText().toString());
                System.out.println(userin.getName()+ "-"+userin.getId());
                System.out.println("===============================================");

                FirebaseDatabase.getInstance().getReference().child("Follow").child(userin.getId()).child("Followers")
                        .child(firebaseUser.getUid().toString()).setValue(userin);

            }
            if (auxiliar.getText().toString().trim().equals("Following")) {

                FirebaseDatabase.getInstance().getReference().child("Follow").child(firebaseUser.getCurrentUser().getUid()).child("Following")
                        .child(userin.getId()).removeValue();

                System.out.println("===============================================");
                System.out.println("In Following");
                System.out.println(auxiliar.getText().toString());
                System.out.println(userin.getName()+ "-"+userin.getId());
                System.out.println("===============================================");

                FirebaseDatabase.getInstance().getReference().child("Follow").child(userin.getId()).child("Followers")
                        .child(firebaseUser.getCurrentUser().getUid()).removeValue();

            }
        }

    });
    }*/

    private void isFollowing(final String id, final Button btn) {

        DatabaseReference MyDB = FirebaseDatabase.getInstance().getReference()
                .child("Follow").child(firebaseUser.getCurrentUser().getUid()).child("Following");

        MyDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(id).exists()) {
                        btn.setText("Following");
                        btn.setTextColor(context.getResources().getColor(R.color.colorTwitter));
                        btn.setBackgroundColor(context.getResources().getColor(R.color.colorWhite));
                    //    btn.setTextColor(getC getResources().getColor(R.color.colorTwitter));
                    //    btn.setBackgroundColor(getResources().getColor(R.color.colorWhite));

                    } else {
                        btn.setText("Follow");

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
