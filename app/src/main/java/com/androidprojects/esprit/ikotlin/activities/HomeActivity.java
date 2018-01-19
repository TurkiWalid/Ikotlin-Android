package com.androidprojects.esprit.ikotlin.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.HomePageTabsAdapter;
import com.androidprojects.esprit.ikotlin.fragments.ForumShowFragment;
import com.androidprojects.esprit.ikotlin.models.ForumQuestion;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeActivity extends AppCompatActivity {

    private static TabLayout tablayout;
    private static ViewPager vpPager;
    private HomePageTabsAdapter adapter;
    private static ColorMatrixColorFilter filter;
    private ColorMatrix matrix;
    private FragmentManager fragmentManager;

    public boolean chageToTab=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fragmentManager =getSupportFragmentManager();

        /** will be used to change tab icons colors on select/deselect */
        matrix = new ColorMatrix();
        matrix.setSaturation(0);
        filter = new ColorMatrixColorFilter(matrix);

        /** setting the actionBar **/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final View v = LayoutInflater.from(this).inflate(R.layout.actionbartitle_view, null);
        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Learn");
        getSupportActionBar().setCustomView(v);
        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#e99631\"  face=\"serif\">" + "<b>Learn</b>" + "</font>")));
        getSupportActionBar().setElevation(0);

        /*** setting the tabsLayout ***/
        adapter = new HomePageTabsAdapter(getSupportFragmentManager());
        vpPager = findViewById(R.id.viewpager);
        vpPager.setAdapter(adapter);
        vpPager.setCurrentItem(0);
        vpPager.setOffscreenPageLimit(3);
        tablayout=findViewById(R.id.tabsLayout);
        tablayout.setupWithViewPager(vpPager);
        setUpTabIcons(tablayout);

        /** change title in actionBar depending on tabSelected **/
        tablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                switch(tablayout.getSelectedTabPosition()) {
                    case 0:
                        tab.getIcon().clearColorFilter();
                        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Learn");
                        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#e99631\" face=\"serif\">" + "<b>Learn</b>" + "</font>")));
                        break;
                    case 1:
                        tab.getIcon().clearColorFilter();
                        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Share");
                        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#e99631\" face=\"serif\">" + "<b>Share</b>" + "</font>")));
                        break;
                    case 2:
                        tab.getIcon().clearColorFilter();
                        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Compete");
                        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#e99631\" face=\"serif\">" + "<b>Compete</b>" + "</font>")));
                        break;
                    case 3:
                        tab.getIcon().clearColorFilter();
                        ((TextView)v.findViewById(R.id.actionBarTitle)).setText("Connect");
                        //getSupportActionBar().setTitle((Html.fromHtml("<font color=\"#e99631\" face=\"serif\">" + "<b>Connect</b>" + "</font>")));
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(filter);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // setting default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/open_sans_light.ttf")
                .build()
        );

        //init codeview
        //CodeProcessor.init(this);
        tablayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                /** set pager postition if needed (share view) */
                if(getIntent().hasExtra("forum") && chageToTab &&  vpPager.getCurrentItem()==1){
                    chageToTab=false;
                    final ForumShowFragment fs =new ForumShowFragment();
                    ForumQuestion f = new ForumQuestion();
                    f.setId(getIntent().getIntExtra("forum",0));
                    fs.setF(f);

                    //Log.d("pager","looking for root");
                    getSupportFragmentManager().beginTransaction()
                        .replace(R.id.root_share_fragment,fs)
                        .addToBackStack(null)
                        .commit();
                }
            }
        });

    }

    /*** menu [profile,notifs,settings] ***/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_profile){
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }
        if(item.getItemId()==R.id.action_settings){
            startActivity(new Intent(getApplicationContext(),SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    /*** helper methods ***/
    private static void allTabIconsToDeselected(TabLayout tablayout){
        for(int i=1;i<4;i++){
            tablayout.getTabAt(i).getIcon().setColorFilter(filter);
        }
    }
    private static void setUpTabIcons(TabLayout tbs){
        tbs.getTabAt(0).setIcon(R.drawable.ic_learn_icon_tab0);
        tbs.getTabAt(1).setIcon(R.drawable.ic_share_icon_tab1);
        tbs.getTabAt(2).setIcon(R.drawable.ic_compete_icon_tab2);
        tbs.getTabAt(3).setIcon(R.drawable.ic_connect_icon_tab3);
        allTabIconsToDeselected(tbs);
    }
    public static void refresh(){
        vpPager.getAdapter().notifyDataSetChanged();
       setUpTabIcons(tablayout);
    }

    /** to prevent coming back to loginActivity **/
    @Override
    public void onBackPressed() {
           fragmentManager.popBackStack();
    }

    /** calligraphy lib **/
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /** btn clicks on ConnectFragment **/
    public void imgBtnsClick(View v) {

        switch (v.getId()) {
            //github
            case R.id.developer1GitBtn:
                Intent igit1 = new Intent(Intent.ACTION_VIEW);
                igit1.setData(Uri.parse("https://github.com/AmalH"));
                startActivity(igit1);
                break;
            case R.id.developer2GitBtn:
                Intent igit2 = new Intent(Intent.ACTION_VIEW);
                igit2.setData(Uri.parse("https://github.com/TurkiWalid"));
                startActivity(igit2);
                break;
            // linkedinhttps://www.linkedin.com/in/amalhichri/
            case R.id.developer1LinkedInBtn:
                Intent ilkdn1 = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://amalhichri"));
                final PackageManager packageManager = getApplicationContext().getPackageManager();
                final List<ResolveInfo> list = packageManager.queryIntentActivities(ilkdn1, PackageManager.MATCH_DEFAULT_ONLY);
                if (list.isEmpty()) {
                    ilkdn1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/amalhichri/"));
                }
                startActivity(ilkdn1);
                break;
            case R.id.developer2LinkedInBtn:
                Intent ilkdn2 = new Intent(Intent.ACTION_VIEW, Uri.parse("linkedin://turki-walid"));
                final PackageManager packageManager2 = getApplicationContext().getPackageManager();
                final List<ResolveInfo> list2 = packageManager2.queryIntentActivities(ilkdn2, PackageManager.MATCH_DEFAULT_ONLY);
                if (list2.isEmpty()) {
                    ilkdn2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/turki-walid/"));
                }
                startActivity(ilkdn2);
                break;
            //twitter
            case R.id.developer1TwitterkBtn:
                Intent intent = null;
                try {
                    this.getPackageManager().getPackageInfo("com.twitter.android", 0);
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=1541480119"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                } catch (Exception e) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/amalhichrii"));
                }
                this.startActivity(intent);
                break;
            case R.id.developer2TwitterkBtn:

                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getIntent().hasExtra("tab")) {
            int t = getIntent().getIntExtra("tab", adapter.getCount() - 1);
            vpPager.setCurrentItem(t);
            chageToTab=true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

       /* new MaterialTapTargetPrompt.Builder(HomeActivity.this)
                .setTarget(findViewById(R.id.tabsLayout))
                .setPrimaryText("Send your first email")
                .setSecondaryText("Tap the envelop to start composing your first email")
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                {
                    @Override
                    public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                    {
                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                        {
                            // User has pressed the prompt target
                        }
                    }
                })
                .show();*/
    }
}
