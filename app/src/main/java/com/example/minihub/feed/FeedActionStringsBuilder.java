package com.example.minihub.feed;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;

import com.example.minihub.R;
import com.example.minihub.domain.Payload;

import java.util.Arrays;

public class FeedActionStringsBuilder {

    private Context mContext;

    private boolean useHtmlTags = false;

    public FeedActionStringsBuilder(Context context, boolean useHtmlTags) {
        mContext = context;
        this.useHtmlTags = useHtmlTags;
    }

    public String getActionByEventType(String eventType, Payload payload) {
        Resources res = mContext.getResources();
        StringBuilder sb = new StringBuilder();
        switch (eventType) {
            case "PushEvent": {
                String[] string = {
                        makeStringBold(res.getQuantityString(R.plurals.pushed_commits, payload.size, payload.size)),
                        mContext.getString(R.string.to),
                        getBranchFromRef(payload.ref),
                        mContext.getString(R.string.at)
                };
                return TextUtils.join(" ", Arrays.asList(string));
            }
            case "IssueCommentEvent": return makeStringBold(getIssueCommentAction(payload)) + " "
                    + mContext.getString(R.string.on_issue_in);
            case "PullRequestEvent": {
                return makeStringBold(getPullRequestAction(payload) + " "
                        + mContext.getString(R.string.pull_request)) + " "
                        + mContext.getString(R.string.in);
            }
            case "CommitCommentEvent": return mContext.getString(R.string.commented_on_commit)
                    + " " + mContext.getString(R.string.in);
            case "IssuesEvent": return makeStringBold(payload.action) + " " + mContext.getString(R.string.issue_in);
            case "CreateEvent": return makeStringBold(mContext.getString(R.string.created) + " " + payload.ref_tag) +
                    " " + mContext.getString(R.string.in);
            case "PullRequestReviewEvent":
                return makeStringBold(getPullRequestReviewAction(payload)) + " "
                        + mContext.getString(R.string.in);
            case "PullRequestReviewCommentEvent":
                return makeStringBold(getPullRequestReviewCommentAction(payload)) + " " +
                        mContext.getString(R.string.in);
        }
        return eventType;
    }

    private String makeStringBold(String str) {
         if (useHtmlTags) return "<b>" + str + "</b>";
         else return str;
    }

    private String getPullRequestReviewAction(Payload payload) {
        switch (payload.action) {
            case "submitted": return mContext.getString(R.string.pull_request_review_action_reviewed);
            case "edited": return mContext.getString(R.string.pull_request_review_action_edited);
            case "dismissed": return mContext.getString(R.string.pull_request_review_action_dismissed);
        }
        return null;
    }

    private String getPullRequestReviewCommentAction(Payload payload) {
        switch (payload.action) {
            case "created": return mContext.getString(R.string.pull_request_review_comment_commented);
            case "edited": return mContext.getString(R.string.pull_request_review_comment_edited);
            case "deleted": return mContext.getString(R.string.pull_request_review_comment_deleted);
        }
        return null;
    }

    public String getIssueCommentAction(Payload payload) {
        if (payload.action.equals("created")) {
            return mContext.getString(R.string.commented);
        } else return payload.action  + mContext.getString(R.string.comment);
    }

    public String getPullRequestAction(Payload payload) {
        if (payload.action.equals("closed")) {
            if (payload.merged) return mContext.getString(R.string.merged);
            else return mContext.getString(R.string.closed);
        } else {
            return payload.action;
        }
    }

    public static String getBranchFromRef(String ref) {
        String[] splitted = ref.split("/");
        return splitted[splitted.length-1];
    }
}
