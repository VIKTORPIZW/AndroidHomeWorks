package com.example.hw2;

import java.util.ArrayList;

public class Calculator {
    public double sumOfArrayList(ArrayList<Integer> listOfNumbers) {
         double sum = 0;
        for (int a : listOfNumbers) {
            sum = sum + a;
        }
        return sum;
    }
    public double averageOfArrayList(ArrayList<Integer> numberList) {
        double sum = 0;
        for (int h : numberList) {
            sum = sum + h;
        }
        return sum / numberList.size();
    }

    public double taskThree(ArrayList<Integer> numberList) {
        double onePart = 0;
        double twoPart = 0;
            if (numberList.size() % 2 == 0) {
                for (int i = 0; i < (int) (numberList.size() / 2); i++) {
                onePart = onePart + numberList.get(i);
            }
            for (int i = (int) (numberList.size() / 2); i < numberList.size(); i++) {
                twoPart = twoPart - numberList.get(i);
            }
        } else {
            for (int i = 0; i < (int) ((numberList.size() - 1) / 2); i++) {
                onePart = onePart + numberList.get(i);
            }
            for (int i = (int) ((numberList.size() + 1) / 2); i < numberList.size(); i++) {
                twoPart = twoPart - numberList.get(i);
            }
        }
        return onePart / twoPart;
    }

}
