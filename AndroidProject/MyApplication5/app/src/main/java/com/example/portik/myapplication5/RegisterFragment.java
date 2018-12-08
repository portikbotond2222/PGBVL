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


public class RegisterFragment extends Fragment {

    View view;
    Button secondButton;
    private Button RegButton;
    private EditText Regemail;
    private EditText Regpsw;
    private EditText Regpsw2;
    private FirebaseAuth myAuth;
    private ProgressBar myprogressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.registerfrag_layout, container, false);
        RegButton = (Button) view.findViewById(R.id.registerFragment);
        Regemail = (EditText) view.findViewById(R.id.emailRegFrag);
        Regpsw = (EditText) view.findViewById(R.id.passwordRegFrag);
        Regpsw2 = (EditText) view.findViewById(R.id.passwordRegFrag2);
        myAuth = FirebaseAuth.getInstance();
        myprogressbar =  (ProgressBar) view.findViewById(R.id.regProgressBar);

        RegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email= Regemail.getText().toString();
                String psw = Regpsw.getText().toString();
                String psw2 = Regpsw2.getText().toString();

                if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(psw) && !TextUtils.isEmpty(psw2)){

                    myprogressbar.setVisibility(View.VISIBLE);
                    if(psw2.equals(psw)){
                        myAuth.createUserWithEmailAndPassword(email,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    redirectToMain();
                                }
                                else{
                                    String errorMsg = task.getException().getMessage();
                                    Toast.makeText(getActivity(), "Error: "+errorMsg, Toast.LENGTH_SHORT).show();

                                }
                                myprogressbar.setVisibility(View.VISIBLE);



                            }
                        });
                    }
                    else{
                        Toast.makeText(getActivity(), "Error: Password and Password again not equal", Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
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
