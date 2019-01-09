package com.example.portik.myapplication5;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


public class MyProfile extends AppCompatActivity {

    private FirebaseFirestore db;

    private static final String TAG = "MyAuth";

    private Button SaveProfile;
    private EditText firstName;
    private EditText lastName;
    private EditText Address;
    private FirebaseAuth myAuth;
    CollectionReference dbUserdata;
    CollectionReference updateData;
    Userdata ud;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        myAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        firstName = (EditText) findViewById(R.id.firstnametext);
        lastName = (EditText) findViewById(R.id.lastnametext);
        Address = (EditText) findViewById(R.id.postaltext);
        SaveProfile = (Button) findViewById(R.id.buttonsave);
        SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String fname = firstName.getText().toString().trim();
                String lname = lastName.getText().toString().trim();
                String address = Address.getText().toString().trim();
                if(validateInputs(userId,fname,lname,address)){
                     dbUserdata = db.collection("userdata");

                     ud = new Userdata(userId,fname,lname,address);
                    saveUserdata(dbUserdata,ud);
//                    updateData = dbUserdata.whereEqualTo("userid", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            dbUserdata.whereEqualTo("userid", userId).;
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//
//                        }
//                    });


                }
            }
        });
    }

    public static class Userdata {
        public String userid;
        public String firstname;
        public String lastname;
        public String adress;


        public Userdata(String userid,String firstname,String lastname,String adress) {
            this.firstname = firstname;
            this.lastname = lastname;
            this.userid = userid;
            this.adress = adress;
        }
    }

    private boolean validateInputs(String userId, String firstname, String lastname, String Address) {
        if (userId.isEmpty() || firstname.isEmpty() || lastname.isEmpty() ||Address.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
    public void saveUserdata(CollectionReference dbUserdata, Userdata ud){
        dbUserdata.add(ud).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(MyProfile.this,"Userdata added",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MyProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}
