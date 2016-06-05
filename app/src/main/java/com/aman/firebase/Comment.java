package com.aman.firebase;

/**
 * Created by aman on 1/6/16.
 */
public class Comment {

    private String postID;
    private String userID;
    private String commentID;
    private String commentContent;
    private long timestamp;

    public Comment(){}
    public Comment(String postID, String userID, String commentID, long timestamp, String commentContent) {
        this.postID = postID;
        this.userID = userID;
        this.timestamp = timestamp;
        this.commentContent = commentContent;
        this.commentID = commentID;
    }
    public String getPostID() {
        return postID;
    }

    public String getUserID() {
        return userID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getCommentContent() {
        return commentContent;
    }

    public String getCommentID() {
        return commentID;
    }

}
