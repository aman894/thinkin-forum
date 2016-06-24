package com.aman.thinkin;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class SignupFragment extends Fragment implements View.OnClickListener{
    Firebase mRootRef;
    Button bSignup;
    EditText etSignupEmail,etSignupPassword;
    ProgressDialog progressDialog;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        bSignup = (Button)view.findViewById(R.id.bSignup);
        etSignupEmail = (EditText)view.findViewById(R.id.etSignupEmail);
        etSignupPassword = (EditText)view.findViewById(R.id.etSignupPassword);
        bSignup.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bSignup:
                String email = etSignupEmail.getText().toString();
                String password = etSignupPassword.getText().toString();
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setTitle("Registering");
                progressDialog.setMessage("Please wait..");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                mRootRef = new Firebase("https://think-in.firebaseio.com/");
                mRootRef.createUser(email, password, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(getActivity(), "Registered", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        Toast.makeText(getActivity(), "Registration failed", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                });
                break;
        }
    }
}
