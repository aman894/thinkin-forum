package com.aman.firebase;

import android.util.Log;

import java.util.Date;

/**
 * Created by 1305266 on 01-04-2016.
 *
 * Attention!! Change format ot post_date from String to Date!!
 */
public class Post {

    private String title;
    private String postedBy;
    private String postDate;
    private int upvotes;

    public Post(){}

    public Post(String title,String content,String postedBy,String postDate,int upvotes){
        this.title = title;
        this.content = content;
        this.postDate = postDate;
        this.upvotes = upvotes;
        this.postedBy = postedBy;
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


    public String getPostDate() {
        return postDate;
    }

    @Override
    public String toString() {
        String json = "posts{tile='"+title+"', content='"+content+"', " +
                "postedBy='"+postedBy+"', postDate='"+postDate+"', upvotes="+upvotes+"}";
        Log.w("JSON",json);
        return json;
    }
}
