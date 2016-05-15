package com.aman.firebase;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Firebase mRootRef;
    String loginEmail,loginPassword;
    ProgressDialog progressDialog;
    EditText etLoginEmail,etLoginPassword;
    Button bLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        setUpVariables();
        mRootRef = new Firebase("https://think-in.firebaseio.com/");
        bLogin.setOnClickListener(this);

        /*
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


            }
        });
        */

        Log.v("Value of root", mRootRef.toString());

    }

    private void setUpVariables() {
        etLoginEmail = (EditText)findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText)findViewById(R.id.etLoginPassword);
        bLogin = (Button) findViewById(R.id.bLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogin:
                Pattern emailPattern = Pattern.compile("[0-9]{7}@(kiit.ac.in)");
                Matcher emailMatcher = emailPattern.matcher(etLoginEmail.getText().toString());
               // if(emailMatcher.matches()){
                    Log.v("E_LOGIN ATTEMPT", "Attempting login with email: " + etLoginEmail.getText().toString());
                    Log.v("E_LOGIN ATTEMPT", "Attempting login with password: " + etLoginPassword.getText().toString());
                    loginEmail=etLoginEmail.getText().toString();
                    loginPassword=etLoginPassword.getText().toString();
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("Attempting Log in");
                    progressDialog.setMessage("Please wait..");
                    progressDialog.setIndeterminate(true);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    mRootRef.authWithPassword(loginEmail, loginPassword, new Firebase.AuthResultHandler() {
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
              //  }
               // else
                   // Toast.makeText(this,"Please enter a valid KIIT email ID",Toast.LENGTH_SHORT).show();

                break;

        }
    }
}
