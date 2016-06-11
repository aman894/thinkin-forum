package com.aman.thinkin;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1305266 on 01-04-2016.
 *
 * Attention!! Change format ot post_date from String to Date!!
 */
public class Post {

    private String postID;
    private String title;
    private String postedBy;
    private long timeStamp;
    private int upvotes;
    private String[] upvoteList;

    public Post(){}

    public String[] getUpvoteList() {
        return upvoteList;
    }

    public Post(String postID, String title, String content, String postedBy, long timeStamp, int upvotes, String[]upvoteList){
        this.postID = postID;
        this.title = title;
        this.content = content;
        this.timeStamp = timeStamp;
        this.upvotes = upvotes;
        this.postedBy = postedBy;
        this.upvoteList = upvoteList;
    }

    public String getPostID() {
        return postID;
    }

    public int getUpvotes() {
        return upvotes;
    }


    private String content;

    public String getContent() {
        return content;
    }


    public String getTitle() {
        return title;
    }
    public String getPostedBy() {
        return postedBy;
    }


    public long getTimeStamp() {
        return timeStamp;
    }
}
