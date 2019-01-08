package com.example.portik.myapplication5;

import android.nfc.Tag;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyProfile extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private static final String TAG = "MyAuth";

    private Button SaveProfile;
    private EditText firstName;
    private EditText lastName;
    private EditText Address;
    private FirebaseAuth myAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        myAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference("userdata");
        firstName = (EditText) findViewById(R.id.firstnametext);
        lastName = (EditText) findViewById(R.id.lastnametext);
        Address = (EditText) findViewById(R.id.postaltext);
        SaveProfile = (Button) findViewById(R.id.buttonsave);
        SaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
               writeNewUser("1",firstName.getText().toString(),lastName.getText().toString(),Address.getText().toString());
            }
        });
    }

    public static class User {
//        public String userid;
        public String firstname;
        public String lastname;
        public String adress;


        public User(String firstname,String lastname,String adress) {
            this.firstname = firstname;
            this.lastname = lastname;
//            this.userid = userid;
            this.adress = adress;
        }
    }


    private void writeNewUser(String userid, String firstname, String lastname,String adress) {
        User user = new User(firstname, lastname,adress);

        mDatabase.setValue(user);
    }



}
