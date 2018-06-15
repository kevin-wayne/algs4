package com.scottshipp.code.algs4;

import java.util.logging.Logger;

class Merge<T extends Comparable<T>> {
    private static Logger logger = Logger.getLogger(Merge.class.toString());

    private T[] in;
    private Object[] out;

    @SuppressWarnings("unchecked")
    Merge(T[] in) {
        this.in = in;
        out = new Object[in.length];
    }

    @SuppressWarnings("unchecked")
    public void sort() {
        sort(0, in.length - 1);
    }

    private void sort(int lo, int hi) {
        if (hi <= lo) {
            return;
        }
        int mid = lo + (hi - lo) / 2;
        sort(lo, mid);
        sort(mid + 1, hi);
        merge(lo, mid, hi);
    }

    @SuppressWarnings("unchecked")
    private void merge(int lo, int mid, int hi) {
        int loPointer = lo;
        int hiPointer = mid + 1;
        for(int currentIndex = lo; currentIndex <= hi; currentIndex++) {
            if(loPointer > mid) {
                out[currentIndex] = in[hiPointer++];
            } else if(hiPointer > hi) {
                out[currentIndex] = in[loPointer++];
            } else if(isLessThan(in[loPointer], in[hiPointer])) {
                out[currentIndex] = in[loPointer++];
            } else {
                out[currentIndex] = in[hiPointer++];
            }
        }

        for(int i = lo; i <= hi; i++) {
            in[i] = (T)out[i];
        }
    }

    Object[] getSorted() {
        return out;
    }

    private boolean isLessThan(T v, T w) {
        return v.compareTo(w) < 0;
    }

}
