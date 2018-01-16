package com.androidprojects.esprit.ikotlin.activities;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.SettingsListAdapter;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.google.firebase.auth.FirebaseAuth;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends ListActivity {

    private FirebaseAuth auth;

    private static final String[] settingsContent = {
            "Sep_ACCOUNT",
            "Edit profile",
            "Change password",
            "Connected accounts",
            "Sep_SETTINGS",
            "Activity Feed",
            "Push notifications",
            "Language",
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
      if(position==9){
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
