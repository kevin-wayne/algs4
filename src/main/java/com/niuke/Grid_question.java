package com.niuke;

import java.util.Scanner;

public class Grid_question {
//    public static void main(String [] args){
//        Scanner in =new Scanner(System.in);
//        int x=in.nextInt();
//        int y=in.nextInt();
//        int n=number(x,y);
//        System.out.println(n);
//    }

    public static  void main(String[] args){
        Scanner sc=new Scanner(System.in);
        int x=sc.nextInt();
        int y =sc.nextInt();
        int [] [] arr= new int [x+1][y+1];
        for (int i=0;i<=x;i++){
            arr[i][0]=1;
        }
        for(int i=0;i<=y;i++){
            arr[0][i]=1;
        }
        for (int i=1;i<=x;i++){
            for(int j=1;j<=y;j++){
                arr[i][j]=arr[i-1][j]+arr[i][j-1];
            }
        }
        System.out.println(arr[x][y]);
        sc.close();
    }
    public static  int number(int x,int y ){
        if(x==0|| y==0) return 1;
        else return number(x-1,y)+number(x,y-1);
    }
}
