package com.scottshipp.code.algs4.exercises;

public class CircularRotation {

    public static boolean isRotation(String a, String b) {
        if(a == null || b == null) {
            throw new IllegalArgumentException("Null value found for a or b.");
        }
        if(a.length() != b.length()) {
            return false;
        }
        char[] aConverted = a.toCharArray();
        char[] bConverted = b.toCharArray();
        int bPointer = 0;
        for(int i = 0; i < a.length(); i++) {
            if(aConverted[i] == bConverted[bPointer]) {
                bPointer++;
            } else {
                bPointer = 0;
            }
            if(bPointer == bConverted.length) {
                return true;
            }
        }
        for(int i = 0; i < a.length(); i++) {
            if(aConverted[i] == bConverted[bPointer]) {
                bPointer++;
            } else {
                bPointer = 0;
            }
            if(bPointer == bConverted.length) {
                return true;
            }
        }
        return false;
    }
}
