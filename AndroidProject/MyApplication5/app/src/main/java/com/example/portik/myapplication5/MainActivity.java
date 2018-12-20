package com.example.portik.myapplication5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Button loginRegBtn;
    private Button LogoutBtn;
    private FirebaseAuth myAuth;
    public static final int CAMERA_REQUEST=9999;
    private ImageView imageView;
    private Button cameraBtn;

    private String photoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAuth = FirebaseAuth.getInstance();

        loginRegBtn = (Button) findViewById(R.id.loginreg_buton);
        LogoutBtn =  (Button) findViewById(R.id.LogoutBtn);

        cameraBtn=(Button) findViewById(R.id.camera);


        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
        loginRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLoginReg();
            }
        });

        imageView=(ImageView) findViewById(R.id.imageView);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCamera(view);
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser == null){
            loginRegBtn.setVisibility(View.VISIBLE);
            LogoutBtn.setVisibility((View.INVISIBLE));


        }
        else{
            loginRegBtn.setVisibility(View.INVISIBLE);
            LogoutBtn.setVisibility(View.VISIBLE);
        }
    }

    private void redirectToMain() {
        Intent mainIntent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void logOut(){
        myAuth.signOut();
        redirectToMain();
    }

    private void redirectToLoginReg(){
        Intent mainIntent = new Intent(MainActivity.this, LoginRegisterActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void openCamera(View view)
    {
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try{
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(photoFile != null)
            {
                Uri photoURI = FileProvider.getUriForFile(this,"com.example.portik.myapplication5",photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(intent,CAMERA_REQUEST);
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CAMERA_REQUEST)
        {
            Bitmap bitmap=(Bitmap)data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private File createImageFile() throws IOException{
        String tmp=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imgFileName="JPEG_"+tmp + " _";
        File storageDir=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=File.createTempFile(
                imgFileName,
                ".jpg",
                storageDir
        );

        photoPath=image.getAbsolutePath();
        return image;
    }

}
