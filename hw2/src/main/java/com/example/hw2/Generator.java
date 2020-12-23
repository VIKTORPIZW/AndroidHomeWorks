package com.example.hw2;

import java.util.ArrayList;
import java.util.HashSet;

public class Generator {

    public ArrayList<Integer> generateSet(int minValueOfCounts, int maxValueOfCounts, int maxValueOfNumbers) {
        HashSet<Integer> hashSet = new HashSet<>();
        int hashSetSize = minValueOfCounts + (int) (Math.random() * ((maxValueOfCounts - minValueOfCounts + 1)));
        for (int i = 0; i < hashSetSize; i++) {
            hashSet.add(((int) (Math.random() * maxValueOfNumbers)) + 1);
        }
        return new ArrayList<>(hashSet);
    }
}