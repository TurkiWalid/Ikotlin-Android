package com.androidprojects.esprit.ikotlin.adapters;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidprojects.esprit.ikotlin.R;
import com.androidprojects.esprit.ikotlin.fragments.FragmentCompeteShow;

import com.androidprojects.esprit.ikotlin.models.Competition;

import com.androidprojects.esprit.ikotlin.models.CompetitionAnswer;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

/**
 * Created by Odil on 12/01/2018.
 */

public class CompetitionAnswerAdapter extends RecyclerView.Adapter<CompetitionAnswerAdapter.CompetitionAnswerItem_ViewHolder> {
    ArrayList<CompetitionAnswer> CompetitionsList;
    Context context;
    String userid;

    public CompetitionAnswerAdapter(ArrayList<CompetitionAnswer> c_list, Context context) {
        this.CompetitionsList = c_list;
        this.context = context;
        userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public CompetitionAnswerAdapter.CompetitionAnswerItem_ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.competition_answer_item, parent, false);
        CompetitionAnswerAdapter.CompetitionAnswerItem_ViewHolder shareItem_viewHolder = new CompetitionAnswerAdapter.CompetitionAnswerItem_ViewHolder(view);

        return shareItem_viewHolder;
    }

    @Override
    public void onBindViewHolder(final CompetitionAnswerAdapter.CompetitionAnswerItem_ViewHolder holder, final int position) {
        holder.title.setText(CompetitionsList.get(position).getCompetition_title());
        holder.createed.setText(CompetitionsList.get(position).getCreated_string());
        holder.level.setText(CompetitionsList.get(position).getCompetiton_level() + "");


        //view content
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) context;

                Competition c = new Competition();
                c.setId(CompetitionsList.get(position).getId_competition());
                FragmentCompeteShow competeShow = new FragmentCompeteShow();
                competeShow.setCompetition(c);

                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.root_compete, competeShow)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return CompetitionsList.size();
    }

    public static class CompetitionAnswerItem_ViewHolder extends RecyclerView.ViewHolder {
        TextView title, level, createed;

        public CompetitionAnswerItem_ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.competeanswer_postTile);
            level = itemView.findViewById(R.id.competeanswer_level);
            createed = itemView.findViewById(R.id.competeanswer_created);
        }

    }
}
