package com.example.krruiz.twitterclonelite;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.krruiz.twitterclonelite.Model.Prevalent;
import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin, passLogin;
    private Button loginButton;

    private ProgressDialog loadingBar;
    private FirebaseAuth auth;
    private DatabaseReference rootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailLogin = (EditText) findViewById(R.id.edit_email_login);
        passLogin = (EditText) findViewById(R.id.edit_pass_login);
        loginButton = (Button) findViewById(R.id.login_account);

        auth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {

        String email = emailLogin.getText().toString();
        String passwordID = passLogin.getText().toString();
        if (email.equals("")){

            Toast.makeText(this, "Please write your email account", Toast.LENGTH_SHORT).show();
        } else if (passwordID.equals("")){

            Toast.makeText(this, "Please write your password account", Toast.LENGTH_SHORT).show();
        }else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(email, passwordID); // it can be a DaO+Room+LiveData+AndroidViewModel
        }
    }

    // change name parametrs
    private void AllowAccessToAccount(final String email, final String password) {


        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    rootRef = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(auth.getCurrentUser().getUid());

                    rootRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Login sucessfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        loadingBar.dismiss();
                        }
                    });

                } else {

                    Toast.makeText(getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                }

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseAuth.getInstance().signOut();
    }
}