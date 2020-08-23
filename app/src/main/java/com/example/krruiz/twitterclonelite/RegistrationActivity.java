package com.example.krruiz.twitterclonelite;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        idEdit = (EditText) findViewById(R.id.edit_id);
        emailEdit = (EditText) findViewById(R.id.edit_email);
        passEdit = (EditText) findViewById(R.id.edit_pass);
        nameEdit = (EditText) findViewById(R.id.edit_name);

        auth = FirebaseAuth.getInstance();

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
        }else if (password.length() < 6) {

            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
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

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    final String userId = firebaseUser.getUid();
                    rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

                    HashMap<String, Object> userdataMap = new HashMap<>();
                    userdataMap.put("idUser", id);
                    userdataMap.put("id", userId);
                    userdataMap.put("name", name);
                    userdataMap.put("email", email);
                    userdataMap.put("password", password);
                    userdataMap.put("bio", "");
                    userdataMap.put("image", "https://firebasestorage.googleapis.com/v0/b/twitterclonelite.appspot.com/o/Profile%2Fusermale.png?alt=media&token=06d1c426-1938-4c55-bb78-7df961506ec2");
                    String imageurl = "https://firebasestorage.googleapis.com/v0/b/twitterclonelite.appspot.com/o/Profile%2Fusermale.png?alt=media&token=06d1c426-1938-4c55-bb78-7df961506ec2";

                    final Users aux = new Users(email, name, userId, id,  password, imageurl, "");


                    rootRef.setValue(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                loadingBar.dismiss();

                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                                FirebaseDatabase.getInstance().getReference().child("Follow").child(userId).child("following")
                                        .child(userId).setValue(aux);

                                startActivity(intent);
                                finish();
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(RegistrationActivity.this, " Network Error: Please try again", Toast.LENGTH_LONG).show();
                            }

                        }
                    });

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(), "This id or email account already exists", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }
}
