package com.zhaoshuai.sort.merge_sort;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

//import edu.princeton.cs.algs4
public class sort_example {
    public static void sort(Comparable[]a){
        /*
        排序算法
         */
    }
    //比较，若v<w ,则返回false，否则返回true。
    private static boolean less(Comparable v,Comparable w){
        return v.compareTo(w)<0;
    }
    // 交换 a[i] 和a[j]进行交换
    private static void exch(Comparable[] a ,int i,int j){
        Comparable t=a[i];
        a[i]=a[j];
        a[j]=t;
    }
    // 打印集合comparable
    private static void show(Comparable[]a){
        for(int i=0;i<a.length;i++)
            StdOut.print(a[i]+"  ");
            StdOut.println();
    }
    public static boolean isSorted(Comparable[] a ){
        for(int i=1;i<a.length;i++)
            if(less(a[i],a[i-1]))
                return false;
        return true;
    }

    public static void main(String args[]){

        String [] a= In.readStrings();
        sort(a);
        assert  isSorted(a);
        show(a);
    }

}
