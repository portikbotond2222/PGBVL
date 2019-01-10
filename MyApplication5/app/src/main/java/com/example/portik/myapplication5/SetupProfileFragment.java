package com.example.portik.myapplication5;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SetupProfileFragment extends Fragment {
    View view;
    private EditText firstName;
    private EditText lastName;
    private EditText Address;
    private Button saveBtn;
    private ProgressBar myprogressbar;
    private FirebaseAuth myAuth;
    private FirebaseFirestore db;

    CollectionReference dbUserdata;
    MyProfile.Userdata ud;
    String userId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_setup_profile, container, false);

        myAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

       firstName = view.findViewById(R.id.fnfrag);
       lastName = view.findViewById(R.id.lnfrag);
       Address = view.findViewById(R.id.addrfrag);
       saveBtn = view.findViewById(R.id.savefrag);
        myprogressbar =  (ProgressBar) view.findViewById(R.id.progressBar2);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = firstName.getText().toString().trim();
                String lname= firstName.getText().toString().trim();
                String addr = Address.getText().toString().trim();
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(validateInputs(userId,fname,lname,addr)){
                    dbUserdata = db.collection("userdata");

                    ud = new MyProfile.Userdata(userId,fname,lname,addr);
                    saveUserdata(dbUserdata,ud);
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


    private boolean validateInputs(String userId, String firstname, String lastname, String Address) {
        if (userId.isEmpty() || firstname.isEmpty() || lastname.isEmpty() ||Address.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public void saveUserdata(CollectionReference dbUserdata, MyProfile.Userdata ud){
        dbUserdata.add(ud).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(getActivity(),"Userdata added",Toast.LENGTH_LONG).show();
                redirectToMain();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
