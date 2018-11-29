package com.example.portik.androidproject.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmailText;
    private EditText loginPassText;
    private Button loginBtn;
    private Button regiszterBtn;
    private ProgressBar myprogressbar;
    private Button phoneAuthBtn;


    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myAuth = FirebaseAuth.getInstance();

        loginEmailText = (EditText) findViewById(R.id.editText2);
        loginPassText = (EditText) findViewById(R.id.editText3);
        loginBtn = (Button) findViewById(R.id.button);
        regiszterBtn= (Button) findViewById(R.id.button2);
        myprogressbar =  (ProgressBar) findViewById(R.id.progressBar);

        phoneAuthBtn = (Button) findViewById(R.id.phoneAuthButton);
        phoneAuthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToRPhoneAuth();
            }
        });

        regiszterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToReg();
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginEmail = loginEmailText.getText().toString();
                String loginPass = loginPassText.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    myprogressbar.setVisibility(View.VISIBLE);
                    myAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                redirectToMain();

                            }else{
                                String e = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : "+e, Toast.LENGTH_LONG).show();
                            }
                            myprogressbar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser currentUser = myAuth.getCurrentUser();

        if(currentUser != null){
            redirectToMain();
        }
    }

    private void redirectToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void redirectToReg() {
        Intent mainIntent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void redirectToRPhoneAuth(){
        Intent mainIntent = new Intent(LoginActivity.this, PhoneAutentification.class);
        startActivity(mainIntent);
        finish();
    }
}
