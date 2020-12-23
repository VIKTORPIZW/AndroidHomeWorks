package com.example.hw2;

import android.util.Log;

import java.util.ArrayList;

public class LogD {
    public void getLogs(ArrayList<Integer> numberList, double sum, double average, double resultNumberThree) {
        Log.d("...", "...");
        Log.d("FirstActivity", " SET OF NUMBERS " + numberList);
        Log.d("FirstActivity", "Amount of numbers = " + sum);
        Log.d("FirstActivity", "Average of numbers = " + average);
        Log.d("FirstActivity", "Result of three task = " + resultNumberThree);
    }
}
