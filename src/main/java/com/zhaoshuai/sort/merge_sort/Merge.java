package com.zhaoshuai.sort.merge_sort;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * 原地归并的抽象方法
 */
public class Merge {
    private static Comparable[] aux;
    public static void merge(Comparable[]a ,int lo,int mid,int hi){
        int i=lo, j=mid +1;
        for(int k=lo;k<=hi;k++)
            aux[k]=a[k];
        for(int k=lo;k<=hi;k++){
            if(i >mid) a[k]=aux[j++];
            else if(j>hi) a[k]=aux[i++];
            else  if(less(aux[j],aux[i])) a[k]=aux[j++];
            else  a[k]=aux[i++];
        }
    }

    /**
     * 自顶向下的归并排序
     * 基于原地归并的抽象方法实现的另一种递归归并。
     * @param a
     */
    public static void sort(Comparable[]a){
        /*
        排序算法
         */
        aux=new Comparable[a.length];
        sort(a,0,a.length-1);

    }
    public static void sort(Comparable[] a ,int lo,int hi ){
        // 将数组 a[lo,hi] 排序
        if(hi<=lo) return;
        int mid=lo+(hi-lo)/2;
        sort(a,lo,mid);
        sort(a,mid+1,hi);
        merge(a,lo,mid ,hi);
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
        String [] a= {"M","E","R","G","E","S","O","R","T","E","X","A","M","P","L","E"};
        sort(a);
        assert  isSorted(a);
        show(a);
    }
}
