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

public class RegisterActivity extends AppCompatActivity {
    private Button RegButton;
    private Button LogButton;
    private EditText Regemail;
    private EditText Regpsw;
    private FirebaseAuth myAuth;
    private ProgressBar myprogressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        RegButton = (Button) findViewById(R.id.registerReg);
        LogButton = (Button) findViewById(R.id.RegisterLog);
        Regemail = (EditText) findViewById(R.id.regEmail);
        Regpsw = (EditText) findViewById(R.id.regPsw);
        myAuth = FirebaseAuth.getInstance();
        myprogressbar =  (ProgressBar) findViewById(R.id.regProgressBar);
        LogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLogin();
            }
        });
        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
                String email= Regemail.getText().toString();
                String psw = Regpsw.getText().toString();
                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(psw)){

                        myprogressbar.setVisibility(View.VISIBLE);
                        myAuth.createUserWithEmailAndPassword(email,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    redirectToMain();
                                }
                                else{
                                    String errorMsg = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error: "+errorMsg, Toast.LENGTH_SHORT).show();

                                }
                                myprogressbar.setVisibility(View.VISIBLE);



                            }
                        });

                }
                else{
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        
        FirebaseUser currentUser = myAuth.getCurrentUser();
        if(currentUser == null){
            
        }
;    }
    private void redirectToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
    private void redirectToLogin() {
        Intent mainIntent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
