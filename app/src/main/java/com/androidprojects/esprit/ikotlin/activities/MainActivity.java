package com.androidprojects.esprit.ikotlin.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.MainUiPagerAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.rey.material.app.Dialog;

import me.relex.circleindicator.CircleIndicator;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity implements View.OnClickListener  {

    private ViewPager vp;
    private CircleIndicator circlePageIndicator;
    private FirebaseAuth auth;
    public static Dialog coursesListDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
        /** check if user is connected **/
        auth=FirebaseAuth.getInstance();
            if(auth.getCurrentUser()!=null){
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();

            }

        /*** Set the page indicator ***/
        vp = findViewById(R.id.vwpMain);
        circlePageIndicator = findViewById(R.id.circlePageIndicator);
        vp.setAdapter(new MainUiPagerAdapter(getApplicationContext()));
        vp.setCurrentItem(0);
        circlePageIndicator.setViewPager(vp);

    }

    /*** login and signup buttons clicks ***/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case(R.id.loginBtn):
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                break;
            case (R.id.signupBtn):
                startActivity(new Intent(getApplicationContext(),SignupActivity.class));
        }
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
