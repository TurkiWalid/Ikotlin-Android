package com.androidprojects.esprit.ikotlin.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidprojects.esprit.ikotlin.R;


public class RootFragment_compete extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.root_compete,new FragmentCompeteMain()).commit();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compete_root, container, false);
    }

}
