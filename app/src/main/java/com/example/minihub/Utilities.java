package com.example.minihub;


import com.example.minihub.domain.Payload;
import com.example.minihub.network.FeedAsyncTask;

import static com.example.minihub.network.FeedAsyncTask.ERROR_INCORRECT_DATA;
import static com.example.minihub.network.FeedAsyncTask.ERROR_NO_NETWORK;

public class Utilities {
    public static String getActionByEventType(String eventType, Payload payload) {
        switch (eventType) {
            case "PushEvent":
                return "<b> pushed " + payload.size + " commits </b> to " + getBranchFromRef(payload.ref) + " at ";
            case "IssueCommentEvent": return " <b> " + getIssueCommentAction(payload) + "</b> on issue in ";
            case "PullRequestEvent": return " <b> " + getPullRequestAction(payload) + " pull request </b> in ";
            case "CommitCommentEvent": return "<b> commented on commit </b> in ";
            case "IssuesEvent": return "<b> " + payload.action + "</b> issue in ";
            case "CreateEvent": return "<b> created " + payload.ref_tag+"</b>";
            case "PullRequestReviewEvent": return "<b> " + getPullRequestReviewAction(payload) + "</b>";
            case "PullRequestReviewCommentEvent": return "<b> " + getPullRequestReviewCommentAction(payload) + "</b>";
        }
        return eventType;
    }

    private static String getPullRequestReviewAction(Payload payload) {
        switch (payload.action) {
            case "submitted": return "reviewed pull request";
            case "edited": return "edited pull request review";
            case "dismissed": return "dismissed pull request review";
        }
        return null;
    }

    private static String getPullRequestReviewCommentAction(Payload payload) {
        switch (payload.action) {
            case "created": return "commented on pull request";
            case "edited": return "edited comment on pull request";
            case "deleted": return "deleted comment on pull request";
        }
        return null;
    }

    public static String getIssueCommentAction(Payload payload) {
        if (payload.action.equals("created")) {
            return "commented";
        } else return payload.action  + " comment";
    }

    public static String getPullRequestAction(Payload payload) {
        if (payload.action.equals("closed")) {
            if (payload.merged) return "merged";
            else return "closed";
        } else {
            return payload.action;
        }
    }

    public static String getBranchFromRef(String ref) {
        String[] splitted = ref.split("/");
        return splitted[splitted.length-1];
    }

    public static int getErrorMessage(@FeedAsyncTask.Error int errorCode) {
        switch (errorCode) {
            case ERROR_INCORRECT_DATA: return R.string.error_incorrect_data;
            case ERROR_NO_NETWORK: return R.string.error_no_network;
            default: return R.string.unknown_error;
        }
    }
}
