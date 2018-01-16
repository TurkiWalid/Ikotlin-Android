package com.androidprojects.esprit.ikotlin.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.ProfileTabsAdapter;
import com.androidprojects.esprit.ikotlin.models.User;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.squareup.picasso.Picasso;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ProfileTabsAdapter adapter;
    private ViewPager viewPager;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /*** disabling actionBar ****/
        getSupportActionBar().hide();

        user=LoginActivity.loggedUser;
        //user=FirebaseAuth.getInstance().getCurrentUser();
        //user= DataBaseHandler.getInstance(getApplicationContext()).getUser();

        /** the tabLayout **/
        tabLayout = findViewById(R.id.profileTabs);
        viewPager = findViewById(R.id.viewpager_profileTabs);
        adapter = new ProfileTabsAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        /*** profile settings click ***/
        findViewById(R.id.profileSettingsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),ProfileSettingsActivity.class));
            }
        });


        if(user!=null){
            /** fields data **/
            ((TextView) findViewById(R.id.fullNameInProfile)).setText((user.getUsername().isEmpty()?user.getFirstName():user.getUsername()));
            //Log.i("user",user.toString());

            if(user.getPictureURL()!=null){
                Picasso.with(getApplicationContext()).load(Uri.parse(user.getPictureURL())).into((ImageView)findViewById(R.id.userImgProfile));
            }
            else
                ((ImageView)findViewById(R.id.userImgProfile)).setImageDrawable(UserProfileServices.getInstance().getEmptyProfimePicture(user.getUsername()));
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
