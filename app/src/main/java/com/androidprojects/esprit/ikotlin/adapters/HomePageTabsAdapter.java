package com.androidprojects.esprit.ikotlin.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.androidprojects.esprit.ikotlin.fragments.RootFragment_compete;

import com.androidprojects.esprit.ikotlin.fragments.ConnectFragment;
import com.androidprojects.esprit.ikotlin.fragments.RootFragment_learn;
import com.androidprojects.esprit.ikotlin.fragments.RootFragment_share;


public class HomePageTabsAdapter extends FragmentPagerAdapter {

    private int NUM_ITEMS = 4;
    private String[] titles= new String[]{"Learn", "Share","Compete","Connect"};

        public HomePageTabsAdapter(FragmentManager fm) {
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
                /** because to switch fragments inside a tab we need a root FrameLayout,
                 in which we load fragments in each time ( getFragmentManager.replce(root,newFrag) )**/
                return new RootFragment_learn();
            case 1:
                return new RootFragment_share();
            case 2:
                return new RootFragment_compete();
            case 3:
                return new ConnectFragment();
            default:
                return null;
        }
    }

    /** needed for fragment refresh **/
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
