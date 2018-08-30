package com.zhaoshuai.sort.primary_sort;

import edu.princeton.cs.algs4.StdOut;

/**
 * 插入排序： 在计算机的实现中，为了给要插入的元素腾出空间，我们需要将其余的所有元素在插入之前都向右移动一位，这种算法叫做插入排序。
 * 插入排序所需的时间取决于输入元素的初始顺序。
 * 对于随机排列的长度为N且主键不重复的数组，
 * 平均情况下插入排序需要-N^2/4 次比较和N^2/4 次交换。
 * 最坏情况下需要N^2/2 次比较和N^2/2 次交换。
 * 最好情况下 需要N-1 次比较以及0次交换。
 */
public class Insertion {
    public static void sort(Comparable[]a){
        /*
        插入排序算法
         */
        for(int i=1;i<a.length;i++)
            for(int j=i;j>0 && less(a[j],a[j-1]);j--)
                exch(a,j,j-1);

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

        String []a ={"Y","E","W","B","A"};
        sort(a);
        assert  isSorted(a);
        show(a);
    }
}
