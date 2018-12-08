package com.example.portik.myapplication5;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class LoginRegisterActivity extends AppCompatActivity {
    Button loginFragment, registerFragment,phoneAutetFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        loginFragment = (Button) findViewById(R.id.loginFragment);
        registerFragment = (Button) findViewById(R.id.RegisterFragment);
        phoneAutetFragment = (Button) findViewById(R.id.PhoneAuthBut);

        loginFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// load First Fragment
                loadFragment(new LoginFragment());
            }
        });
// perform setOnClickListener event on Second Button
        registerFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// load Second Fragment
                loadFragment(new RegisterFragment());
            }
        });

        phoneAutetFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// load Second Fragment
                loadFragment(new PhoneAuthFragment());
            }
        });


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
}
