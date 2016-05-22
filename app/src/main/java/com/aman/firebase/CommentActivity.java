package com.aman.firebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    public static String COMMENT_URL = "https://think-in.firebaseio.com/comments";
    Firebase commentRef;
    ArrayList<Comment> commentList;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rvComments;
    CommentAdapter commentAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Firebase.setAndroidContext(this);
        commentList = new ArrayList<Comment>();
        commentRef = new Firebase(COMMENT_URL);

        rvComments = (RecyclerView)findViewById(R.id.rvComments);

        linearLayoutManager = new LinearLayoutManager(this);
        commentAdapter = new CommentAdapter(commentList);

        rvComments.setLayoutManager(linearLayoutManager);
        rvComments.setAdapter(commentAdapter);

        //firebase stuff

        commentRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.w("THINKIN::","child added");
                Log.w("THINKIN::",dataSnapshot.toString());
                commentList.add(dataSnapshot.getValue(Comment.class));
                commentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.w("THINKIN::","child changed");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });



    }
}
