package com.androidprojects.esprit.ikotlin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidprojects.esprit.ikotlin.R;

public class RootFragment_share extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.root_share_fragment,new ShareFragment()).commit();
        //Log.d("pager","root created");
        return inflater.inflate(R.layout.fragment_root_share, container, false);
    }


}
