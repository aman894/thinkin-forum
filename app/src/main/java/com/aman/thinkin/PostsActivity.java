package com.aman.thinkin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.GenericTypeIndicator;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.jar.*;

import me.gujun.android.taggroup.TagGroup;

public class PostsActivity extends BaseAppCompatActivity {
    private static String FIREBASE_URL = "https://think-in.firebaseio.com/";
    Firebase mRoot, mPosts;
    RecyclerView rvPosts;
    static FirebaseRecyclerAdapter<Post, PostViewHolder> postAdapter;
    static AuthData authData;
    Date timeStamp;
    static String postID;
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
                if(post.getTagList()!=null)
                    postViewHolder.tgDisplayTags.setTags(post.getTagList());

                //Query name of user who has posted

               Query queryName = mRoot.child("users/" + post.getPostedBy() + "/fname");
                Log.w("POSTED_BY: ",post.getPostedBy());
                queryName.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot != null)
                            Log.w("POST::",dataSnapshot.getValue(String.class));
                            postViewHolder.tvPostedBy.setText(dataSnapshot.getValue(String.class));

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }
            //get contextual time
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
        postAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
        });
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
                final TagGroup mTagGroup = (TagGroup)dialogView.findViewById(R.id.tgInputTags);
                Button bPostAddDone = (Button)dialogView.findViewById(R.id.bPostAddDone);
                Button bPostAddCancel = (Button) dialogView.findViewById(R.id.bPostAddCancel);
                bPostAddDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeStamp = new Date();
                        long timeStampMills = timeStamp.getTime();
                        Firebase pushToken = mPosts.push();
                        postID = pushToken.getKey();
                        Log.w("PostID::",postID);
                        String [] upvoteList = {""};
                        Post post = new Post(postID,etInputPostTitle.getText().toString(), etInputPostContent.getText().toString(), authData.getUid(), -timeStampMills, 0,upvoteList,mTagGroup.getTags());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_post_activity,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.post_logout:
                mRoot.unauth();
                if(mRoot.getAuth() == null){
                    Intent goBackToLogin = new Intent(PostsActivity.this, MainActivity.class);
                    startActivity(goBackToLogin);
                }
                else Log.w("AUTH_DATA::",mRoot.getAuth().toString());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Initializing variables
    private void setUpVariables() {
        rvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        fabInputPost = (FloatingActionButton) findViewById(R.id.fabInputPost);
    }

    //Custom view holder
    public static class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,TagGroup.OnTagClickListener{
        TextView tvTitle, tvContent, tvPostedBy, tvUpvotes,tvPostTime;

        ImageButton ibPostMenuButton;
        TagGroup tgDisplayTags;
        Button bUpvote;
        private Context context;
        public PostViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvContent);
            tvPostedBy = (TextView) itemView.findViewById(R.id.tvPostedBy);
            tvUpvotes = (TextView) itemView.findViewById(R.id.tvUpvotes);
            tvPostTime = (TextView) itemView.findViewById(R.id.tvPostTime);
            tgDisplayTags = (TagGroup) itemView.findViewById(R.id.tgDisplayPostTags);
            tgDisplayTags.setOnTagClickListener(this);
            bUpvote = (Button) itemView.findViewById(R.id.bUpvote);
            ibPostMenuButton = (ImageButton)itemView.findViewById(R.id.ibPostMenuButton);
            ibPostMenuButton.setOnClickListener(this);
            bUpvote.setOnClickListener(this);
            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.bUpvote:
                    Post postForUpvote = PostsActivity.postAdapter.getItem(getAdapterPosition());
                    Firebase upvoteListRef = new Firebase(FIREBASE_URL+"/posts/"+postForUpvote.getPostID()+"/upvoteList");
                    ArrayList<String> upvoteList = new ArrayList<String>(Arrays.asList(postForUpvote.getUpvoteList()));
                    Log.w("List item",upvoteList.toString());
                    if(!upvoteList.contains(authData.getUid())){
                        upvoteList.add(authData.getUid());
                        upvoteListRef.setValue(upvoteList.toArray());
                        Firebase upvotePostRef = new Firebase(FIREBASE_URL+"/posts/"+postForUpvote.getPostID()+"/upvotes");
                        upvotePostRef.setValue(postForUpvote.getUpvotes()+1);
                    }
                    else Toast.makeText(context,"Already Upvoted",Toast.LENGTH_SHORT).show();

                    break;
                case R.id.ibPostMenuButton:
                    PopupMenu popupMenu = new PopupMenu(context,ibPostMenuButton);
                    View menuParent = (View)view.getParent();
                    final Post post = PostsActivity.postAdapter.getItem(getAdapterPosition());
                    if(post.getPostedBy().equals(authData.getUid())){
                        popupMenu.getMenu().add(0,0,0,"Edit");
                        popupMenu.getMenu().add(0,1,1,"Delete");
                    }
                    else
                        popupMenu.getMenu().add(0,2,2,"Report");
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case 0:
                                    Toast.makeText(context,"Edit coming soon!",Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    String postID = post.getPostID();
                                    Toast.makeText(context,"Deleted",Toast.LENGTH_SHORT).show();
                                    Firebase postRef = new Firebase(FIREBASE_URL).child("posts").child(postID);
                                    postRef.setValue(null);
                                    break;
                                case 2:
                                    break;
                            }
                            return true;
                        }
                    });
                    popupMenu.show();
                    break;
                default:
                    Log.d("THINKIN", "onClick " + getAdapterPosition());
                    Intent startCommentActivity = new Intent(context,CommentActivity.class);
                    Log.w("PostID::",PostsActivity.postAdapter.getRef(getAdapterPosition()).getKey());
                    startCommentActivity.putExtra("postID",PostsActivity.postAdapter.getRef(getAdapterPosition()).getKey());
                    TextView title = (TextView)view.findViewById(R.id.tvTitle);
                    TextView content = (TextView)view.findViewById(R.id.tvContent);
                    TextView postedBy = (TextView)view.findViewById(R.id.tvPostedBy);
                    startCommentActivity.putExtra("POST_TITLE",title.getText());
                    startCommentActivity.putExtra("POST_CONTENT",content.getText());
                    startCommentActivity.putExtra("POSTED_BY",postedBy.getText());
                    context.startActivity(startCommentActivity);
            }
        }

        @Override
        public void onTagClick(String tag) {
            //Toast.makeText(context,tag+" clicked",Toast.LENGTH_SHORT).show();
            Intent openTagActivity = new Intent(context,TagActivity.class);
            openTagActivity.putExtra("TAG_VALUE",tag);
            context.startActivity(openTagActivity);
        }
    }
}