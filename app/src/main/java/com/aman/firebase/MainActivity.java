package com.aman.firebase;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;


import java.util.Map;

import shem.com.materiallogin.MaterialLoginView;
import shem.com.materiallogin.MaterialLoginViewListener;

public class MainActivity extends AppCompatActivity {
    Firebase mRootRef;
    String username,pass;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mRootRef = new Firebase("https://think-in.firebaseio.com/");
        final MaterialLoginView login = (MaterialLoginView) findViewById(R.id.login);
        login.setListener(new MaterialLoginViewListener() {
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                //Handle register
                final CharSequence userID=registerUser.getEditText().getText().toString();
                mRootRef.createUser(registerUser.getEditText().getText().toString(), registerPass.getEditText().getText().toString(),
                        new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(MainActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                        Firebase userRef = mRootRef.child("users");
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(MainActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                //Handle login
                Log.v("E_LOGIN ATTEMPT", "Attempting login with email: " + loginUser.getEditText().getText().toString());
                Log.v("E_LOGIN ATTEMPT", "Attempting login with password: " + loginPass.getEditText().getText().toString());
                username=loginUser.getEditText().getText().toString();
                pass=loginPass.getEditText().getText().toString();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setTitle("Attempting Log in");
                progressDialog.setMessage("Please wait..");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                mRootRef.authWithPassword(username, pass, new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        Log.v("E_LOGIN", "Logged in");
                        progressDialog.dismiss();
                        Log.w("F_PROVIDER",authData.getProvider());
                        Toast.makeText(MainActivity.this, "Logged in !!", Toast.LENGTH_SHORT).show();
                        Intent openHomeActivity = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(openHomeActivity);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progressDialog.dismiss();
                        Log.v("E_LOGIN", "Logged in failed with code " + firebaseError.getCode());
                        Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Log.v("Value of root", mRootRef.toString());

    }

}
