package com.aman.firebase;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.ImageView;
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
import java.util.Map;

public class PostsActivity extends AppCompatActivity {
    private static String FIREBASE_URL = "https://think-in.firebaseio.com/";
    Firebase mRoot, mPosts;
    RecyclerView rvPosts;
    FirebaseRecyclerAdapter<Post, PostViewHolder> postAdapter;
    AuthData authData;
    Date timeStamp;
    FloatingActionButton fabInputPost;
    Activity activity;
    int value=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpVariables();

        //Firebase context has to be set before any firebase reference is made
        Firebase.setAndroidContext(this);
        mRoot = new Firebase(FIREBASE_URL);
        mPosts = mRoot.child("posts");

        rvPosts.setHasFixedSize(true); //for performance improvement
        rvPosts.setLayoutManager(new LinearLayoutManager(this));    //for vertical list


        authData = mRoot.getAuth();

        //using built-in firebase adapter from firebase ui to automatically handle firebase stuff

        postAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.view_posts, PostViewHolder.class, mPosts.orderByChild("timeStamp")) {
            @Override
            protected void populateViewHolder(final PostViewHolder postViewHolder, final Post post, int i) {
                Log.w("FIREBASE_ADAPTER", "called");
                //populating views in card view
                postViewHolder.tvTitle.setText(post.getTitle());
                postViewHolder.tvContent.setText(post.getContent());
                postViewHolder.tvUpvotes.setText(String.valueOf(post.getUpvotes()));
                postViewHolder.tvPostTime.setText(getDate(post.getTimeStamp()));

                //Query name of user who has posted

               Query queryName = mRoot.child("users/" + post.getPostedBy() + "/fname");
                queryName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null)
                            postViewHolder.tvPostedBy.setText(dataSnapshot.getValue(String.class));

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            private String getDate(long timeStamp) {
                Date date = new Date(-timeStamp);
                Format format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                return format.format(date);
            }
        };
        rvPosts.setAdapter(postAdapter);
        fabInputPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostsActivity.this);
                final AlertDialog alertDialog = dialogBuilder.create();
                LayoutInflater inflater = PostsActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.post_input_dialog, null);
                alertDialog.setView(dialogView);
                final EditText etInputPostTitle = (EditText)dialogView.findViewById(R.id.etInputPostTitle);
                final EditText etInputPostContent = (EditText)dialogView.findViewById(R.id.etInputPostContent);
                Button bPostAddDone = (Button)dialogView.findViewById(R.id.bPostAddDone);
                Button bPostAddCancel = (Button) dialogView.findViewById(R.id.bPostAddCancel);
                bPostAddDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeStamp = new Date();
                        long timeStampMills = timeStamp.getTime();
                        Firebase pushToken = mPosts.push();
                        String postID = pushToken.getKey();
                        Log.w("PostID::",postID);
                        Post post = new Post(postID,etInputPostTitle.getText().toString(), etInputPostContent.getText().toString(), authData.getUid(), -timeStampMills, 0);
                        pushToken.setValue(post);
                        alertDialog.dismiss();
                    }
                });
                bPostAddCancel.setOnClickListener(new View.OnClickListener() {
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
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        fabInputPost = (FloatingActionButton) findViewById(R.id.fabInputPost);
    }

    //Custom view holder
    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTitle, tvContent, tvPostedBy, tvUpvotes,tvPostTime;

        public PostViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvPostedBy = (TextView) itemView.findViewById(R.id.tvPostedBy);
            tvUpvotes = (TextView) itemView.findViewById(R.id.tvUpvotes);
            tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);

        }

        @Override
        public void onClick(View view) {
            Log.d("THINKIN", "onClick " + getAdapterPosition());
            //Intent startCommentActivity = new Intent(PostsActivity.this,CommentActivity.class);
            //startActivity(startCommentActivity);
        }
    }
}