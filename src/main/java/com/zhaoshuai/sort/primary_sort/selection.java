package com.zhaoshuai.sort.primary_sort;

import edu.princeton.cs.algs4.StdOut;

/**
 * 选择排序
 * 一种最简单的排序算法，首先找到数组最小的哪个元素，其次，将它与数组的第一个元素交换位置。
 * 再次，在剩下的元素中找到最小的元素，将它与数组的第二个元素交换位置。如此直至将整个数组排序。
 * 选择排序需要大约N^2/2次比较和N次交换
 * 选择排序两个鲜明的特点：
 * 1。运行时间与输入无关（与输入时数组的状态无关）
 * 2。 数据移动是最少的。
 *
 */
public class selection {
    public static void sort(Comparable[]a){
        /*
        排序算法
         */
        for(int i =0;i<a.length;i++) {
            int min=i;
            for (int j = 1; j < a.length; j++)
                if (less(a[j],a[min])) min=j;
                exch(a,i,min);
        }

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

//        String [] a= In.readStrings();
        String []a ={"E","B"};
        sort(a);
        assert  isSorted(a);
        show(a);
    }
}
