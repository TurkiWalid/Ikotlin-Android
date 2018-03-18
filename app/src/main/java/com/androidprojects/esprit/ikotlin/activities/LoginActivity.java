package com.androidprojects.esprit.ikotlin.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.models.User;
import com.androidprojects.esprit.ikotlin.utils.Configuration;
import com.androidprojects.esprit.ikotlin.utils.DataBaseHandler;
import com.androidprojects.esprit.ikotlin.webservices.ServerCallbacks;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.ApiListener;
import com.linkedin.platform.listeners.ApiResponse;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class LoginActivity extends AppCompatActivity {
    com.rey.material.widget.EditText emailText;
    com.scottyab.showhidepasswordedittext.ShowHidePasswordEditText passwordText;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    public static User loggedUser;
    public static User user;
    public boolean iFfacebok=false;

    //facebook to firebase callback
    CallbackManager mCallbackManager;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*** disabling actionBar ****/
        getSupportActionBar().hide();

        emailText = findViewById(R.id.emailText_login);
        passwordText = findViewById(R.id.pswText);
        progressBar = findViewById(R.id.login_progress);

        auth = FirebaseAuth.getInstance();
        //init facebook (just in case)
        LoginManager.getInstance().logOut();
        //init facebook login
        logwithFacebookInit();
        progressDialog=new ProgressDialog(this);
    }

    /**
     * ---------- FIREBASE (and mysql) LOGIN ---------
     **/
    public void login(View view) {

        final String userEmail = emailText.getText().toString();
        final String userPassword = passwordText.getText().toString();

        if (userEmail.trim().isEmpty()) {
            emailText.setError("Required");
            return;
        }

        if (userPassword.trim().isEmpty()) {
            passwordText.setError("Required");
            return;
        }

        /***check internet connection***/
        if (Configuration.isOnline(getApplicationContext())) {
            progressDialog.setMessage("Welcome to IKotlin.");
            progressDialog.show();
            //progressBar.setVisibility(View.VISIBLE);
            //login via firebase
            auth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener
                    (LoginActivity.this, new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                // there was an error
                                if (userPassword.length() < 6) {
                                    passwordText.setError("Password length must be over 6 characters");
                                } else {
                                    Toast.makeText(LoginActivity.this, "wrong entries.", Toast.LENGTH_LONG).show();
                                }
                            } else {

                                //try logging via mysql
                                String uid = auth.getCurrentUser().getUid();
                                UserProfileServices.getInstance().markLoggedUserWebService(uid, LoginActivity.this, new ServerCallbacks() {
                                    @Override
                                    public void onSuccess(JSONObject result) {
                                        progressDialog.dismiss();
                                        User user;
                                        user = UserProfileServices.getInstance().get_user_from_json(result);
                                        DataBaseHandler.getInstance(LoginActivity.this).saveUser(user);
                                        //Log.d("user",user.toString());
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onError(VolleyError result) {
                                        auth.signOut();
                                    }

                                    @Override
                                    public void onWrong(JSONObject result) {
                                        auth.signOut();
                                    }
                                });

                            }
                        }
                    });
        } else
            Toast.makeText(LoginActivity.this, "No internet connection.", Toast.LENGTH_LONG).show();
    }

    /**
     * ---------- PASSEWORD RESETTING ---------
     **/
    public void resetPassword(View v) {
        if (emailText.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Put your email to send a reset email", Toast.LENGTH_LONG).show();
        } else {
            if (Configuration.isOnline(getApplicationContext())) {
                progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailText.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "An email has been sent to you\n check your inbox..", Toast.LENGTH_LONG).show();
                                    progressBar.setVisibility(View.GONE);
                                } else
                                    Toast.makeText(getApplicationContext(), "Verify your email", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            } else
                Toast.makeText(getApplicationContext(), "No internet connection", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    }


    /**
     * ---------- FACEBOOK LOGIN ---------
     **/
    public void logwithFacebookInit(){
        mCallbackManager= CallbackManager.Factory.create();
        final LoginButton loginButton = findViewById(R.id.facebookLoginBtn);
        loginButton.setReadPermissions("email","public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.i("facebook_login", "facebook:onSuccess:" + loginResult);
                iFfacebok=true;
                handleFacebookAccessToken(loginResult.getAccessToken());
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancel() {
                //Log.i("facebook_login","canceled");
                Toast.makeText(getApplicationContext(),"Something happened while Connecting to facebook",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                //Log.i("facebook_login","error : "+error);
                Toast.makeText(getApplicationContext(),"Error Connecting to facebook",Toast.LENGTH_SHORT).show();
            }
        });
    }
    /** call the upper facebook callback on reponce**/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


        /** causes facebook error */
        if(!iFfacebok){
            LISessionManager.getInstance(getApplicationContext())
                    .onActivityResult(this,
                            requestCode, resultCode, data);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            iFfacebok=false;
        }//quick fix

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("facebook_token", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            final FirebaseUser user = auth.getCurrentUser();
                            Log.d("photo",user.getPhotoUrl().toString());
                            Toast.makeText(LoginActivity.this,"Welcome "+user.getDisplayName(),
                                    Toast.LENGTH_SHORT).show();

                            //check if user exists in database by loging
                            UserProfileServices.getInstance().markLoggedUserWebService(user.getUid(), LoginActivity.this, new ServerCallbacks() {
                                @Override
                                public void onSuccess(JSONObject result) {

                                    User user;
                                    user = UserProfileServices.getInstance().get_user_from_json(result);
                                    user.setPictureUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                                    DataBaseHandler.getInstance(LoginActivity.this).saveUser(user);
                                    //Log.d("user",user.toString());
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onError(VolleyError result) {
                                    auth.signOut();
                                    auth.getCurrentUser().delete();
                                }

                                @Override
                                public void onWrong(JSONObject result) {

                                    Toast.makeText(LoginActivity.this, "registred",
                                            Toast.LENGTH_SHORT).show();
                                    //register as a new user
                                    UserProfileServices.getInstance().registerUserWebService(user.getUid(),user.getDisplayName(),user.getEmail(),LoginActivity.this,new ServerCallbacks(){

                                        @Override
                                        public void onSuccess(JSONObject result) {

                                            //start intent
                                            User user;
                                            user=UserProfileServices.getInstance().get_user_from_json(result);
                                            user.setPictureUrl(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
                                            DataBaseHandler.getInstance(LoginActivity.this).saveUser(user);
                                            //Log.d("user",user.toString());
                                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                            startActivity(intent);
                                            //clear backstack
                                            finishAffinity();
                                            finish();
                                        }

                                        @Override
                                        public void onError(VolleyError result) {
                                            auth.getCurrentUser().delete();
                                            Toast.makeText(LoginActivity.this,"Error while registring please retry",Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onWrong(JSONObject result) {
                                            auth.getCurrentUser().delete();
                                            Toast.makeText(LoginActivity.this,"Error while registring please retry",Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    public void printHash(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.androidprojects.esprit.ikotlin",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    /**
     * ---------- LINKEDIN LOGIN ---------
     **/

    public void loginWithLinkedin(View v){

        LISessionManager.getInstance(getApplicationContext())
                .init(this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess() {
                        /*Toast.makeText(getApplicationContext(), "success" +
                                        LISessionManager
                                                .getInstance(getApplicationContext())
                                                .getSession().getAccessToken().toString(),
                                Toast.LENGTH_LONG).show();*/
                    }

                    @Override
                    public void onAuthError(LIAuthError error) {
                        Toast.makeText(getApplicationContext(), "failed "
                                        + error.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                }, true);
        /** managing api responses **/
        String url = "https://api.linkedin.com/v1/people/~:(id,first-name,last-name,picture-url,email-address)";
        final APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(this, url, new ApiListener() {
            @Override
            public void onApiSuccess(ApiResponse apiResponse) {
                // create User object from the linkedin profile
                Gson gson = new Gson();
                final User userFromLinkedIn = gson.fromJson(apiResponse.getResponseDataAsJson().toString(),User.class);
                // check if this user exists in database ( through linkedin's is saved as its password if it has already signedup with linkedin)
                UserProfileServices.getInstance().markLoggedUserWebService(userFromLinkedIn.getId(), LoginActivity.this, new ServerCallbacks() {
                    // if yes, login with firebase
                    @Override
                    public void onSuccess(JSONObject result) {
                        Toast.makeText(LoginActivity.this,"User exists | will sign in with firebase",Toast.LENGTH_LONG).show();
                       auth.signInWithEmailAndPassword(userFromLinkedIn.getEmail(), userFromLinkedIn.getId()).addOnCompleteListener
                                (LoginActivity.this, new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "failed to login with linkedin", Toast.LENGTH_LONG).show();
                                        } else {
                                            Log.d("test 2",userFromLinkedIn.getPictureURL());
                                            loggedUser=userFromLinkedIn;
                                            Log.d("test",loggedUser.toString());
                                            Toast.makeText(LoginActivity.this, "logged in with linkedin email + id", Toast.LENGTH_LONG).show();
                                            //Toast.makeText(LoginActivity.this, "logged user: "+loggedUser.toString(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(VolleyError result) {
                        Log.d("error","error");
                    }

                    // if not, firebase signup + mysql signup
                    @Override
                    public void onWrong(JSONObject result) {
                        Toast.makeText(LoginActivity.this,"User does not exists will registerfirebase",Toast.LENGTH_LONG).show();
                        // add user to firebase
                       auth.createUserWithEmailAndPassword(userFromLinkedIn.getEmail(),userFromLinkedIn.getId())
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this,"REGITRED THROUGH FIREBASE",Toast.LENGTH_LONG).show();
                                            // add user to mySql db
                                            UserProfileServices.getInstance().registerUserWebService(userFromLinkedIn.getId(),userFromLinkedIn.getFirstName()+" "+userFromLinkedIn.getLastName(),userFromLinkedIn.getEmail(),LoginActivity.this,new ServerCallbacks(){
                                                @Override
                                                public void onSuccess(JSONObject result) {
                                                    User loggedU = UserProfileServices.getInstance().get_user_from_json(result);
                                                    loggedU.setPictureUrl(userFromLinkedIn.getPictureURL());
                                                    loggedUser=loggedU;
                                                    DataBaseHandler.getInstance(LoginActivity.this).saveUser(loggedUser);
                                                    Toast.makeText(LoginActivity.this,"ADDED TO DB",Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onError(VolleyError result) {
                                                    auth.getCurrentUser().delete();
                                                    Toast.makeText(LoginActivity.this,"Error while registring please retry",Toast.LENGTH_LONG).show();
                                                }

                                                @Override
                                                public void onWrong(JSONObject result) {
                                                    auth.getCurrentUser().delete();
                                                    Toast.makeText(LoginActivity.this,"Error while registring please retry",Toast.LENGTH_LONG).show();
                                                }
                                            });

                                        } else {
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                    }
                });
            }

            @Override
            public void onApiError(LIApiError liApiError) {
                Log.d("LINKEDIN API","Can't connect to linkedin");
            }
        });
    }
    /*** asking for linkedin account info retreive permission */
    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


}