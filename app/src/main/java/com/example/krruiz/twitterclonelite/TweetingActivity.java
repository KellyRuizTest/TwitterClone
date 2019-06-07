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

import com.example.krruiz.twitterclonelite.Model.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TweetingActivity extends AppCompatActivity {

    private Button buttonTweeting;
    private ProgressDialog loadingBar;
    private EditText textTweet;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private String saveCurrentDate, saveCurrenTime, productRandomKey, downloadImageURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweeting);

        getSupportActionBar().hide();
        buttonTweeting = (Button) findViewById(R.id.button_tweetclick);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Tweets");

        textTweet = (EditText) findViewById(R.id.editTweet);

        loadingBar = new ProgressDialog(this);

        buttonTweeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBar.setTitle("Tweeting");
                loadingBar.setMessage("Please wait");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                validateData();
            }
        });

    }

    private void validateData(){

        String auxTextTweet = textTweet.getText().toString();
        if (auxTextTweet.equals("")){
            Toast.makeText(getApplicationContext(), "Tweet is empty", Toast.LENGTH_LONG).show();
        }else {
            StoringInformation();
        }
    }

    private void StoringInformation(){

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currenDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate = currenDate.format(calendar.getTime());

        SimpleDateFormat currenTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrenTime = currenTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrenTime;
        String tweet = textTweet.getText().toString();

        HashMap<String, Object> photoMap = new HashMap<>();
        photoMap.put("pid", productRandomKey);
        photoMap.put("tweet", tweet);
        photoMap.put("date", saveCurrentDate);
        photoMap.put("time", saveCurrenTime);
        photoMap.put("user", Prevalent.currentUser.getId());
        photoMap.put("name", Prevalent.currentUser.getName());

        String clearCaracter = productRandomKey.replace('.', ':').replace(',', ' ');

        ProductsRef.child(productRandomKey).updateChildren(photoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Tweeting", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(TweetingActivity.this, HomeActivity.class);
                    startActivity(intent);

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(TweetingActivity.this, "Error Tweeting", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}
