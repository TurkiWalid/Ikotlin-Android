package com.androidprojects.esprit.ikotlin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.models.User;
import com.androidprojects.esprit.ikotlin.utils.Configuration;
import com.androidprojects.esprit.ikotlin.utils.DataBaseHandler;
import com.androidprojects.esprit.ikotlin.webservices.ServerCallbacks;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rey.material.widget.EditText;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileSettingsActivity extends AppCompatActivity {

    CircleImageView userImgProfile;
    TextView editProfilePic;
    EditText changeNameTxt;
    Button save;
    Button upload_image;

    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 85;

    ProgressDialog progressDialog;

    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final View v = LayoutInflater.from(this).inflate(R.layout.actionbartitle_view, null);
        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Edit profil");
        ((TextView)v.findViewById(R.id.actionBarTitle)).setGravity(Gravity.CENTER_VERTICAL);
        getSupportActionBar().setCustomView(v);


        userImgProfile=findViewById(R.id.userImgProfile_inSettings);
        editProfilePic=findViewById(R.id.editProfilePic);
        changeNameTxt=findViewById(R.id.changeNameTxt);
        save=findViewById(R.id.save_profile);
        upload_image = findViewById(R.id.upload_image_setting);

        String name= DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser().getUsername();
        if(name!=null && !name.isEmpty())
        changeNameTxt.setText(name);
        else
            name="Na";

        progressDialog=new ProgressDialog(this);

        if(DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser().getPictureURL()!=null)
            Picasso.with(this).load(Uri.parse(DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser().getPictureURL())).into(userImgProfile);
        else{
            userImgProfile.setImageDrawable(UserProfileServices.getInstance().getEmptyProfimePicture(name));
        }

        attachClickOnEditAvatar();
        attachClickOnSave();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void attachClickOnSave(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!changeNameTxt.getText().toString().isEmpty() && Configuration.isOnline(ProfileSettingsActivity.this)){
                    if(!changeNameTxt.getText().toString().trim().isEmpty()){
                        String name=changeNameTxt.getText().toString().trim();
                        progressDialog.setMessage("Saving new data...");
                        progressDialog.show();
                        User u = DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser();
                        u.setUsername(name);
                        DataBaseHandler.getInstance(ProfileSettingsActivity.this).updateUser(u);
                        UserProfileServices.getInstance().changeUsername(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, getApplicationContext(), new ServerCallbacks() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Toast.makeText(ProfileSettingsActivity.this,"Done",Toast.LENGTH_SHORT).show();
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Intent i = new Intent(ProfileSettingsActivity.this,HomeActivity.class);
                                startActivity(i);
                            }

                            @Override
                            public void onError(VolleyError result) {
                                Toast.makeText(ProfileSettingsActivity.this,"Error while saving data",Toast.LENGTH_SHORT).show();
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Intent i = new Intent(ProfileSettingsActivity.this,HomeActivity.class);
                                startActivity(i);
                            }

                            @Override
                            public void onWrong(JSONObject result) {
                                Toast.makeText(ProfileSettingsActivity.this,"Error while saving data",Toast.LENGTH_SHORT).show();
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Intent i = new Intent(ProfileSettingsActivity.this,HomeActivity.class);
                                startActivity(i);
                            }
                        });
                    }
                }
                else Toast.makeText(ProfileSettingsActivity.this,"Could not update",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void attachClickOnEditAvatar(){
        editProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
        upload_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImgProfile.setImageBitmap(bitmap);
                upload_image.setVisibility(View.VISIBLE);
            }
            catch (IOException e)
            {
                e.printStackTrace();
                upload_image.setVisibility(View.GONE);
            }
        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child(UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ProfileSettingsActivity.this.updateDatabase(taskSnapshot.getDownloadUrl().toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(ProfileSettingsActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void updateDatabase(final String url_image){
        User u = DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser();
        u.setPictureUrl(url_image);
        DataBaseHandler.getInstance(ProfileSettingsActivity.this).saveUser(u);
        UserProfileServices.getInstance().changeProfilePicture(FirebaseAuth.getInstance().getCurrentUser().getUid(), url_image, this, new ServerCallbacks() {
            @Override
            public void onSuccess(JSONObject result) {
                User u = DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser();
                StorageReference photoRef = storage.getReferenceFromUrl(u.getPictureURL());
                u.setPictureUrl(url_image);
                DataBaseHandler.getInstance(ProfileSettingsActivity.this).updateUser(u);
                Toast.makeText(ProfileSettingsActivity.this, "Profile picture saved", Toast.LENGTH_SHORT).show();
                upload_image.setVisibility(View.GONE);


                //deleting old
                photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // File deleted successfully
                       // Log.d("del", "onSuccess: deleted file");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Uh-oh, an error occurred!
                      //  Log.d("del", "onFailure: did not delete file");
                    }
                });
            }

            @Override
            public void onError(VolleyError result) {
                if(DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser().getPictureURL()!=null)
                    Picasso.with(ProfileSettingsActivity.this).load(Uri.parse(DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser().getPictureURL())).into(userImgProfile);
                Toast.makeText(ProfileSettingsActivity.this, "Problem saving new picture", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWrong(JSONObject result) {
                if(DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser().getPictureURL()!=null)
                    Picasso.with(ProfileSettingsActivity.this).load(Uri.parse(DataBaseHandler.getInstance(ProfileSettingsActivity.this).getUser().getPictureURL())).into(userImgProfile);
                Toast.makeText(ProfileSettingsActivity.this, "Problem saving new picture", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
