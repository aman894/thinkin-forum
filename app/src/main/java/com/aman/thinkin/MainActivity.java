package com.aman.thinkin;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.firebase.client.Firebase;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button bToSignup;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bToSignup = (Button)findViewById(R.id.bToSignup);
        bToSignup.setOnClickListener(this);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LoginFragment loginFragment = new LoginFragment();
        fragmentTransaction.add(R.id.flMainContainer,loginFragment);
        fragmentTransaction.commit();

        /*Firebase.setAndroidContext(this);
        setUpVariables();
        mRootRef = new Firebase("https://think-in.firebaseio.com/");
        bLogin.setOnClickListener(this);
        /*----------remove this--------------*/
        /*mRootRef.authWithPassword("aman@gmail.com", "aman", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.v("E_LOGIN", "Logged in");
                //progressDialog.dismiss();
                Log.w("F_PROVIDER",authData.getProvider());
                Toast.makeText(MainActivity.this, "Logged in !!", Toast.LENGTH_SHORT).show();
                Intent openHomeActivity = new Intent(MainActivity.this,HomeActivity.class);
                startActivity(openHomeActivity);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                //progressDialog.dismiss();
                Log.v("E_LOGIN", "Logged in failed with code " + firebaseError.getCode());
                Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
        /*----------------------------------------------*/

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

        /*Log.v("Value of root", mRootRef.toString());*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bToSignup:
                SignupFragment signupFragment = new SignupFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setCustomAnimations(R.anim.slide_from_right,R.anim.slide_to_left);
                fragmentTransaction.replace(R.id.flMainContainer,signupFragment);
                fragmentTransaction.commit();
                bToSignup.setVisibility(View.INVISIBLE);
                break;

        }
    }
}
