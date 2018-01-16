package com.androidprojects.esprit.ikotlin.utils;

import android.content.Context;
import android.widget.ExpandableListView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.CoursesListAdapter;
import com.rey.material.app.Dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Amal on 30/11/2017.
 */

public class Statics {

    /**
     *  to keep all static references
     *  to avoid instanciating them 2+ times in diffrents activitis/fragments
     */

    static List<String> courses=new ArrayList<>();
    static HashMap<String,List> chapters= new HashMap<>();

    public static Dialog createCoursesListDialog(Context context){   /** attached listcourses_view ui to the dialog (check CoursesListAdapter..)**/
        Dialog dialog = new Dialog(context);
        int[] icons = new int[]{R.drawable.ic_overview,R.drawable.ic_start,R.drawable.ic_basics,R.drawable.ic_classesobjects,
                R.drawable.ic_functions,R.drawable.ic_others,R.drawable.ic_java,R.drawable.ic_javascript};
        dialog.setContentView(R.layout.listcourses_view);
        ExpandableListView lvCourses=dialog.findViewById(R.id.expandableLvw);
        /*ArrayList<Integer> coursesPositions = new ArrayList<>();
        coursesPositions.add(0);coursesPositions.add(1);coursesPositions.add(2);
        getCoursesListData(coursesPositions);*/
       prepareCoursesListData();
        lvCourses.setAdapter(new CoursesListAdapter(context,courses,chapters,icons));
        return dialog;
    }

    public static void prepareCoursesListData(){
        courses = new ArrayList<String>();
        chapters = new HashMap<String, List>();

        courses.add("Overview");
        courses.add("Getting started");
        courses.add("Basics");
        courses.add("Classes and objects");
        courses.add("Functions and Lambdas");
        courses.add("Others");
        courses.add("Java Interop");
        courses.add("Javascript");

        List overviewCourse = new ArrayList();
        overviewCourse.add("Kotlin for Server side");
        overviewCourse.add("Kotlin for Android");
        overviewCourse.add("Kotlin for Javascript");
        overviewCourse.add("Kotlin/Native");

        List gettingStartedCourse = new ArrayList();
        gettingStartedCourse.add("Basic syntax");
        gettingStartedCourse.add("Idioms");
        gettingStartedCourse.add("Coding conventions");

        List basicsCourse = new ArrayList();
        basicsCourse.add("Basics Types");
        basicsCourse.add("Packages and imports");
        basicsCourse.add("Control flow");
        basicsCourse.add("Returns and jumps");

        List classesAndObjectsCourse = new ArrayList();
        classesAndObjectsCourse.add("Classes and inheritance");
        classesAndObjectsCourse.add("Properties and fields");
        classesAndObjectsCourse.add("Interfaces");
        classesAndObjectsCourse.add("Visibility modifiers");
        classesAndObjectsCourse.add("Extensions");
        classesAndObjectsCourse.add("Data classes");
        classesAndObjectsCourse.add("Sealed classes");
        classesAndObjectsCourse.add("Generics");
        classesAndObjectsCourse.add("Nested classes");
        classesAndObjectsCourse.add("Enum classes");
        classesAndObjectsCourse.add("Objects");
        classesAndObjectsCourse.add("Delegation");
        classesAndObjectsCourse.add("Delegated properties");

        List functionsAnLambdasCourse = new ArrayList();
        functionsAnLambdasCourse.add("Functions");
        functionsAnLambdasCourse.add("Lambdas");
        functionsAnLambdasCourse.add("Inline functions");
        functionsAnLambdasCourse.add("Coroutines");

        List othersCourse = new ArrayList();
        othersCourse.add("Destructuring declarations");othersCourse.add("Collections");othersCourse.add("Ranges");othersCourse.add("Type Checks and casts");othersCourse.add("This expressions");
        othersCourse.add("Equality"); othersCourse.add("Sealed classes");othersCourse.add("Operator overloading");othersCourse.add("Null safety");othersCourse.add("Exceptions");othersCourse.add("Annotations");
        othersCourse.add("Reflection");othersCourse.add("Type-safety builders");othersCourse.add("Types Aliases");othersCourse.add("Multiplatform projects");

        List javaInteropCourse = new ArrayList();
        javaInteropCourse.add("Calling Java from Kotlin");
        javaInteropCourse.add("Calling Kotlin frm Java");

        List javascriptCourse = new ArrayList();
        javascriptCourse.add("Dynamic Type");
        javascriptCourse.add("Calling JavaScript from Kotlin");
        javascriptCourse.add("Calling Kotlin from JavaScript");
        javascriptCourse.add("Javascript modules");
        javascriptCourse.add("Javascript reflection");
        javascriptCourse.add("Javascript DCE");
        chapters.put(courses.get(0), overviewCourse);
        chapters.put(courses.get(1), gettingStartedCourse);
        chapters.put(courses.get(2), basicsCourse);
        chapters.put(courses.get(3),classesAndObjectsCourse);
        chapters.put(courses.get(4),functionsAnLambdasCourse);
        chapters.put(courses.get(5),othersCourse);
        chapters.put(courses.get(6),javaInteropCourse);
        chapters.put(courses.get(7),javascriptCourse);

    }

