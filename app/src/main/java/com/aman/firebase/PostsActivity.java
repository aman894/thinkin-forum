package com.aman.firebase;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class PostsActivity extends AppCompatActivity {
    private static String FIREBASE_URL = "https://think-in.firebaseio.com/";
    Firebase mRoot,mPosts;
    RecyclerView rvPosts;
    FirebaseRecyclerAdapter<Post,PostViewHolder> postAdapter;
    float width,height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUpVariables();
        Firebase.setAndroidContext(this);
        mRoot = new Firebase(FIREBASE_URL);
        mPosts = mRoot.child("posts");


        rvPosts.setHasFixedSize(true);
        rvPosts.setLayoutManager(new LinearLayoutManager(this));



        postAdapter = new FirebaseRecyclerAdapter<Post, PostViewHolder>(Post.class,R.layout.view_posts,PostViewHolder.class,mPosts) {
            @Override
            protected void populateViewHolder(PostViewHolder postViewHolder, Post post, int i) {
                Log.w("FIREBASE_ADAPTER","called");
                postViewHolder.tvTitle.setText(post.getTitle());
                postViewHolder.tvContent.setText(post.getContent());
                postViewHolder.tvUpvotes.setText(String.valueOf(post.getUpvotes()));
                postViewHolder.tvPostedBy.setText(post.getPostedBy());

            }
        };
        rvPosts.setAdapter(postAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initiatedialog(null);


            }
        });
    }
    protected void initiatedialog(View view) {

        final Dialog dialog = new Dialog(PostsActivity.this);
        dialog.setContentView(R.layout.popup);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        int width = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
        int height = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();

        lp.width = Math.round((width * 13) / 15);
        lp.height = Math.round(height / 3);
        dialog.getWindow().setAttributes(lp);
        final EditText etPostContent = (EditText) dialog.findViewById(R.id.etPostContent);
        Button bAddPost = (Button) dialog.findViewById(R.id.bAddPost);


        bAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Post post = new Post("Some Title", etPostContent.getText().toString(), "Random User", "01-04-2016", 0);
                mPosts.push().setValue(post);
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void setUpVariables() {
        rvPosts = (RecyclerView)findViewById(R.id.rvPosts);
        //etPostContent = (EditText)findViewById(R.id.etPostContent);
        //bAddPost = (Button)findViewById(R.id.bAddPost);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        TextView tvTitle,tvContent,tvPostedBy,tvUpvotes;
        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView)itemView.findViewById(R.id.tvContent);
            tvPostedBy = (TextView)itemView.findViewById(R.id.tvPostedBy);
            tvUpvotes = (TextView)itemView.findViewById(R.id.tvUpvotes);
        }
    }

}
