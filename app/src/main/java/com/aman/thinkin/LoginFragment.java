package com.aman.thinkin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginFragment extends Fragment implements View.OnClickListener{
    Button bLogin;
    EditText etLoginEmail,etLoginPassword;
    ProgressDialog progressDialog;
    Firebase mRootRef;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setUpVariables();
        Firebase.setAndroidContext(getActivity());
        mRootRef = new Firebase("https://think-in.firebaseio.com/");
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        etLoginEmail = (EditText)view.findViewById(R.id.etLoginEmail);
        etLoginPassword = (EditText)view.findViewById(R.id.etLoginPassword);
        bLogin = (Button)view.findViewById(R.id.bLogin);
        bLogin.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bLogin:
                String loginEmail,loginPassword;
                Pattern emailPattern = Pattern.compile("[0-9]{7}@(kiit.ac.in)");
                Matcher emailMatcher = emailPattern.matcher(etLoginEmail.getText().toString());
                // if(emailMatcher.matches()){
                Log.v("E_LOGIN ATTEMPT", "Attempting login with email: " + etLoginEmail.getText().toString());
                Log.v("E_LOGIN ATTEMPT", "Attempting login with password: " + etLoginPassword.getText().toString());
                loginEmail=etLoginEmail.getText().toString();
                loginPassword=etLoginPassword.getText().toString();
                progressDialog = new ProgressDialog(getActivity());
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
                        Toast.makeText(getActivity(), "Logged in !!", Toast.LENGTH_SHORT).show();
                        Intent openHomeActivity = new Intent(getActivity(),HomeActivity.class);
                        startActivity(openHomeActivity);
                    }

                    @Override
                    public void onAuthenticationError(FirebaseError firebaseError) {
                        progressDialog.dismiss();
                        Log.v("E_LOGIN", "Logged in failed with code " + firebaseError.getCode());
                        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_SHORT).show();
                    }
                });
                //  }
                // else
                // Toast.makeText(this,"Please enter a valid KIIT email ID",Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
