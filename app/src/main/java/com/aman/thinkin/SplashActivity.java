package com.aman.thinkin;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.simplelogin.FirebaseSimpleLoginError;
import com.firebase.simplelogin.FirebaseSimpleLoginUser;
import com.firebase.simplelogin.SimpleLogin;
import com.firebase.simplelogin.SimpleLoginAuthenticatedHandler;

public class SplashActivity extends AppCompatActivity{
    private TextView splashTitle,splashSubTitle;
    private Typeface titleTypeface,subTitleTypeface;
    SimpleLogin simpleLogin;
    SimpleLoginAuthenticatedHandler simpleLoginAuthenticatedHandler;
    Firebase mRootRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Log.w("SPASH","CALLED");
        splashTitle = (TextView)findViewById(R.id.splashTitle);
        splashSubTitle = (TextView)findViewById(R.id.splashSubTitle);
        // set fonts
        titleTypeface = Typeface.createFromAsset(getAssets(),"fonts/Exo-Regular.ttf");
        splashTitle.setTypeface(titleTypeface);

        subTitleTypeface = Typeface.createFromAsset(getAssets(),"fonts/Exo-Thin.ttf");
        splashSubTitle.setTypeface(subTitleTypeface);

        if(!isNetworkAvailable()){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Check your internet connection!");
            alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            alertDialog.show();
        }

        Firebase.setAndroidContext(this);
        mRootRef = new Firebase("https://think-in.firebaseio.com/");
        simpleLogin = new SimpleLogin(mRootRef,this);
        simpleLoginAuthenticatedHandler = new SimpleLoginAuthenticatedHandler() {
            @Override
            public void authenticated(FirebaseSimpleLoginError firebaseSimpleLoginError, FirebaseSimpleLoginUser firebaseSimpleLoginUser) {
                if(firebaseSimpleLoginError!=null){
                    System.out.println(firebaseSimpleLoginError.getMessage());
                    Toast.makeText(SplashActivity.this,"Sorry. Login failed",Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(login);
                }
                if(firebaseSimpleLoginUser == null){
                    Toast.makeText(SplashActivity.this,"Sorry. No User Logged In",Toast.LENGTH_SHORT).show();
                    Intent login = new Intent(SplashActivity.this,MainActivity.class);
                    startActivity(login);
                }
                else{
                    Log.v("E_LOGIN", "Logged in");
                    Log.w("F_PROVIDER",firebaseSimpleLoginUser.getProvider().toString());
                    Toast.makeText(SplashActivity.this, "Logged in !!", Toast.LENGTH_SHORT).show();
                    Intent openHomeActivity = new Intent(SplashActivity.this,PostsActivity.class);
                    startActivity(openHomeActivity);
                }
            }
        };
        Toast.makeText(SplashActivity.this,"Please wait..",Toast.LENGTH_SHORT).show();
        simpleLogin.checkAuthStatus(simpleLoginAuthenticatedHandler);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isConnected();
    }

}
