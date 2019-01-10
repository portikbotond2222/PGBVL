package com.example.portik.myapplication5;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class MyProfile extends AppCompatActivity {
    Button setupProfilefragmentBtn, updateProfilefragmentBtn;

    private FirebaseAuth myAuth;
    private FirebaseFirestore db;
    String userId;
    Userdata userdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        setupProfilefragmentBtn =  findViewById(R.id.setupFragmentbutton);
        updateProfilefragmentBtn =  findViewById(R.id.updateButtonbutton);
        Log.d("akarmi3", "teszt");
        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        if(watchdata(userId)){
            db.collection("userdata").whereEqualTo("userid",userId).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("akarmi", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d("akarmi2", "Error getting documents: ", task.getException());
                        }


                }
            });
        }



//        Log.d("setupProfile", setupProfilefragmentBtn.toString());
        setupProfilefragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// load First Fragment
                loadFragment(new SetupProfileFragment());
            }
        });
// perform setOnClickListener event on Second Button
        updateProfilefragmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// load Second Fragment
                loadFragment(new UpdateProfileFragment());
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

    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    Boolean watcher = false;
    public boolean watchdata(String userId){

       db.collection("userdata").whereEqualTo("userid",userId).limit(1).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    watcher = true;
                } else {
                    watcher = false;
                }
            }
        });
       if(watcher == true){
           return true;
       }
       else{
           return false;
       }
    }

}
