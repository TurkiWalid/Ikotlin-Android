package com.androidprojects.esprit.ikotlin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
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
import com.androidprojects.esprit.ikotlin.utils.Configuration;
import com.androidprojects.esprit.ikotlin.utils.DataBaseHandler;
import com.androidprojects.esprit.ikotlin.webservices.ServerCallbacks;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rey.material.widget.EditText;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileSettingsActivity extends AppCompatActivity {

    CircleImageView userImgProfile;
    TextView editProfilePic;
    EditText changeNameTxt;
    Button save;
    private StorageReference mStorageRef;
    private static final int PICK_IMAGE_REQUEST = 1234;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilesettings);

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

        String name= DataBaseHandler.getInstance(getApplicationContext()).getUser().getUsername();
        if(name!=null && !name.isEmpty())
        changeNameTxt.setText(name);
        else
            name="Na";

        progressDialog=new ProgressDialog(this);

        if(DataBaseHandler.getInstance(getApplicationContext()).getUser().getPictureURL()!=null)
            Picasso.with(this).load(Uri.parse(DataBaseHandler.getInstance(getApplicationContext()).getUser().getPictureURL())).into(userImgProfile);
        else{
            userImgProfile.setImageDrawable(UserProfileServices.getInstance().getEmptyProfimePicture(name));
        }

        attachClickOnEditAvatar();
        attachClickOnSave();
        mStorageRef = FirebaseStorage.getInstance().getReference();
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
                        DataBaseHandler.getInstance(getApplicationContext()).getUser().setUsername(name);
                        UserProfileServices.getInstance().changeUsername(FirebaseAuth.getInstance().getCurrentUser().getUid(), name, getApplicationContext(), new ServerCallbacks() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                Toast.makeText(ProfileSettingsActivity.this,"Done",Toast.LENGTH_SHORT).show();
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onError(VolleyError result) {
                                Toast.makeText(ProfileSettingsActivity.this,"Error while saving data",Toast.LENGTH_SHORT).show();
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onWrong(JSONObject result) {
                                Toast.makeText(ProfileSettingsActivity.this,"Error while saving data",Toast.LENGTH_SHORT).show();
                                if(progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
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

            }
        });
    }
/*
    public void showChoosePictureDialog()
    {

        Button btnUpload, btnDone, btnCancel;

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_choose_profile_picture);

        btnUpload = dialog.findViewById(R.id.btn_dialog_pic_upload);
        btnDone = dialog.findViewById(R.id.btn_dialog_pic_save);
        btnCancel = dialog.findViewById(R.id.btn_dialog_pic_cancel);

        ivCrop = dialog.findViewById(R.id.iv_dialog_Crop);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile(filePath);
                dialog.dismiss();

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        dialog.show();

    }

    private void showFileChooser()
    {
        Log.d("upload", "onClick: in file chooser");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select an image"), PICK_IMAGE_REQUEST);

        Log.d("upload", "onClick: after file chooser");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                imageViewMteek.setImageBitmap(bitmap);

                //uploadFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void uploadFile (Uri filePath)
    {
        //Uri path = Uri.parse("android.resource://your.package.name/" + R.drawable.icon_usuario);

        String filename = getRandomString(25);
        String fileExt = getExt(filePath);

        Log.d("xxxx", "upload: "+filename);
        Log.d("xxxx", "upload: ext"+fileExt);

        //imgTitle = filename+"."+fileExt;

        StorageReference riversRef = mStorageRef.child("images/"+filename+"."+fileExt);

        riversRef.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Uri downloadUrl = taskSnapshot.getDownloadUrl();

                        imgTitle = downloadUrl.toString();
                        DashboardActivity.loggedinuser.setPic(imgTitle);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });

    }
*/
    public String getExt(Uri uri) {
        String result = null;
        Log.d("uriiiii", "getExt: "+uri);
        if (uri.getScheme().equals("content")) {
            Cursor cursor = this.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result != null) {
            int cut = result.lastIndexOf('.');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

}
