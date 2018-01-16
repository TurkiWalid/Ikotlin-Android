package com.androidprojects.esprit.ikotlin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.fragments.ChapterFragment;
import com.androidprojects.esprit.ikotlin.fragments.LearnFragment_course;
import com.androidprojects.esprit.ikotlin.utils.AllCourses;

/**
 * Created by Amal on 26/11/2017.
 */

public class ChapterAdapter extends BaseAdapter {


    /** this adapter is to load data dynamiacally in the linearLayout @+id/courseHeaderLayout from fragment_learn_course.xml **/

    private Context context;
    private int coursePosition,chapterPosition;

    public ChapterAdapter(Context context, int coursePosition, int chapterPosition){
        this.context=context;
        this.coursePosition=coursePosition;
        this.chapterPosition=chapterPosition;
    }

    @Override
    public int getCount() {
       return AllCourses.getCourse(coursePosition).getChaptersList().size();
    }

    @Override
    public Object getItem(int position) {
        return AllCourses.getCourse(coursePosition).getChaptersList().get(coursePosition+1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View rowView= ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.chapteradapter_item, parent, false);
        final String chapterTitle = AllCourses.getCourse(coursePosition).getChaptersList().get(chapterPosition).getTitle();
        ((TextView) rowView.findViewById(R.id.oneChapter_title)).setText(chapterTitle);
        ((WebView)(rowView.findViewById(R.id.chapterContentWebView))).loadUrl(getChapterContentFilePath());
        (( rowView.findViewById(R.id.chapterContentScrollView))).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener(){
             @Override public void onScrollChanged() {
                 /** if it's first chapter **/
                 if ((((ScrollView)(rowView.findViewById(R.id.chapterContentScrollView))).getChildAt(0).getBottom() <= (rowView.findViewById(R.id.chapterContentScrollView).getHeight() + rowView.findViewById(R.id.chapterContentScrollView).getScrollY()))) {
                     SharedPreferences badgesPrefs = context.getSharedPreferences("badgesTestPrefs",0);
                     /** if engaged in badge is not unlocked **/
                     if(!badgesPrefs.contains("engagedInBadge")){
                         /** --> unlock it **/
                         ChapterFragment.engagedInBadgeUnlockedAltert.show();
                         // .. ?
                         /** save this to sharedPrefs now for test, will move this DB lila :D **/
                         SharedPreferences.Editor coursesPrefsEditor = badgesPrefs.edit();
                         coursesPrefsEditor.putInt("engagedInBadge", 1);
                         coursesPrefsEditor.commit();
                         /** update course's state **/
                         AllCourses.getCourse(coursePosition).setBadgesEarnedNb(AllCourses.getCourse(coursePosition).getBadgesEarnedNb()+1);
                         AllCourses.getCourse(coursePosition).setCompletedChaptersNb(AllCourses.getCourse(coursePosition).getCompletedChaptersNb()+1);
                         LearnFragment_course.headerAdapter.notifyDataSetChanged();
                     }

                 }
             }
        });
        return rowView;
    }


    private String getChapterContentFilePath(){
        String chapterContentFilePath =null;
        /** course 1 **/
        if(coursePosition==0&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course1Content/C1_Chapter1Content.html";
        if(coursePosition==0&&chapterPosition==1)
            chapterContentFilePath="file:///android_asset/Course1Content/C1_Chapter2Content.html";
        if(coursePosition==0&&chapterPosition==2)
            chapterContentFilePath="file:///android_asset/Course1Content/C1_Chapter3Content.html";
        if(coursePosition==0&&chapterPosition==3)
            chapterContentFilePath="file:///android_asset/Course1Content/C1_Chapter4Content.html";
        /** course 2 **/
        if(coursePosition==1&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course2Content/C2_Chapter1Content.html";
        if(coursePosition==1&&chapterPosition==1)
            chapterContentFilePath="file:///android_asset/Course2Content/C2_Chapter2Content.html";
        if(coursePosition==1&&chapterPosition==2)
            chapterContentFilePath="file:///android_asset/Course2Content/C2_Chapter3Content.html";
        /** course 3 **/
        if(coursePosition==2&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course3Content/C3_Chapter1Content.html";
        if(coursePosition==2&&chapterPosition==1)
            chapterContentFilePath="file:///android_asset/Course3Content/C3_Chapter2Content.html";
        if(coursePosition==2&&chapterPosition==2)
            chapterContentFilePath="file:///android_asset/Course3Content/C3_Chapter3Content.html";
        if(coursePosition==2&&chapterPosition==3)
            chapterContentFilePath="file:///android_asset/Course3Content/C3_Chapter4Content.html";
        /** course 4 **/
        if(coursePosition==3&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter1Content.html";
        if(coursePosition==3&&chapterPosition==1)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter2Content.html";
        if(coursePosition==3&&chapterPosition==2)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter3Content.html";
        if(coursePosition==3&&chapterPosition==3)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter4Content.html";
        if(coursePosition==3&&chapterPosition==4)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter5Content.html";
        if(coursePosition==3&&chapterPosition==5)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter6Content.html";
        if(coursePosition==3&&chapterPosition==6)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter7Content.html";
        if(coursePosition==3&&chapterPosition==7)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter8Content.html";
        if(coursePosition==3&&chapterPosition==8)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter9Content.html";
        if(coursePosition==3&&chapterPosition==9)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter10Content.html";
        if(coursePosition==3&&chapterPosition==10)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter11Content.html";
        if(coursePosition==3&&chapterPosition==11)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter12Content.html";
        if(coursePosition==3&&chapterPosition==12)
            chapterContentFilePath="file:///android_asset/Course4Content/C4_Chapter13Content.html";
        /** chapter 5 **/
        if(coursePosition==4&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course5Content/C5_Chapter1Content.html";
        if(coursePosition==4&&chapterPosition==1)
            chapterContentFilePath="file:///android_asset/Course5Content/C5_Chapter2Content.html";
        if(coursePosition==4&&chapterPosition==2)
            chapterContentFilePath="file:///android_asset/Course5Content/C5_Chapter3Content.html";
        if(coursePosition==4&&chapterPosition==3)
            chapterContentFilePath="file:///android_asset/Course5Content/C5_Chapter4Content.html";
        /** course 6 **/
        if(coursePosition==5&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter1Content.html";
        if(coursePosition==5&&chapterPosition==1)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter2Content.html";
        if(coursePosition==5&&chapterPosition==2)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter3Content.html";
        if(coursePosition==5&&chapterPosition==3)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter4Content.html";
        if(coursePosition==5&&chapterPosition==4)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter5Content.html";
        if(coursePosition==5&&chapterPosition==5)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter6Content.html";
        if(coursePosition==5&&chapterPosition==6)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter7Content.html";
        if(coursePosition==5&&chapterPosition==7)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter8Content.html";
        if(coursePosition==5&&chapterPosition==8)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter9Content.html";
        if(coursePosition==5&&chapterPosition==9)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter10Content.html";
        if(coursePosition==5&&chapterPosition==10)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter11Content.html";
        if(coursePosition==5&&chapterPosition==11)
            chapterContentFilePath="file:///android_asset/Course6Content/C6_Chapter12Content.html";
        /** chapter 7 **/
        if(coursePosition==6&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course7Content/C7_Chapter1Content.html";
        if(coursePosition==6&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course7Content/C7_Chapter2Content.html";
        /** chapter 8 **/
        if(coursePosition==7&&chapterPosition==0)
            chapterContentFilePath="file:///android_asset/Course8Content/C8_Chapter1Content.html";
        if(coursePosition==7&&chapterPosition==1)
            chapterContentFilePath="file:///android_asset/Course8Content/C8_Chapter2Content.html";
        if(coursePosition==7&&chapterPosition==2)
            chapterContentFilePath="file:///android_asset/Course8Content/C8_Chapter3Content.html";
        if(coursePosition==7&&chapterPosition==3)
            chapterContentFilePath="file:///android_asset/Course8Content/C8_Chapter4Content.html";
        if(coursePosition==7&&chapterPosition==4)
            chapterContentFilePath="file:///android_asset/Course8Content/C8_Chapter5Content.html";
        if(coursePosition==7&&chapterPosition==5)
            chapterContentFilePath="file:///android_asset/Course8Content/C8_Chapter6Content.html";
        return chapterContentFilePath;
    }
}