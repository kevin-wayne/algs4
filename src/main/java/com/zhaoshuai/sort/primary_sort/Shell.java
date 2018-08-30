package com.zhaoshuai.sort.primary_sort;

import edu.princeton.cs.algs4.StdOut;

public class Shell {

    /**
     * 希尔排序:
     * 希尔希尔排序更高效的原因是它权衡了子数组的规模和有序性。
     * 希尔排序为了加快速度简单地改进了插入排序，交换不相邻的元素
     * 以对数组的局部进行排序，并最终用插入排序将局部有序的数组排序。
     * 希尔排序的思想是使用数组中任意间隔为h的元素都是有序的。这样的数组被称为h有序数组。
     * @param a
     */
    public static void sort(Comparable[]a){
        /*
        排序算法： 将a[] 按照升序排列
         */
        int N =a.length;
        int  h=1;
        while(h<N/3) h=3*h+1;
        while(h>=1){
            for(int i=h;i<N;i++){
                for(int j=i;j>=h&&less(a[j],a[j-h]);j-=h)
                    exch(a,j,j-h);
            }
            h=h/3;
        }
    }
    //比较，若v<w ,则返回false，否                    则返回true。
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

        String [] a= {"S","H","E","L","S","O","R","T","E"};
        sort(a);
        assert  isSorted(a);
        show(a);
    }
}
