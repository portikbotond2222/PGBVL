package com.example.portik.myapplication5;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button loginRegBtn;
    private Button LogoutBtn;
    private FirebaseAuth myAuth;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAuth = FirebaseAuth.getInstance();

        loginRegBtn = (Button) findViewById(R.id.loginreg_buton);
        LogoutBtn =  (Button) findViewById(R.id.LogoutBtn);

        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLoginReg();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            loginRegBtn.setVisibility(View.VISIBLE);
            LogoutBtn.setVisibility((View.INVISIBLE));


        }
        else{
            loginRegBtn.setVisibility(View.INVISIBLE);
            LogoutBtn.setVisibility(View.VISIBLE);
        }
    }

    private void redirectToMain() {
        Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void logOut(){
        myAuth.signOut();
        redirectToMain();
    }

    private void redirectToLoginReg(){
        Intent mainIntent = new Intent(MainActivity.this, LoginRegisterActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
