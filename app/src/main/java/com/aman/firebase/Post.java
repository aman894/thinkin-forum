package com.aman.firebase;

import android.util.Log;

import java.util.Date;

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

    public Post(){}

    public Post(String postID,String title,String content,String postedBy,long timeStamp,int upvotes){
        this.postID = postID;
        this.title = title;
        this.content = content;
        this.timeStamp = timeStamp;
        this.upvotes = upvotes;
        this.postedBy = postedBy;
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
