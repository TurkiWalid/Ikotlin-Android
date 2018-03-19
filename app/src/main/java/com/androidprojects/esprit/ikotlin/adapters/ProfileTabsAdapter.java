package com.androidprojects.esprit.ikotlin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.androidprojects.esprit.ikotlin.fragments.BadgesFragment;
import com.androidprojects.esprit.ikotlin.fragments.PostsFragment;
import com.androidprojects.esprit.ikotlin.fragments.SkillsFragment;

/**
 * Created by Amal on 11/11/2017.
 */

public class ProfileTabsAdapter extends FragmentPagerAdapter {

    private int NUM_ITEMS = 1;
    private String[] titles= new String[]{"Posts"};

    public ProfileTabsAdapter(FragmentManager fm) {
        super(fm);
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return  NUM_ITEMS ;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PostsFragment();
            default:
                return null;
        }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return  titles[position];
    }
}
