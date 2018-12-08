package com.example.portik.myapplication5;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

public class PhoneAuthFragment extends Fragment {

    View view;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.phoneauthfrag_layout, container, false);
        Code  = (EditText) view.findViewById(R.id.codeFrag);
        CodeVerify = (Button) view.findViewById(R.id.verifycodefrag);
        Phone = (EditText) view.findViewById(R.id.phonenumber);
        SendCode = (Button) view.findViewById(R.id.sendcode);
        ResendCode = (Button) view.findViewById(R.id.resendcode);
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
        return view;
    }
    public void sendCode(){
        String phoneNumber = Phone.getText().toString();
        Log.e("phone",phoneNumber);
        setUpVerificationCallBack();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                getActivity(),
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
                getActivity(),
                varificationCallBack,
                resendToken);
    }
    public void redirectToMain(){
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }

}
