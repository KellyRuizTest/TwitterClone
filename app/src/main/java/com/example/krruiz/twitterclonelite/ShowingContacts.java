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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.krruiz.twitterclonelite.Model.Users;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class ShowingContacts extends AppCompatActivity {

    private RecyclerView recyclerViewContacts;
    private TextView ShowingPutExtra;
    private DatabaseReference UserFirebase;
    private String QUERY_TO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_contacts);

        setTitle("Buscar Tweets");

        UserFirebase = FirebaseDatabase.getInstance().getReference().child("Users");
        String aux = getIntent().getStringExtra(HomeActivity.QUERY_FIREBASE);

        recyclerViewContacts = findViewById(R.id.show_contact_recyclerview);
        recyclerViewContacts.setHasFixedSize(true);
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));

       // recyclerViewContacts.setAdapter(firebaseRecyclerAdapter);

        QUERY_TO = getIntent().getStringExtra(HomeActivity.QUERY_FIREBASE);
        Log.d("RESULT_OK", QUERY_TO);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Query firebaseSearchQuery = UserFirebase.orderByChild("name").startAt(QUERY_TO).endAt(QUERY_TO + "\uf8ff");

                //startAt(QUERY_TO).endAt(QUERY_TO + "\uf8ff");

        FirebaseRecyclerOptions<Users> options = new FirebaseRecyclerOptions.Builder<Users>().setQuery(firebaseSearchQuery, Users.class).build();

        FirebaseRecyclerAdapter<Users, UserViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Users, UserViewHolder>(
                options
        ) {

            @Override
            protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull Users model) {

                Log.d("RESULT_OK", "Entre a onBindViewHolder de RecyclerAdapter");
                holder.bindUser(getApplicationContext(), model.getName(), model.getId(), model.getImage());

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

    public class UserViewHolder extends RecyclerView.ViewHolder{

        View v;
        //Users tweetAux;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            v = itemView;
            //context = itemView.getContext();
        }

        public void bindUser (Context ctx, String name, String id, String userImage){

            Log.d("RESULT_OK", "Entre a bindUser from UserViewHolderClass");

            //tweetAux = users;
            TextView nameUser = (TextView) v.findViewById(R.id.show_contact_name);
            TextView idUser = (TextView) v.findViewById(R.id.show_contact_id);
            ImageView imageUser = (ImageView) v.findViewById(R.id.show_contact_image);

            nameUser.setText(name);
            idUser.setText(id);

            Picasso.with(ctx).load(userImage)
                    .into(imageUser);

        }
    }

     /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_searc, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //searchContact(query);

                Log.d("RESULT_OK", "Entre a SearchView");
                firebaseSearch();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //searchContact(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }*/


}
