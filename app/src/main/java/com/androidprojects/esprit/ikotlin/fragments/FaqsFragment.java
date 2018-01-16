package com.androidprojects.esprit.ikotlin.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.FaqsListAdapter;

public class FaqsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_faqs, container, false);
        ((ExpandableListView)v.findViewById(R.id.faqsListView)).setAdapter(new FaqsListAdapter(getContext()));
        return v;
    }

}
