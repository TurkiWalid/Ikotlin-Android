package com.androidprojects.esprit.ikotlin.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.ProfileTabsAdapter;
import com.androidprojects.esprit.ikotlin.models.User;
import com.androidprojects.esprit.ikotlin.utils.DataBaseHandler;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ProfileTabsAdapter adapter;
    private ViewPager viewPager;
    private User user;

    ImageButton profileSettingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*** disabling actionBar ****/
        getSupportActionBar().hide();

        //user=LoginActivity.loggedUser; /** why not using sqlite ?*/
        //user=FirebaseAuth.getInstance().getCurrentUser();

        /** the tabLayout **/
        tabLayout = findViewById(R.id.profileTabs);
        viewPager = findViewById(R.id.viewpager_profileTabs);
        profileSettingsBtn=findViewById(R.id.profileSettingsBtn);
        adapter = new ProfileTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        if(UserProfileServices.getInstance().isFacebooklogged(this)) profileSettingsBtn.setVisibility(View.GONE);
        else
        /*** profile settings click ***/
            profileSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),ProfileSettingsActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        User user2 = user;
        user= DataBaseHandler.getInstance(getApplicationContext()).getUser();
        if(user!=null){
            /** fields data **/
            if(user2 == null || !user.getUsername().equals(user2.getUsername()))
            ((TextView) findViewById(R.id.fullNameInProfile)).setText((user.getUsername().isEmpty()?user.getFirstName():user.getUsername()));
            //Log.d("profileI","im here");
            //Log.d("profileI",user.toString());
            if(user2 == null) {
                if (user.getPictureURL() != null) {
                    Picasso.with(getApplicationContext()).load(Uri.parse(user.getPictureURL())).into((ImageView) findViewById(R.id.userImgProfile));
                } else
                    ((ImageView) findViewById(R.id.userImgProfile)).setImageDrawable(UserProfileServices.getInstance().getEmptyProfimePicture(user.getUsername()));
            }
            else {
                if (user.getPictureURL() != null) {
                    Picasso.with(getApplicationContext()).load(Uri.parse(user.getPictureURL())).into((ImageView) findViewById(R.id.userImgProfile));
                } else
                    ((ImageView) findViewById(R.id.userImgProfile)).setImageDrawable(UserProfileServices.getInstance().getEmptyProfimePicture(user.getUsername()));
            }

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
