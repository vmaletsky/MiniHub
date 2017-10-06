package com.example.minihub.user_repos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.minihub.R;
import com.example.minihub.domain.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by volod on 8/26/2017.
 */

public class ReposAdapter extends RecyclerView.Adapter<ReposAdapter.ViewHolder> {

    private List<Repository> repos;

    public ReposAdapter() {
        repos = new ArrayList<>();
    }

    public void setRepos(List<Repository> repos) {
        this.repos = repos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_list_element, parent, false);
        return new ReposAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repository repo = repos.get(position);
        holder.repoName.setText(repo.name);
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    class ViewHolder  extends RecyclerView.ViewHolder {

        TextView repoName;

        public ViewHolder(View itemView) {
            super(itemView);
            this.repoName = (TextView) itemView.findViewById(R.id.repo_name);
        }
    }
}
