package com.aman.firebase;

/**
 * Created by aman on 22/5/16.
 */
public class Comment {
    String postID,userID,content,timestamp;
    public Comment(String postID, String userID, String timestamp, String content) {
        this.postID = postID;
        this.userID = userID;
        this.timestamp = timestamp;
        this.content = content;
    }
}
