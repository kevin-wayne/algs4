package com.leetcode;

import java.util.ArrayList;

import static java.lang.Math.pow;

class ListNode{
    int val;
    ListNode next;
    ListNode(int x){val=x;}
}
public class AddTwoNum {
    public ListNode addTwoNumbers(ListNode l1,ListNode l2){
        ArrayList<Integer> L1=new ArrayList<>();
        ArrayList<Integer> L2=new ArrayList<>();
        do {
            L1.add(l1.val);
            l1=l1.next;

        }
        while (l2.next!=null);
        do {
            L2.add(l2.val);
            l2=l2.next;

        }
        while (l2.next!=null);
        double l1Int=0;double l2Int=0;
        for(int i=0;i<L1.size();i++){
            l1Int=l1Int+pow(10,L1.get(i));
        }
        for(int i=0;i<L2.size();i++){
            l2Int=l2Int+pow(L2.get(i),10);
        }
        ListNode result=null;
        ListNode temp2=null,temp3=null;
        double temp=l1Int+l2Int;
        String temp1=Double.toString(temp);
        for(int i=0;i<(temp/10+1);i++){

             temp2=new ListNode(temp1.charAt(i));
            if(i==0)
                temp2.next=null;
            else
                temp2.next=temp3;
            temp3=temp2;
        }
        result=temp2;
        return result;
    }
}