    /*static void getCoursesListData(ArrayList<Integer> coursesPositions){
        // prepare chapters
        List overviewCourse = new ArrayList();
        overviewCourse.add("Kotlin for Server side");
        overviewCourse.add("Kotlin for Android");
        overviewCourse.add("Kotlin for Javascript");
        overviewCourse.add("Kotlin/Native");

        List gettingStartedCourse = new ArrayList();
        gettingStartedCourse.add("Basic syntax");
        gettingStartedCourse.add("Idioms");
        gettingStartedCourse.add("Coding conventions");

        List basicsCourse = new ArrayList();
        basicsCourse.add("Basics Types");
        basicsCourse.add("Packages and imports");
        basicsCourse.add("Control flow");
        basicsCourse.add("Returns and jumps");

        List classesAndObjectsCourse = new ArrayList();
        classesAndObjectsCourse.add("Classes and inheritance");
        classesAndObjectsCourse.add("Properties and fields");
        classesAndObjectsCourse.add("Interfaces");
        classesAndObjectsCourse.add("Visibility modifiers");
        classesAndObjectsCourse.add("Extensions");
        classesAndObjectsCourse.add("Data classes");
        classesAndObjectsCourse.add("Sealed classes");
        classesAndObjectsCourse.add("Generics");
        classesAndObjectsCourse.add("Nested classes");
        classesAndObjectsCourse.add("Enum classes");
        classesAndObjectsCourse.add("Objects");
        classesAndObjectsCourse.add("Delegation");
        classesAndObjectsCourse.add("Delegated properties");

        List functionsAnLambdasCourse = new ArrayList();
        functionsAnLambdasCourse.add("Functions");
        functionsAnLambdasCourse.add("Lambdas");
        functionsAnLambdasCourse.add("Inline functions");
        functionsAnLambdasCourse.add("Coroutines");

        List othersCourse = new ArrayList();
        othersCourse.add("Destructuring declarations");othersCourse.add("Collections");othersCourse.add("Ranges");othersCourse.add("Type Checks and casts");othersCourse.add("This expressions");
        othersCourse.add("Equality"); othersCourse.add("Sealed classes");othersCourse.add("Operator overloading");othersCourse.add("Null safety");othersCourse.add("Exceptions");othersCourse.add("Annotations");
        othersCourse.add("Reflection");othersCourse.add("Type-safety builders");othersCourse.add("Types Aliases");othersCourse.add("Multiplatform projects");

        List javaInteropCourse = new ArrayList();
        javaInteropCourse.add("Calling Java from Kotlin");
        javaInteropCourse.add("Calling Kotlin frm Java");

        List javascriptCourse = new ArrayList();
        javascriptCourse.add("Dynamic Type");
        javascriptCourse.add("Calling JavaScript from Kotlin");
        javascriptCourse.add("Calling Kotlin from JavaScript");
        javascriptCourse.add("Javascript modules");
        javascriptCourse.add("Javascript reflection");
        javascriptCourse.add("Javascript DCE");

        ArrayList<List> chaptersLists = new ArrayList<>();
        chaptersLists.add(overviewCourse);chaptersLists.add(gettingStartedCourse);chaptersLists.add(basicsCourse);
        chaptersLists.add(classesAndObjectsCourse);chaptersLists.add(functionsAnLambdasCourse);chaptersLists.add(othersCourse);
        chaptersLists.add(javaInteropCourse);chaptersLists.add(javascriptCourse);
        // link chapters to courses
        for(int i=0;i<coursesPositions.size();i++){
            courses.add(AllCourses.getCourse(i).getTitle());
            chapters.put(courses.get(i),chaptersLists.get(i));
        }
    }*/

}
