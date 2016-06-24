package com.aman.thinkin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class ProfileActivity extends BaseAppCompatActivity {
    TextView tvDisplayFName,tvDisplayLName,tvDisplayBranch,tvDisplaySection,tvDisplayYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setUpVariables();
        Firebase mRoot = new Firebase("https://think-in.firebaseio.com/");
        AuthData authData = mRoot.getAuth();
        Firebase mUser = mRoot.child("users/" + authData.getUid());
        mUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> profileData = dataSnapshot.getValue(Map.class);
                for(Map.Entry<String,String> me: profileData.entrySet()){
                    switch(me.getKey()){
                        case "section":
                            tvDisplaySection.setText(me.getValue());
                            break;
                        case "branch":
                            tvDisplayBranch.setText(me.getValue());
                            break;
                        case "year":
                            tvDisplayYear.setText(me.getValue());
                            break;
                        case "fname":
                            tvDisplayFName.setText(me.getValue());
                            break;
                        case "lname":
                            tvDisplayLName.setText(me.getValue());
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void setUpVariables() {
        tvDisplayBranch = (TextView)findViewById(R.id.tvDisplayBranch);
        tvDisplaySection = (TextView)findViewById(R.id.tvDisplaySection);
        tvDisplayYear = (TextView)findViewById(R.id.tvDisplayYear);
        tvDisplayFName = (TextView)findViewById(R.id.tvDisplayFName);
        tvDisplayLName = (TextView)findViewById(R.id.tvDisplayLName);
    }

}
