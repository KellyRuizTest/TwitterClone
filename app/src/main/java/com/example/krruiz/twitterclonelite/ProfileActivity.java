package com.example.krruiz.twitterclonelite;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.krruiz.twitterclonelite.Model.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    // Data
    private EditText UserName, UserBio, UserLocation, UserSite;
    private ImageView UserProfile, UserBanner;
    private TextView Userid;

    // complement
    private LinearLayout BannerLinear;
    private Button UpdateProfile;
    private ProgressDialog loadingBar;

    // Database
    private DatabaseReference ProfileReference;
    private StorageReference ProfileImagRef;
    private StorageReference ProfileBanner;

    // others complement
    private Uri selectImage;
    private String productRandomKey, saveCurrentDate, saveCurrenTime, downloadImageURL;

    private int kindPhoto = 0; // 0 for profile, 1 for banner

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Editar perfil");

        ProfileReference = FirebaseDatabase.getInstance().getReference().child("Users");
        ProfileImagRef = FirebaseStorage.getInstance().getReference().child("Profile");
        ProfileBanner = FirebaseStorage.getInstance().getReference().child("Banner");

        Userid = (TextView) findViewById(R.id.iduser);
        UserName = (EditText) findViewById(R.id.name);
        UserBio = (EditText) findViewById(R.id.biografia);
        UserLocation = (EditText) findViewById(R.id.ubicacion);
        UserSite = (EditText) findViewById(R.id.sitio);

        UserProfile = (ImageView) findViewById(R.id.profile_img);
        UserBanner = (ImageView) findViewById(R.id.image_banner);
        UpdateProfile = (Button) findViewById(R.id.updateprofile);

        loadingBar = new ProgressDialog(this);

        Userid.setText(Prevalent.currentUser.getId());
        UserName.setText(Prevalent.currentUser.getName());

        UserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                kindPhoto = 0;
                Toast.makeText(ProfileActivity.this, "I clicked Image", Toast.LENGTH_SHORT).show();

                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String [] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else{
                    openGallery();
                }
            }
        });

        UserBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kindPhoto = 1;
                if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String [] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else{
                    openGallery();
                }
            }
        });

        UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBar.setTitle("Actualizando Perfil");
                loadingBar.setMessage("Please wait");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
                validateData();
            }
        });
    }

    private void openGallery() {

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        selectImage = data.getData();

        if (requestCode == 1 && resultCode == RESULT_OK && data != null){

            if (kindPhoto == 0) {
                Picasso.with(this).load(selectImage).fit().centerCrop().into(UserProfile);
            } else {
                Picasso.with(this).load(selectImage).fit().centerCrop().into(UserBanner);
            }
        }else {
            Toast.makeText(ProfileActivity.this, "There is a issue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void validateData(){

        String name_aux = UserName.getText().toString();

        if (name_aux == null || name_aux.equals("") || selectImage == null){
            Toast.makeText(ProfileActivity.this, "name and profile are empty", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currenDate = new SimpleDateFormat("yyyy-MM-dd");
        saveCurrentDate = currenDate.format(calendar.getTime());

        SimpleDateFormat currenTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrenTime = currenTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrenTime;

        // StorageReference variable to save everything
        final StorageReference filePath = ProfileImagRef.child(selectImage.getLastPathSegment() + productRandomKey + ".jpg" );
        final UploadTask uploadTask = filePath.putFile(selectImage);

        // checking if failure upload
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error: "+e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(getApplicationContext(), "Image uploaded successfully", Toast.LENGTH_LONG).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadImageURL = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if (task.isSuccessful()){
                            downloadImageURL = task.getResult().toString();
                            System.out.println("========================================================");
                            System.out.println(downloadImageURL);
                            System.out.println("========================================================");

                            Toast.makeText(getApplicationContext(), "Profile Image URL sucessfully ", Toast.LENGTH_LONG);

                            saveProductInfoDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveProductInfoDatabase() {

        String id = Prevalent.currentUser.getId();

        HashMap<String, Object> photoMap = new HashMap<>();
        photoMap.put("name", Prevalent.currentUser.getName());
        photoMap.put("image", downloadImageURL);

        if (UserBio.getText().toString() != null || !(UserBio.getText().toString().equals(""))) {
            photoMap.put("bio", UserBio.getText().toString());
        }

        if (UserLocation.getText().toString() != null || !(UserLocation.getText().toString().equals(""))) {
            photoMap.put("location", UserLocation.getText().toString());
        }

        if (UserSite.getText().toString() != null || !(UserSite.getText().toString().equals(""))){
            photoMap.put("sitioweb", UserSite.getText().toString());
        }

        ProfileReference.child(id).updateChildren(photoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()){
                    Toast.makeText(ProfileActivity.this, "Profile was updated sucessfully", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                    startActivity(intent);

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(ProfileActivity.this, "Error updating profile", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
