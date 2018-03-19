package com.androidprojects.esprit.ikotlin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidprojects.esprit.ikotlin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class PasswordChangeActivity extends AppCompatActivity {
    FirebaseUser user;
    Button savePass;

    com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText current,newpass;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_change);

        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final View v = LayoutInflater.from(this).inflate(R.layout.actionbartitle_view, null);
        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Change password");
        ((TextView)v.findViewById(R.id.actionBarTitle)).setGravity(Gravity.CENTER_VERTICAL);
        getSupportActionBar().setCustomView(v);


        progressDialog=new ProgressDialog(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        savePass = findViewById(R.id.save_password);
        current = findViewById(R.id.pswText_old);
        newpass = findViewById(R.id.pswText_change);


         savePass.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (current.getText().toString().isEmpty())   current.setError("Required");
                 else if (newpass.getText().toString().isEmpty())  newpass.setError("Required");
                 else{
                     progressDialog.setMessage("Updating password");
                     progressDialog.show();
                     AuthCredential credential = EmailAuthProvider
                             .getCredential(user.getEmail(), current.getText().toString());

                     user.reauthenticate(credential)
                             .addOnCompleteListener(new OnCompleteListener<Void>() {
                                 @Override
                                 public void onComplete(@NonNull Task<Void> task) {
                                     if (task.isSuccessful()) {
                                         user.updatePassword(newpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                             @Override
                                             public void onComplete(@NonNull Task<Void> task) {
                                                 if (task.isSuccessful()) {
                                                     Toast.makeText(PasswordChangeActivity.this,"Password Updated ",Toast.LENGTH_LONG).show();
                                                     progressDialog.dismiss();
                                                     Intent i = new Intent(PasswordChangeActivity.this,HomeActivity.class);
                                                     startActivity(i);
                                                 } else {
                                                     Toast.makeText(PasswordChangeActivity.this,"Passwor not updated...\nPlease verify your entries ",Toast.LENGTH_LONG).show();
                                                     progressDialog.dismiss();
                                                 }
                                             }
                                         });
                                     } else {
                                         Toast.makeText(PasswordChangeActivity.this,"Passwor not updated...\nPlease reenter current password ",Toast.LENGTH_LONG).show();
                                         progressDialog.dismiss();
                                     }
                                 }
                             });
                 }
             }
         });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
