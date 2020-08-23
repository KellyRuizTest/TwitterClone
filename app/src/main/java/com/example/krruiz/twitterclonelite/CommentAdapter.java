package com.example.krruiz.twitterclonelite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.krruiz.twitterclonelite.Model.Comments;
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

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comments> aComment;
    private FirebaseUser firebaseUser;

    public CommentAdapter(Context mContext, List<Comments> aComment) {
        this.mContext = mContext;
        this.aComment = aComment;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.comment_view, parent, false);
        return new CommentViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Comments aux = aComment.get(position);
        holder.textComment.setText(aux.getComment());

        getUserInfo(aux.getCommenter(), holder.imageUser, holder.idUser);

    }

    private void getUserInfo(String comenter, final ImageView imagene, final TextView idUser) {

        DatabaseReference userInfo = FirebaseDatabase.getInstance().getReference().child("Users").child(comenter);
        userInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    Users getIn = snapshot.getValue(Users.class);
                    idUser.setText(getIn.getIdUser());
                    Picasso.get().load(getIn.getImage()).placeholder(R.drawable.usermale).into(imagene);
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return aComment.size();
    }

        public class CommentViewHolder extends RecyclerView.ViewHolder {

            public ImageView imageUser;
            public TextView idUser;
            public TextView textComment;


            public CommentViewHolder(@NonNull View itemView) {
                super(itemView);
                imageUser = itemView.findViewById(R.id.imageprofile_item_comment);
                idUser = itemView.findViewById(R.id.username_item_comment);
                textComment = itemView.findViewById(R.id.description_item_comment);
            }
        }
}

