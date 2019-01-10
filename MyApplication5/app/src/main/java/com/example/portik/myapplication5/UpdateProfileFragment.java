package com.example.portik.myapplication5;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UpdateProfileFragment extends Fragment {

    View view;
    private EditText firstNameup;
    private EditText lastNameup;
    private EditText Address;
    private Button updateBtn;
    private ProgressBar myprogressbar;
    private FirebaseAuth myAuth;
    private FirebaseFirestore db;
    private static final String TAG = "GET_QUERY";

    String userId;
    MyProfile.Userdata userdata;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        myAuth = FirebaseAuth.getInstance();

        firstNameup = view.findViewById(R.id.fnfragup);
        lastNameup = view.findViewById(R.id.lnfragup);
        Address = view.findViewById(R.id.addrfragup);
        updateBtn = view.findViewById(R.id.updatefrag);
        myprogressbar =  (ProgressBar) view.findViewById(R.id.progressBar2);

        db = FirebaseFirestore.getInstance();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                updateUserdata(userId);

            }
        });


        return view;
    }

    private void redirectToMain() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        startActivity(mainIntent);
        getActivity().finish();
    }

    private void updateUserdata(String userId){
        String firstName = firstNameup.getText().toString().trim();
        String lastName = lastNameup.getText().toString().trim();
        String addr = Address.getText().toString().trim();

        userdata = new MyProfile.Userdata(userId,firstName,lastName,addr);

        Query path = db.collection("userdata").whereEqualTo("userid",userId).limit(1) ;
        path.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getId());
                        db.collection("userdata").document(document.getId()).set(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getActivity(),"Userdata updated",Toast.LENGTH_LONG).show();
                                redirectToMain();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
