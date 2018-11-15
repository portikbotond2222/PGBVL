package com.example.portik.androidproject.feature;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button registerBtn;
    private Button loginBtn;
    private Button logoutBtn;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myAuth = FirebaseAuth.getInstance();
        registerBtn = (Button) findViewById(R.id.register_btn);
        loginBtn = (Button) findViewById(R.id.login_btn);
        logoutBtn = (Button) findViewById(R.id.logout_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLogin();
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToRegister();
            }
        });
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            registerBtn.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.VISIBLE);
            logoutBtn.setVisibility(View.INVISIBLE);
        }
        else{
            registerBtn.setVisibility(View.INVISIBLE);
            loginBtn.setVisibility(View.INVISIBLE);
            logoutBtn.setVisibility(View.VISIBLE);

        }
    }

    private void redirectToLogin() {
        Intent mainIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void redirectToRegister() {
        Intent mainIntent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(mainIntent);
        finish();
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
}
