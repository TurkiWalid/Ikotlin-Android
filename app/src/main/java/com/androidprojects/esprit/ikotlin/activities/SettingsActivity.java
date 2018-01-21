package com.androidprojects.esprit.ikotlin.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.SettingsListAdapter;
import com.androidprojects.esprit.ikotlin.fragments.ShareFragment;
import com.androidprojects.esprit.ikotlin.webservices.ForumServices;
import com.androidprojects.esprit.ikotlin.webservices.ServerCallbacks;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONObject;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends ListActivity {

    private FirebaseAuth auth;
    ProgressDialog progressDialog;

    private static final String[] settingsContent = {
            "Sep_ACCOUNT",
            "Edit profile",
            "Change password",
            "Sep_",
            "Sign out"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //getActionBar().setTitle((Html.fromHtml("<font color=\"#e99631\" face=\"serif\" face=\"serif\">" + "<b>    Settings</b>" + "</font>")));

        getActionBar().setDisplayShowCustomEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);
        final View v = LayoutInflater.from(this).inflate(R.layout.actionbartitle_view, null);
        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Settings");
        ((TextView)v.findViewById(R.id.actionBarTitle)).setGravity(Gravity.CENTER_VERTICAL);
        getActionBar().setCustomView(v);

        setListAdapter(new SettingsListAdapter(this,settingsContent));
        auth=FirebaseAuth.getInstance();
    }

    @Override
    protected void onListItemClick(ListView l, View v,int position,long id){
      if(position==4){
                initSignOut();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void initSignOut(){
        new AlertDialog.Builder(this)
                //set message, title, and icon
                .setTitle("Sign-out")
                .setMessage("Do you really want to Sign-out")
                .setIcon(R.drawable.ic_action_logout)
                .setPositiveButton("Sign-out now", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int whichButton) {
                        signOut();
                    }

                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void signOut(){
        auth.signOut();
        //delete user from sqlite
        UserProfileServices.getInstance().logout(this);
        //clear backstack
        finishAffinity();
        //start intent
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
