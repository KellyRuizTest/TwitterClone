package com.example.krruiz.twitterclonelite;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText idEdit, emailEdit, passEdit, nameEdit;
    private Button createUser;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        getSupportActionBar().hide();

        idEdit = (EditText) findViewById(R.id.edit_id);
        emailEdit = (EditText) findViewById(R.id.edit_email);
        passEdit = (EditText) findViewById(R.id.edit_pass);
        nameEdit = (EditText) findViewById(R.id.edit_name);

        loadingBar = new ProgressDialog(this);

        createUser = (Button) findViewById(R.id.register_account);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                creatingAccount();
            }
        });
    }
    private void creatingAccount() {

        String id = "@"+idEdit.getText().toString();
        String name = nameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passEdit.getText().toString();

        if (id == null || id.equals("")){
            Toast.makeText(this, "Please input an id account", Toast.LENGTH_SHORT);
        }else if (email == null || email.equals("")){

            Toast.makeText(this, "Please input an email account", Toast.LENGTH_SHORT);
        } else if (password == null || password.equals("")){

            Toast.makeText(this, "Please write a password account", Toast.LENGTH_SHORT);
        }else {

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
           // Toast.makeText(this, "Registering", Toast.LENGTH_SHORT).show();
            validateEmailandId(id, name, email, password);
        }
    }

    private void validateEmailandId(final String id, final String name, final String email, final String password){

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (!(dataSnapshot.child("Users").child(id).exists()) && (!(dataSnapshot.child("Users").child(id).exists()))) {

                        HashMap<String, Object> userdataMap = new HashMap<>();
                        userdataMap.put("id", id);
                        userdataMap.put("name", name);
                        userdataMap.put("email", email);
                        userdataMap.put("password", password);

                        rootRef.child("Users").child(id).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Your account was created sucessfully", Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    loadingBar.dismiss();
                                    Toast.makeText(RegistrationActivity.this, " Network Error: Please try again", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "This id or email account already exists", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();

                        Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
