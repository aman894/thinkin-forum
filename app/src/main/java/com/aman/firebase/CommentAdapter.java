package com.aman.firebase;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by aman on 22/5/16.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    //Custom Adapter will have a list to popuate the recycler view.
    ArrayList<Comment>commentList;
    public CommentAdapter(ArrayList<Comment>commentList){
        this.commentList = commentList;
    }
    //Custom View holder
    class CommentViewHolder extends RecyclerView.ViewHolder{
        //fields in one of the list items
        TextView tvCommentContent,tvCommentTimestamp;
        public CommentViewHolder(View itemView) {
            super(itemView);
            //initializing variables
            tvCommentContent = (TextView)itemView.findViewById(R.id.tvCommentContent);
            tvCommentTimestamp = (TextView)itemView.findViewById(R.id.tvCommentTimestamp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    //will be used to set view of the view holder
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_body,parent,false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(v);
        return commentViewHolder;
    }
    //will be used to set data items to the view
    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.tvCommentContent.setText(commentList.get(position).userID+": "+commentList.get(position).content);
        holder.tvCommentTimestamp.setText(commentList.get(position).timestamp);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}