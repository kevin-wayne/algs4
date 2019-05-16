import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.*;
public class Insertion {

   public static void sort(Comparable[] a){
       int N=a.length;
       for (int i = 0; i <N ; i++) {
           int min=i;
           for (int j = i+1; j <N; j++)
               if (less(a[j],a[min]))
                   min=j;
           exch(a,i,min);

       }
   }
   private static boolean less(Comparable v, Comparable w){//比较函数 v>=w,false ; v<w,true;
       return v.compareTo(w)<0;
   }
   private static void exch(Comparable[] a,int i,int j){//次序交互函数
       Comparable t=a[i];
       a[i]=a[j];
       a[j]=t;

   }
   private static void show(Comparable[] a){//展示函数
       for (int i = 0; i <a.length; i++) {
           StdOut.print(a[i]+" ");
  }
       StdOut.println("");
   }
   public static boolean isSorted(Comparable[] a){//测试函数
       for (int i = 1; i <a.length; i++) {
           if (less(a[i],a[i-1])) {
               System.out.print("error+\n");
               return false;
           }
       }
       System.out.println("success");
       return true;
   }
   public static void main(String[] args){
       //String[] a= new In("tiny.txt").readAllStrings();//In.readStrings();
       int N=20;
        Double a[]=new Double[N];
       for (int i = 0; i <N; i++)
           a[i]=StdRandom.uniform();//Double.valueOf(N-i);
       show(a);
       System.out.println(">>>>>>>>>>>>>>>>>>>>>>>");
       sort(a);
       isSorted(a);
       show(a);
   }
}
