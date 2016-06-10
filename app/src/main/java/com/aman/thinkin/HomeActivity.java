package com.aman.thinkin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aman.thinkin.planner.Planner_MainActivity;
import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity{
    EditText etFName,etLName,etBranch,etSection,etYear;
    Button bProfileSubmit,bSkipProfile,bGoToPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //setting up text boxes and button
        setUpViews();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,Planner_MainActivity.class));
            }
        });
        Firebase.setAndroidContext(this);
        final Firebase mRootRef = new Firebase("https://think-in.firebaseio.com/");
        final AuthData authData = mRootRef.getAuth();
        if(authData == null){
            Toast.makeText(HomeActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(HomeActivity.this, "User authenticated ;)", Toast.LENGTH_SHORT).show();
        }
        bProfileSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = authData.getUid();
                String fName = etFName.getText().toString();
                String lName = etLName.getText().toString();
                String branch = etBranch.getText().toString();
                String section = etSection.getText().toString();
                String year = etYear.getText().toString();

                Intent i=new Intent(HomeActivity.this,PostsActivity.class);
                i.putExtra("User",etFName.getText().toString());


                Map<String,Object> profileDetails = new HashMap<String, Object>();

                profileDetails.put("fname",fName);
                profileDetails.put("lname",lName);
                profileDetails.put("branch",branch);
                profileDetails.put("section",section);
                profileDetails.put("year",year);

                Firebase userRef = mRootRef.child("users/"+userID);

                userRef.setValue(profileDetails, new Firebase.CompletionListener() {

                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
        bSkipProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        bGoToPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PostsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setUpViews() {
        etBranch = (EditText)findViewById(R.id.etBranch);
        etSection = (EditText)findViewById(R.id.etSection);
        etYear = (EditText)findViewById(R.id.etYear);
        etFName = (EditText)findViewById(R.id.etFName);
        etLName = (EditText)findViewById(R.id.etLName);
        bProfileSubmit = (Button)findViewById(R.id.bProfileSubmit);
        bSkipProfile = (Button)findViewById(R.id.bSkipProfile);
        bGoToPost = (Button)findViewById(R.id.bGoToPost);
    }


}
