package com.example.portik.androidproject.feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity  {
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private Button button7;
    private Button button8;
    private Button button9;
    public int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        button5 = findViewById(R.id.button5);
        button6 = findViewById(R.id.button6);
        button7 = findViewById(R.id.button7);
        button8 = findViewById(R.id.button8);
        button9 = findViewById(R.id.button9);

        this.setClickListener();
    }


    private void setClickListener(){
        button1.setOnClickListener(new MyOnClickListener());
        button2.setOnClickListener(new MyOnClickListener());
        button3.setOnClickListener(new MyOnClickListener());
        button4.setOnClickListener(new MyOnClickListener());
        button5.setOnClickListener(new MyOnClickListener());
        button6.setOnClickListener(new MyOnClickListener());
        button7.setOnClickListener(new MyOnClickListener());
        button8.setOnClickListener(new MyOnClickListener());
        button9.setOnClickListener(new MyOnClickListener());

    }

    class MyOnClickListener implements View.OnClickListener {
        public void onClick( View v){
            Button source = (Button) v;
            number = Integer.parseInt(source.getText().toString());
            if(number < 9){
                number++;
            }
            else{
                number = 0;
            }
            source.setText(String.valueOf(number));
        }

    }


}
