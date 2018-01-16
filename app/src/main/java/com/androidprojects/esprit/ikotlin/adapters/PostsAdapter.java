package com.androidprojects.esprit.ikotlin.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.activities.HomeActivity;
import com.androidprojects.esprit.ikotlin.models.ForumQuestion;
import com.androidprojects.esprit.ikotlin.webservices.ForumServices;
import com.androidprojects.esprit.ikotlin.webservices.ServerCallbacks;
import com.androidprojects.esprit.ikotlin.webservices.UserProfileServices;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import me.originqiu.library.FlowLayout;

/**
 * Created by Odil on 01/01/2018.
 */

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsItem_ViewHolder> {

    ArrayList<ForumQuestion> forumQuestionsList= new ArrayList<>();
    Context context;
    String userid;

    public PostsAdapter(ArrayList<ForumQuestion> forum_list, Context context){
        this.forumQuestionsList=forum_list;
        this.context=context;
        userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public PostsAdapter.PostsItem_ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.my_posts_list_item,parent,false);
        PostsAdapter.PostsItem_ViewHolder postItem_viewHolder = new PostsItem_ViewHolder(view);

        return postItem_viewHolder;
    }


    @Override
    public void onBindViewHolder(final PostsAdapter.PostsItem_ViewHolder holder, final int position) {
        holder.title.setText(forumQuestionsList.get(position).getSubject());
        holder.nbviews.setText(forumQuestionsList.get(position).getViews_string());
        holder.createed.setText(forumQuestionsList.get(position).getCreated_string());

        String item=forumQuestionsList.get(position).getRatingString();
        holder.rating_picture.setImageDrawable(UserProfileServices.getInstance().getRatingINPicture(item));

        //split tags
        String[] array = forumQuestionsList.get(position).getTags().split(",");

        //init tags layout
        holder.tags_layout.removeAllViews();
        //fill tags
        for(String s : array){
            TextView t = new TextView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5,5,5,5);
            t.setLayoutParams(lp);
            t.setText(s);
            t.setBackgroundColor(context.getResources().getColor(R.color.material_deep_teal_50));
            t.setTextColor(context.getResources().getColor(R.color.cardview_light_background));
            t.setGravity(View.TEXT_ALIGNMENT_CENTER);
            t.setPaddingRelative(5,3,5,3);
            t.setTextSize(TypedValue.COMPLEX_UNIT_DIP ,13);
            holder.tags_layout.addView(t);
        }
        //view content
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity) context;


                ForumServices.getInstance().markViewForum(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        context, forumQuestionsList.get(position).getId(), new ServerCallbacks() {
                            @Override
                            public void onSuccess(JSONObject result) {
                                // Toast.makeText(context,"marked ",Toast.LENGTH_LONG).show();
                                try {
                                    if(result.getInt("resp")>0){
                                        holder.nbviews.setText(String.valueOf(result.getInt("resp")));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError result) {
                                //  Toast.makeText(context,"not marked",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onWrong(JSONObject result) {
                                //  Toast.makeText(context,"not marked",Toast.LENGTH_LONG).show();
                            }
                        });
                Intent homeIntent = new Intent(activity,HomeActivity.class);
                homeIntent.putExtra("tab",1);
                homeIntent.putExtra("forum",forumQuestionsList.get(position).getId());
                activity.finish();
                context.startActivity(homeIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return forumQuestionsList.size();
    }

    public static class PostsItem_ViewHolder extends RecyclerView.ViewHolder  {
        CircleImageView rating_picture;
        TextView title,nbviews,createed;
        FlowLayout tags_layout;


        public PostsItem_ViewHolder(View itemView) {
            super(itemView);
            rating_picture= itemView.findViewById(R.id.post_rating);
            title= itemView.findViewById(R.id.mypostTile);
            nbviews=itemView.findViewById(R.id.my_post_nbViews);
            createed=itemView.findViewById(R.id.my_post_created);
            tags_layout=itemView.findViewById(R.id.my_post_tags_layout);

        }

    }
}
