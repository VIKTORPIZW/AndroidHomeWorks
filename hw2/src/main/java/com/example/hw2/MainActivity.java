package com.example.hw2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "FirstActivity";
    private static final String NUMBERS_OF_RANDOM_HASH_SET = "HASH_SET_NUMBERS";
    private Animation animation;
    private Button button;
    private Animation animation3;
    private Button button2FirstActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        animation = AnimationUtils.loadAnimation(this,R.anim.button_anim);
        animation3 = AnimationUtils.loadAnimation(this,R.anim.button_anim2);
        button = (Button) findViewById(R.id.buttonGenerate);
        button2FirstActivity=(Button) findViewById(R.id.restButton);
        button.startAnimation(animation);
        findViewById(R.id.buttonGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Generator generator = new Generator();
                startActivityForResult(SecondActivity.newInstance(MainActivity.this,
                        generator.generateSet(4, 25, 100),
                        NUMBERS_OF_RANDOM_HASH_SET ),1);

            }
        });
        Log.d(TAG, "onCreate");

        button2FirstActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.startAnimation(animation3);
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data != null) {
            LogD logD = new LogD();
            logD.getLogs(data.getIntegerArrayListExtra("ListOfNumbers"), data.getDoubleExtra("RESULT_TASK_1", 0),
                    data.getDoubleExtra("RESULT_TASK_2", 0), data.getDoubleExtra("RESULT_TASK_3", 0));
        }
        super.onActivityResult(requestCode, resultCode, data);
        button.startAnimation(animation);

    }
}

