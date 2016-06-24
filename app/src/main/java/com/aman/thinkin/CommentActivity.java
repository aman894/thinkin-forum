package com.aman.thinkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentActivity extends BaseAppCompatActivity {
    private static String FIREBASE_URL = "https://think-in.firebaseio.com/";
    Firebase mRoot, mComments;
    RecyclerView rvComments;
    FirebaseRecyclerAdapter<Comment, CommentViewHolder> commentAdapter;
    AuthData authData;
    Date timeStamp;
    FloatingActionButton fabInputComment;
    int value=0;
    TextView tvCommentPostTitle,tvCommentPostContent,tvPostedByInComment;
    String postID,postTitle,postContent,postedBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.commentActivityToolbar);
        setSupportActionBar(toolbar);
        setUpVariables();
        Intent intent = getIntent();
        postID = intent.getStringExtra("postID");
        postTitle = intent.getStringExtra("POST_TITLE");
        postContent = intent.getStringExtra("POST_CONTENT");
        postedBy = intent.getStringExtra("POSTED_BY");

        tvCommentPostTitle.setText(postTitle);
        tvCommentPostContent.setText(postContent);
        tvPostedByInComment.setText(postedBy);
        Log.w("postID",postID);
        //Firebase context has to be set before any firebase reference is made
        Firebase.setAndroidContext(this);
        mRoot = new Firebase(FIREBASE_URL);
        mComments = mRoot.child("comments");

        rvComments.setHasFixedSize(true); //for performance improvement
        rvComments.setLayoutManager(new LinearLayoutManager(this));    //for vertical list

        authData = mRoot.getAuth();

        //using built-in firebase adapter from firebase ui to automatically handle firebase stuff
        commentAdapter = new FirebaseRecyclerAdapter<Comment, CommentViewHolder>(Comment.class, R.layout.comment_body, CommentViewHolder.class, mComments.orderByChild("postID").equalTo(postID)) {
            @Override
            protected void populateViewHolder(final CommentViewHolder commentViewHolder, final Comment comment, int i) {
                Log.w("COMMENT_ADAPTER", "called");
                //populating views in card view
                commentViewHolder.tvCommentContent.setText(comment.getCommentContent());
                commentViewHolder.tvCommentTimestamp.setText(getDate(comment.getTimestamp()));
                commentViewHolder.tvCommentUser.setText(comment.getUserID());

                //Query name of user who has posted

                Query queryName = mRoot.child("users/" + comment.getUserID() + "/fname");
                queryName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null)
                            Log.w("COMMENT::",dataSnapshot.getValue(String.class));
                        commentViewHolder.tvCommentUser.setText(dataSnapshot.getValue(String.class));

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            private String getDate(long timeStamp) {
                long timestamp = -timeStamp;
                Date date = new Date();
                long difference = Math.abs(timestamp-date.getTime())/1000;

                if(difference<60){
                    String time = String.valueOf(difference);
                    if(time.equals("1"))
                        return time+" sec ago";
                    else return time+" secs ago";

                }
                else if(difference>=60 && difference<3600){
                    String time = String.valueOf(difference/60);
                    if(time.equals("1"))
                        return time+" min ago";
                    else return time+" mins ago";
                }
                else if(difference>=3600 && difference<3600*24){
                    String time = String.valueOf(difference/3600);
                    if(time.equals("1"))
                        return time+" hr ago";
                    else return time+" hrs ago";
                }
                else {
                    String time = String.valueOf(difference/(3600*24));
                    if(time.equals("1"))
                        return time+" day ago";
                    else return time+" days ago";
                }
            }
        };
        rvComments.setAdapter(commentAdapter);
        fabInputComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommentActivity.this);
                final AlertDialog alertDialog = dialogBuilder.create();
                LayoutInflater inflater = CommentActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.comment_input_dialog, null);
                alertDialog.setView(dialogView);

                final EditText etInputCommentContent = (EditText)dialogView.findViewById(R.id.etInputCommentContent);
                Button bCommentAddDone = (Button)dialogView.findViewById(R.id.bCommentAddDone);
                Button bCommentAddCancel = (Button) dialogView.findViewById(R.id.bCommentAddCancel);
                bCommentAddDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeStamp = new Date();
                        long timeStampMills = timeStamp.getTime();
                        Firebase pushToken = mComments.push();
                        String commentID = pushToken.getKey();
                        Log.w("CommentID::",commentID);
                        //Log.w("PostID::",postID);
                        Comment comment = new Comment(postID,authData.getUid(),commentID, -timeStampMills, etInputCommentContent.getText().toString());
                        pushToken.setValue(comment);
                        alertDialog.dismiss();
                    }
                });
                bCommentAddCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    //Initializing variables
    private void setUpVariables() {
        rvComments = (RecyclerView) findViewById(R.id.rvComments);
        fabInputComment = (FloatingActionButton) findViewById(R.id.fabInputComment);
        tvCommentPostTitle = (TextView)findViewById(R.id.tvCommentPostTitle);
        tvCommentPostContent = (TextView)findViewById(R.id.tvCommentPostContent);
        tvPostedByInComment = (TextView)findViewById(R.id.tvPostedByInComment);
    }

    //Custom view holder
    public static class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView tvCommentUser,tvCommentContent,tvCommentTimestamp;

        public CommentViewHolder(View itemView) {
            super(itemView);
            tvCommentContent = (TextView)itemView.findViewById(R.id.tvCommentContent);
            tvCommentUser = (TextView)itemView.findViewById(R.id.tvCommentUser);
            tvCommentTimestamp = (TextView)itemView.findViewById(R.id.tvCommentTimestamp);
        }
    }
}