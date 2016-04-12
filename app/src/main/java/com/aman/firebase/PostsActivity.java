package com.aman.firebase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

public class PostsActivity extends AppCompatActivity {
    private static String FIREBASE_URL = "https://think-in.firebaseio.com/";
    Firebase mRoot, mPosts;
    RecyclerView rvPosts;
    FirebaseRecyclerAdapter<Post, PostViewHolder> postAdapter;
    EditText etPostContent;
    Button bAddPost;
    AuthData authData;

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

        bAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating a post and uploading to firebase
                Post post = new Post("Some Title", etPostContent.getText().toString(), authData.getUid(), "01-04-2016", 0);
                mPosts.push().setValue(post);
            }
        });

        //using built-in firebase adapter from firebase ui to automatically handle firebase stuff

        postAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class, R.layout.view_posts, PostViewHolder.class, mPosts) {
            @Override
            protected void populateViewHolder(final PostViewHolder postViewHolder, final Post post, int i) {
                Log.w("FIREBASE_ADAPTER", "called");
                //populating views in card view
                postViewHolder.tvTitle.setText(post.getTitle());
                postViewHolder.tvContent.setText(post.getContent());
                postViewHolder.tvUpvotes.setText(String.valueOf(post.getUpvotes()));

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
        };
        rvPosts.setAdapter(postAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //Initializing variables
    private void setUpVariables() {
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        etPostContent = (EditText) findViewById(R.id.etPostContent);
        bAddPost = (Button) findViewById(R.id.bAddPost);
    }

    //Custom view holder
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvPostedBy, tvUpvotes;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvPostedBy = (TextView) itemView.findViewById(R.id.tvPostedBy);
            tvUpvotes = (TextView) itemView.findViewById(R.id.tvUpvotes);
        }
    }
}
