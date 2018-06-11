package com.scottshipp.code.algs4;

public class QuickUnionUF {

    int[] id;
    int count;

    QuickUnionUF(int n) {
        id = new int[n];
        for(int i = 0; i < id.length; i++) {
            id[i] = i;
        }
        count = n;
    }

    void union(int p, int q) {
        int rootOfP = findRoot(p);
        int rootOfQ = findRoot(q);
        if(rootOfP != rootOfQ) {
            id[rootOfP] = rootOfQ;
            count--;
        }
    }

    int findRoot(int n) {
        while(id[n] != n) {
            n = id[n];
        }
        return n;
    }

    boolean connected(int p, int q) {
        return findRoot(p) == findRoot(q);
    }

    int count() {
        return count;
    }
}
