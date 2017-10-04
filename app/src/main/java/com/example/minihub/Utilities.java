package com.example.minihub;

/**
 * Created by v.maletskiy on 10/2/2017.
 */

public class Utilities {
    public static String getActionByEventType(String eventType) {
        switch (eventType) {
            case "PushEvent": return " pushed commits to ";
            case "IssueCommentEvent": return " commented on issue in ";
            case "PullRequestEvent": return " created pull request in ";
            case "CommitCommentEvent": return " commented on commit in ";
            case "IssuesEvent": return " opened issue in ";
        }
        return eventType;
    }
}
