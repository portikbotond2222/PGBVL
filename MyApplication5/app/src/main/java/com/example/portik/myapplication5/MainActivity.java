package com.example.portik.myapplication5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Button loginRegBtn;
    private Button LogoutBtn;
    private FirebaseAuth myAuth;

    private Uri imageCaptureUri;
    private ImageView mImageView;
    private Button cameraBtn;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myAuth = FirebaseAuth.getInstance();

        loginRegBtn = (Button) findViewById(R.id.loginreg_buton);
        LogoutBtn =  (Button) findViewById(R.id.LogoutBtn);

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

        final String[] items = new String[] {"From Cramera", "From SD Card"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item,items);
        AlertDialog.Builder builder =  new AlertDialog.Builder(this);
        builder.setTitle("Select Image");
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent,1);
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,2);
                }
            }

        });

        final AlertDialog dialog = builder.create();
        mImageView = (ImageView) findViewById(R.id.imageView);
        cameraBtn = (Button) findViewById(R.id.camera);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if(requestCode==1) {
            bitmap = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            mImageView.setImageBitmap(bitmap);
        }
        else if(requestCode == 2){
            Uri imageuri = data.getData();// Get intent
            try {
                bitmap = decodeUri(this, imageuri, 300);// call
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (bitmap != null)
                mImageView.setImageBitmap(bitmap);
        }
    }

    public String getRealPathFromUri(MainActivity mainActivity, Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(contentUri,proj, null,null,null);
        if(cursor == null){
            return null;
        }
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    public static Bitmap decodeUri(Context context, Uri uri,
                                   final int requiredSize) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o);

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;

        while (true) {
            if (width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver()
                .openInputStream(uri), null, o2);
    }
}
