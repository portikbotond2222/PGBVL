package com.example.portik.androidproject.app;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAutentification extends AppCompatActivity {

    private static final String TAG = "PhoneAuth";

    private EditText Code;
    private EditText Phone;
    private Button CodeVerify;
    private Button SendCode;
    private Button ResendCode;

    private String phoneVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks varificationCallBack;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    private FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_autentification);

        Phone = (EditText) findViewById(R.id.phonenumber);
        Code = (EditText) findViewById(R.id.code);
        CodeVerify = (Button) findViewById(R.id.verifycode);
        SendCode = (Button) findViewById(R.id.sendcode);
        ResendCode = (Button) findViewById(R.id.resendcode);
        SendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
        CodeVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyCode(view);
            }
        });
        ResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendCode(view);
            }
        });
        CodeVerify.setEnabled(false);
        ResendCode.setEnabled(false);

        myAuth = FirebaseAuth.getInstance();
    }
    public void sendCode(){
        String phoneNumber = Phone.getText().toString();
        setUpVerificationCallBack();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                varificationCallBack
        );
    }

    private void setUpVerificationCallBack() {
        varificationCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                ResendCode.setEnabled(false);
                CodeVerify.setEnabled(false);
                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if(e instanceof FirebaseAuthInvalidCredentialsException){
                    Log.d(TAG,"invalid credetial: " + e.getLocalizedMessage());
                }
                else if(e instanceof FirebaseTooManyRequestsException){
                    Log.d(TAG, "SMS quota esceeded.");
                }
            }
            @Override
            public void onCodeSent(String verifivationId, PhoneAuthProvider.ForceResendingToken token){
                phoneVerificationId =verifivationId;
                resendToken = token;
                CodeVerify.setEnabled(true);
                ResendCode.setEnabled(true);
                SendCode.setEnabled(false);
            }
        };
    }

    public void verifyCode(View view){
        String code = Code.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        myAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    ResendCode.setEnabled(false);
                    CodeVerify.setEnabled(false);
                    FirebaseUser user = task.getResult().getUser();
                    redirectToMain();
                }
                else{
                    if(task.getException() instanceof  FirebaseAuthInvalidCredentialsException){

                    }
                }
            }
        });
    }
    public void resendCode(View view){
        String phNumber = Phone.getText().toString();
        setUpVerificationCallBack();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phNumber,
                60,
                TimeUnit.SECONDS,
                this,
                varificationCallBack,
                resendToken);
    }
    public void redirectToMain(){
        Intent mainIntent = new Intent(PhoneAutentification.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
