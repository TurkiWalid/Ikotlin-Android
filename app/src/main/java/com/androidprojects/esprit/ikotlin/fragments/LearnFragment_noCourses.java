package com.androidprojects.esprit.ikotlin.fragments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.utils.AllCourses;
import com.androidprojects.esprit.ikotlin.utils.Statics;


public class LearnFragment_noCourses extends Fragment {


    private static Dialog dialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_learn_nocourses, container, false);
      /*  ArrayList<Integer> coursesPositions = new ArrayList<>();
         coursesPositions.add(0);coursesPositions.add(1);coursesPositions.add(2);*/
        this.dialog= Statics.createCoursesListDialog(getContext());   /** this holds the courses list,
         it's in Statics because we'll load it in other fragments not only this one ! **/
        v.findViewById(R.id.openCoursesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }

        });

        return v;
    }


}
