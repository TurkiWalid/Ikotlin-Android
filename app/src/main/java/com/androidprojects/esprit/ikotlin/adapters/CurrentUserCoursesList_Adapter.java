package com.androidprojects.esprit.ikotlin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.models.Course;
import com.daimajia.numberprogressbar.NumberProgressBar;

import java.util.ArrayList;

;

/**
 * Created by Amal on 29/11/2017.
 */

public class CurrentUserCoursesList_Adapter extends BaseAdapter {

    /** adapter for the the list to be shown if user is enrolled to courses **/

    private Context context;
    private ArrayList<Course> currentUserCourses;

    public CurrentUserCoursesList_Adapter(Context context, ArrayList<Course> currentUserCourses){
        this.context=context;
        this.currentUserCourses=currentUserCourses;
    }


    @Override
    public int getCount() {
        return currentUserCourses.size();
    }

    @Override
    public Object getItem(int position) {
        return currentUserCourses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    /*** if reset list is clicked : remove listItem
     * + update Statics.loggedUser_startedLearning in case all items are removed !
     * + perform course rest in DB
     * ***/
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View rowView= ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.currentusercourses_list_item,parent, false);
        ((ImageView)rowView.findViewById(R.id.userCourseIcon)).setImageResource(currentUserCourses.get(position).getIconId());
        ((TextView)rowView.findViewById(R.id.userCourseTitle)).setText(currentUserCourses.get(position).getTitle());
        ((NumberProgressBar)rowView.findViewById(R.id.userCourseProgress)).setProgress(currentUserCourses.get(position).getAdvancement());
        /*((ListItemView) rowView.findViewById(R.id.userCoursesListItem )).setOnMenuItemClickListener(new ListItemView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(final MenuItem item) {
                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Reset course?")
                        .setContentText("Want to restart learning?\nAll scores and badges will be lost !")
                        .setCancelText("No,cancel plx!")
                        .setConfirmText("Yes,reset!")
                        .showCancelButton(true)
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog
                                        .setTitleText("Cancelled!")
                                        .setContentText("Your course milestone is safe.\nContinue the journey !")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(null)
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                            }
                        })
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(final SweetAlertDialog sDialog) {
                                /**  show the dialog
                                sDialog.setTitleText("Course reset!")
                                        .setContentText("Your course milestone has been reset.\nYou will perform better this time !")
                                        .setConfirmText("OK")
                                        .showCancelButton(false)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sDialog.cancel();
                                                /** update  Statics.loggedUser_startedLearning in case all items are removed ! *
                                                if(LearnFragment_currentUserCourses.currentUserCourses.isEmpty()){
                                                    // notify the home tabLayout adapter
                                                    // to be able load LearFragment_noCourses ui in case Statics.loggedUser_startedLearning=false;
                                                    HomeActivity.refresh();
                                                }
                                            }
                                        })
                                        .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
                                /** remove listItem
                                LearnFragment_currentUserCourses.currentUserCourses.remove(position);
                                LearnFragment_currentUserCourses.listAdapter.notifyDataSetChanged();
                                /** perform course reset in DB
                                // to be finished later
                            }
                        })
                        .show();
            }
        });*/
        return rowView;
    }
}
