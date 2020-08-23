package com.example.krruiz.twitterclonelite;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.krruiz.twitterclonelite.Model.Prevalent;
import com.example.krruiz.twitterclonelite.Model.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class TweetingActivity extends AppCompatActivity {

    private Button buttonTweeting;
    private ProgressDialog loadingBar;
    private EditText textTweet;
    private ImageView closeTweet;
    private ImageView showImageTweet;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private FirebaseUser firebaseUser;
    private String saveCurrentDate, saveCurrenTime, productRandomKey, downloadImageURL;
    private Users actualUser;
    private String ImageURL;
    private Uri selectImage;
    private ImageView ImageTweet;
    private StorageReference ProfileImagRef;
    private boolean imageWasSaved = false;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweeting2);

        ProfileImagRef = FirebaseStorage.getInstance().getReference().child("Tweets");
        buttonTweeting = findViewById(R.id.button_tweetclick);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Tweets");
        textTweet = findViewById(R.id.editTweet);
        closeTweet = findViewById(R.id.imageView4);
        showImageTweet = findViewById(R.id.show_image_tweeting);
        ImageTweet = findViewById(R.id.image_tweet);
        textTweet.requestFocus();

        FloatingActionButton imageTweet = (FloatingActionButton) findViewById(R.id.add_image_tweet);
        imageTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }else{
                    Intent Imageintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(Imageintent, 1);
                }
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference Db = FirebaseDatabase.getInstance().getReference().child("Users").child(firebaseUser.getUid());
        Db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                actualUser = dataSnapshot.getValue(Users.class);
                ImageURL = actualUser.getImage();
                Picasso.get().load(actualUser.getImage()).into(showImageTweet);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


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

        closeTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intentBack = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intentBack);
                //intentBack.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        selectImage = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){
            try {
                System.out.println(selectImage);
                Picasso.get().load(selectImage).into(ImageTweet);
                imageWasSaved = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    private void showSoftKeyboard(EditText view){

        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void validateData(){

        String auxTextTweet = textTweet.getText().toString();
        if (auxTextTweet.equals("")) {

            if (imageWasSaved == false) {
                Toast.makeText(getApplicationContext(), "Tweet is empty", Toast.LENGTH_LONG).show();
            } else {
                StoringInformation();
            }
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

        if (imageWasSaved) {

            final StorageReference filePath = ProfileImagRef.child(selectImage.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask = filePath.putFile(selectImage);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            downloadImageURL = filePath.getDownloadUrl().toString();
                            return filePath.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {

                            if (task.isSuccessful()) {
                                downloadImageURL = task.getResult().toString();
                                System.out.println(downloadImageURL);
                                Toast.makeText(getApplicationContext(), "Image uploaded", Toast.LENGTH_SHORT);
                                saveTweetProduct(downloadImageURL);

                            }
                        }
                    });
                }
            });

        }else{
            downloadImageURL = null;
            saveTweetProduct(downloadImageURL);
        }
    }

    private void saveTweetProduct(String aux){

        String pid = firebaseUser.getUid().toString();
        String tweet = textTweet.getText().toString();
        String imageTweet = aux;
        String idTweet = FirebaseDatabase.getInstance().getReference().child("Tweets").push().getKey();

        HashMap<String, Object> photoMap = new HashMap<>();
        photoMap.put("id", idTweet);
        photoMap.put("tweet", tweet);
        photoMap.put("date", saveCurrentDate);
        photoMap.put("time", saveCurrenTime);
        photoMap.put("user", pid);
        photoMap.put("name", actualUser.getName());
        photoMap.put("imageUser", actualUser.getImage());
        photoMap.put("imageTweet", imageTweet);

        String clearCaracter = productRandomKey.replace('.', ':').replace(',', ' ');

        //voy a eliminar child(firebaseUser.getUid()) a ver que sale updateChildren(photoMap)
        ProductsRef.child(idTweet).updateChildren(photoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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
