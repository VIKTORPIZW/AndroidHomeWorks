package com.example.hw2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.ArrayList;

public class SecondActivity extends MainActivity {
    private static final String TAG2 = "SecondActivity";
    private Animation animation2;
    private Button button2;

    private double sum;
    private double average;
    private double resultNumberThree;
    private ArrayList<Integer> numberList;

    public static Intent newInstance(Context context, ArrayList<Integer> numberList, String name) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra(name, numberList);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d(TAG2, "onCreate");

        final Intent intent = getIntent();
        if (intent != null) {
            numberList = intent.getIntegerArrayListExtra("HASH_SET_NUMBERS");
        }
        findViewById(R.id.buttonResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calculator calculator = new Calculator();
                sum = calculator.sumOfArrayList(numberList);
                average = calculator.averageOfArrayList(numberList);
                resultNumberThree = calculator.taskThree(numberList);
                Intent resultIntent = new Intent();
                resultIntent.putExtra("ListOfNumbers", numberList);
                resultIntent.putExtra("RESULT_TASK_1", sum);
                resultIntent.putExtra("RESULT_TASK_2", average);
                resultIntent.putExtra("RESULT_TASK_3", resultNumberThree);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        animation2 = AnimationUtils.loadAnimation(this,R.anim.button_anim);
        button2 = (Button) findViewById(R.id.buttonResult);
        button2.startAnimation(animation2);
    }


}