package com.scottshipp.code.algs4;

import edu.princeton.cs.algs4.StdIn;

import java.util.Arrays;

public class QuickFindUF {

    int[] id;
    int count;

    QuickFindUF(int n) {
        id = new int[n];
        for(int i = 0; i < id.length; i++) {
            id[i] = i;
        }
        count = n;
    }

    void union(int p, int q) {
        if(id[p] == id[q]) {
            return;
        }
        int oldValue = id[p];
        boolean newConnection = false;
        for(int i = 0; i < id.length; i++) {
            if(id[i] == oldValue) {
                id[i] = id[q];
                newConnection = true;
            }
        }
        if(newConnection) {
            count--;
        }
    }

    int find(int n) {
        return n;
        // unimplemented
    }

    boolean connected(int p, int q) {
        return id[p] == id[q];
    }

    int count() {
        return count;
    }

    @Override
    public String toString() {
        return Arrays.toString(id);
    }

}
