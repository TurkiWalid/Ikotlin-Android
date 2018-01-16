package com.androidprojects.esprit.ikotlin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.utils.DataBaseHandler;
import com.androidprojects.esprit.ikotlin.models.User;
import com.androidprojects.esprit.ikotlin.webservices.ServerCallbacks;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rey.material.widget.EditText;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SignupActivity extends AppCompatActivity {

    public static User user;
    private FirebaseAuth auth;
    EditText usernameTxt, emailTxt, passwordTxt;
    UserProfileServices userprofile;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /** disabling actionBar **/
        getSupportActionBar().hide();

        //get firebase instance
        auth=FirebaseAuth.getInstance();

        usernameTxt = findViewById(R.id.userNameTxt);
        emailTxt= findViewById(R.id.emailText_signup);
        passwordTxt= findViewById(R.id.pswText);
        progressDialog=new ProgressDialog(this);

    }

    /**
     * ---------- SIGN UP---------
     **/
    public void signUp(View v){
        final String username=usernameTxt.getText().toString();
        final String email = emailTxt.getText().toString();
        final String password = passwordTxt.getText().toString();

        if (email.trim().isEmpty()){
            emailTxt.setError("Required");
            return;
        }

        if (username.trim().isEmpty()){
            usernameTxt.setError("Required");
            return;
        }

        if (password.trim().isEmpty()){
            passwordTxt.setError("Required");
            return;
        }
        progressDialog.setMessage("Welcome to IKotlin.");
        progressDialog.show();
        //authenticate user
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (passwordTxt.getText().toString().length() < 6) {
                                passwordTxt.setError("Password length must be over 6 characters");
                                if(progressDialog.isShowing()) progressDialog.dismiss();
                            } else {
                                Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                if(progressDialog.isShowing()) progressDialog.dismiss();
                            }
                        } else {
                            String idUser=auth.getCurrentUser().getUid();
                            // save user to mysql database and start the next activity
                            UserProfileServices.getInstance().registerUserWebService(idUser,username,email,SignupActivity.this,new ServerCallbacks(){

                                @Override
                                public void onSuccess(JSONObject result) {
                                    //send email verification

                                    //clear backstack
                                    finishAffinity();
                                    //start intent
                                    User user;
                                    user=UserProfileServices.getInstance().get_user_from_json(result);
                                    DataBaseHandler.getInstance(SignupActivity.this).saveUser(user);
                                   // FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                    Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                    startActivity(intent);

                                    finish();

                                    if(progressDialog.isShowing()) progressDialog.dismiss();
                                }

                                @Override
                                public void onError(VolleyError result) {
                                    auth.getCurrentUser().delete();
                                    Toast.makeText(SignupActivity.this,"Error while registring please retry",Toast.LENGTH_LONG).show();
                                    if(progressDialog.isShowing()) progressDialog.dismiss();
                                }

                                @Override
                                public void onWrong(JSONObject result) {
                                    auth.getCurrentUser().delete();
                                    Toast.makeText(SignupActivity.this,"Error while registring please retry",Toast.LENGTH_LONG).show();
                                    if(progressDialog.isShowing()) progressDialog.dismiss();
                                }
                            });

                        }
                    }
                });


    }


    /** calligraphy lib **/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}
