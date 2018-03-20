package com.androidprojects.esprit.ikotlin.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.adapters.PostsAdapter;
import com.androidprojects.esprit.ikotlin.adapters.ShareListAdapter;
import com.androidprojects.esprit.ikotlin.models.ForumQuestion;
import com.androidprojects.esprit.ikotlin.utils.Configuration;
import com.androidprojects.esprit.ikotlin.webservices.ForumServices;
import com.androidprojects.esprit.ikotlin.webservices.ServerCallbacks;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PostsFragment extends Fragment {

    public RecyclerView forumRececyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    public RecyclerView.Adapter adapter;
    public RecyclerView.LayoutManager layoutManager;
    public ArrayList<ForumQuestion> listForum;
    public TextView noConenction_textView;
    private static int loaded_length;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loaded_length=0;
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posts, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loaded_length=0;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /** affect list **/
        forumRececyclerView = getActivity().findViewById(R.id.forum_recycler_view_posts);
        adapter = new ShareListAdapter(new ArrayList<ForumQuestion>(),getActivity());
        forumRececyclerView.setAdapter(adapter);
        layoutManager = new LinearLayoutManager(getContext());
        forumRececyclerView.setLayoutManager(layoutManager);
        listForum=new ArrayList<>();
        loaded_length=0;
        DefaultItemAnimator animator = new DefaultItemAnimator() {
            @Override
            public boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder) {
                return true;
            }
        };
        forumRececyclerView.setItemAnimator(animator);
        //Affect views
        noConenction_textView = getActivity().findViewById(R.id.no_connection_postsFragment);
        swipeRefreshLayout = getActivity().findViewById(R.id.posts_refresh);


        swipeRefreshLayout.setColorSchemeColors(
                getContext().getResources().getColor(R.color.refresh_progress_1),
                getContext().getResources().getColor(R.color.refresh_progress_2),
                getContext().getResources().getColor(R.color.refresh_progress_3));


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Configuration.isOnline(getContext())){
                    loaded_length=0;
                    //Log.d("loaded",loaded_length+"by refresh");
                    load_forum(0);
                }
                else
                {
                    noConenction_textView.setVisibility(View.VISIBLE);
                    forumRececyclerView.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    loading=false;
                }
            }
        });

        //add scrollListener to the recycler view
        attach_scrollListener();
        load_forum(0);
    }

    /** this method load forum items and push it to showList **/
    public synchronized void  load_forum(final int start_at){
        if(start_at==0)
        {
            loaded_length=0;
            forumRececyclerView.removeAllViews();
            listForum.clear();
        }
        swipeRefreshLayout.setRefreshing(true);
        //Toast.makeText(getContext(),"Loaded : "+loaded_length,Toast.LENGTH_LONG).show();
        ForumServices.getInstance().getusersForums(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                getActivity().getApplicationContext(),start_at,
                new ServerCallbacks() {
                    @Override
                    public void onSuccess(JSONObject result) {
                        boolean goShow=true;
                        JSONArray array = new JSONArray();
                        try {
                            array = result.getJSONArray("forum");
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"Server error while loading forum , please report",Toast.LENGTH_SHORT).show();
                            goShow=false;
                        }
                        for(int i = 0 ; i < array.length() ; i++){
                            try {
                                /** parse forum and add it to the arraylist**/
                                listForum.add(ForumServices.parse_(array.getJSONObject(i)));
                            } catch (JSONException e) {
                                Toast.makeText(getContext(),"Application error while loading forum , please report",Toast.LENGTH_SHORT).show();
                                goShow=false;
                            }
                        }

                        /** All the work will be here **/
                        if(goShow) {
                            noConenction_textView.setVisibility(View.GONE);
                            forumRececyclerView.setVisibility(View.VISIBLE);
                            if(loaded_length==0){
                                //Log.d("loaded",loaded_length+"new load");
                                adapter=new PostsAdapter(listForum,getContext());
                                forumRececyclerView.setAdapter(adapter);
                            }
                            else{
                                //Log.d("loaded",loaded_length+"old load : "+loaded_length+"     "+forumRececyclerView+"   "+adapter);
                                if(adapter==null){
                                    adapter=new PostsAdapter(listForum,getContext());
                                    forumRececyclerView.setAdapter(adapter);
                                }
                                else
                                    adapter.notifyDataSetChanged();
                            }
                            //addCalculated
                            if(loaded_length==0) loaded_length+=15;
                            else
                                loaded_length+=10;
                        }
                        else{
                            noConenction_textView.setVisibility(View.VISIBLE);
                            forumRececyclerView.setVisibility(View.GONE);
                        }
                        if(listForum.size()==0){
                            noConenction_textView.setVisibility(View.VISIBLE);
                            forumRececyclerView.setVisibility(View.GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        loading=false;
                    }

                    @Override
                    public void onError(VolleyError result) {
                        noConenction_textView.setVisibility(View.VISIBLE);
                        forumRececyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        loading=false;
                    }

                    @Override
                    public void onWrong(JSONObject result) {
                        noConenction_textView.setVisibility(View.VISIBLE);
                        forumRececyclerView.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
                        loading=false;
                    }
                });
    }

    public void attach_scrollListener(){
        loading =false;
        forumRececyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager)layoutManager).findFirstVisibleItemPosition();

                    if (Configuration.isOnline(getContext())&& !loading)
                    {
                        if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount-4)
                        {
                            loading=true;
                            if(adapter!=null)
                                //Log.d("loaded",loaded_length+"by search");
                                load_forum(loaded_length);
                            // //Log.d("loading","loading_more");
                        }
                    }
                }
            }
        });
    }

}
