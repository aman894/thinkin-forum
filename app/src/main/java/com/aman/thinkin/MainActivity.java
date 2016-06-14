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
