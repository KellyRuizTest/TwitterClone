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

import com.example.krruiz.twitterclonelite.Model.Prevalent;
import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLogin, passLogin;
    private Button loginButton;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        emailLogin = (EditText) findViewById(R.id.edit_email_login);
        passLogin = (EditText) findViewById(R.id.edit_pass_login);
        loginButton = (Button) findViewById(R.id.login_account);

        loadingBar = new ProgressDialog(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });
    }

    private void LoginUser() {

        String id = "@"+emailLogin.getText().toString();
        String passwordID = passLogin.getText().toString();
        if (id.equals("")){

            Toast.makeText(this, "Please write your email account", Toast.LENGTH_SHORT).show();
        } else if (passwordID.equals("")){

            Toast.makeText(this, "Please write your password account", Toast.LENGTH_SHORT).show();
        }else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            AllowAccessToAccount(id, passwordID); // it can be a DaO+Room+LiveData+AndroidViewModel
        }
    }

    // change name parametrs
    private void AllowAccessToAccount(final String id, final String password) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Users").child(id).exists()){

                    Users userData = dataSnapshot.child("Users").child(id).getValue(Users.class);
                    String passwordAux = userData.getPassword();

                    System.out.println("=========================================");
                    System.out.println(userData.getId());
                    System.out.println(passwordAux);
                    System.out.println("=========================================");

                    if (id.equals(userData.getId())) {

                        if (password.equals(userData.getPassword())) {

                            Toast.makeText(getApplicationContext(), "Login sucessfully", Toast.LENGTH_LONG).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                            Prevalent.currentUser = userData;
                            startActivity(intent);
                            finish();

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(getApplicationContext(), "Password Incorrect", Toast.LENGTH_LONG).show();
                        }
                    }

                }else{

                    Toast.makeText(getApplicationContext(), "Incorrect ID", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}