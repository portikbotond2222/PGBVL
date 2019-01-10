package com.example.portik.myapplication5;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {

    View view;
    private EditText Email;
    private EditText Password;
    private Button Login;
    private ProgressBar myprogressbar;
    private FirebaseAuth myAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.loginfrag_layout, container, false);

        myAuth = FirebaseAuth.getInstance();

        Email = (EditText) view.findViewById(R.id.emailFragment);
        Password = (EditText) view.findViewById(R.id.passwordFragment);
        Login = (Button) view.findViewById(R.id.loginFragment);
        myprogressbar =  (ProgressBar) view.findViewById(R.id.progressBar2);




        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String loginEmail = Email.getText().toString();
                String loginPass = Password.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)){
                    myprogressbar.setVisibility(View.VISIBLE);
                    myAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                redirectToMain();

                            }else{
                                String e = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Error : "+e, Toast.LENGTH_LONG).show();
                            }
                            myprogressbar.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });


        return view;
    }



    private void redirectToMain() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }


}
