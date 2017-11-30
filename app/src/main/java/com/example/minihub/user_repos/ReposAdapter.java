package com.example.minihub.user_repos;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorAdapter;
import com.androidessence.recyclerviewcursoradapter.RecyclerViewCursorViewHolder;
import com.example.minihub.R;
import com.example.minihub.domain.Repository;
import com.example.minihub.feed.FeedAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.minihub.user_repos.UserReposPresenter.COL_FORKS_COUNT;
import static com.example.minihub.user_repos.UserReposPresenter.COL_LANGUAGE;
import static com.example.minihub.user_repos.UserReposPresenter.COL_REPO_ID;
import static com.example.minihub.user_repos.UserReposPresenter.COL_REPO_NAME;
import static com.example.minihub.user_repos.UserReposPresenter.COL_STARGAZERS_COUNT;
import static com.example.minihub.user_repos.UserReposPresenter.COL_WATCHERS_COUNT;


public class ReposAdapter extends RecyclerViewCursorAdapter<ReposAdapter.ViewHolder> {
    private String TAG = getClass().getSimpleName();
    private Context mContext;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_list_element, parent, false);
        return new ReposAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (position < mCursorAdapter.getCount()) {
            mCursorAdapter.getCursor().moveToPosition(position);
            setViewHolder(holder);
            mCursorAdapter.bindView(null, mContext, mCursorAdapter.getCursor());
        }
    }

    protected ReposAdapter(Context context) {
        super(context);
        mContext = context;
        setupCursorAdapter(null, 0, R.layout.repo_list_element, false);
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    class ViewHolder  extends RecyclerViewCursorViewHolder {

        @BindView(R.id.repo_name)
        TextView repoName;
        @BindView(R.id.stargazers_count)
        TextView starsCount;
        @BindView(R.id.forks_count)
        TextView forksCount;
        @BindView(R.id.language)
        TextView language;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bindCursor(Cursor cursor) {
            Repository repo = new Repository();
            repo.id = cursor.getInt(COL_REPO_ID);
            repo.name = cursor.getString(COL_REPO_NAME);
            repo.language = cursor.getString(COL_LANGUAGE);
            repo.forksCount = cursor.getInt(COL_FORKS_COUNT);
            repo.stargazersCount = cursor.getInt(COL_STARGAZERS_COUNT);
            repo.watchersCount = cursor.getInt(COL_WATCHERS_COUNT);
            repoName.setText(repo.name);
            starsCount.setText(Integer.toString(repo.stargazersCount));
            forksCount.setText(Integer.toString(repo.forksCount));
            language.setText(repo.language);
        }
    }
}
