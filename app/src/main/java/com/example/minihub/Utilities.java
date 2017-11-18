package com.example.minihub;


import com.example.minihub.domain.Payload;

public class Utilities {
    public static String getActionByEventType(String eventType, Payload payload) {
        switch (eventType) {
            case "PushEvent":
                return "<b> pushed " + payload.size + " commits </b> to " + getBranchFromRef(payload.ref) + " at ";
            case "IssueCommentEvent": return " <b>" + getIssueCommentAction(payload) + "</b> on issue in ";
            case "PullRequestEvent": return " <b>" + getPullRequestAction(payload) + " pull request </b> in ";
            case "CommitCommentEvent": return "<b> commented on commit </b> in ";
            case "IssuesEvent": return " <b>" + payload.action + "</b> issue in ";
            case "CreateEvent": return "<b> created " + payload.ref_tag+"</b>";
        }
        return eventType;
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
}
